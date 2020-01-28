package com.iipsen2.app;

import com.iipsen2.app.checks.DatabaseHealthCheck;
import com.iipsen2.app.filters.AuthenticationFilter;
import com.iipsen2.app.interfaces.abstracts.UploadPaths;
import com.iipsen2.app.modules.Education.EducationModule;
import com.iipsen2.app.modules.Education.dao.seeders.EducationTableSeeder;
import com.iipsen2.app.modules.Institute.InstituteModule;
import com.iipsen2.app.modules.Institute.dao.seeders.InstituteTableSeeder;
import com.iipsen2.app.modules.Project.ProjectModule;
import com.iipsen2.app.modules.Project.dao.seeders.ProjectTableSeeder;
import com.iipsen2.app.modules.Tag.TagModule;
import com.iipsen2.app.modules.Tag.dao.seeders.TagTableSeeder;
import com.iipsen2.app.modules.Upload.ResourceModule;
import com.iipsen2.app.modules.Upload.dao.seeders.UploadTableSeeder;
import com.iipsen2.app.modules.User.UserModule;
import com.iipsen2.app.modules.User.dao.seeders.UserTableSeeder;
import com.iipsen2.app.providers.TokenProvider;
import com.iipsen2.app.services.Amazon.S3EncryptedService;
import com.iipsen2.app.services.Amazon.S3Service;
import io.dropwizard.Application;
import io.dropwizard.db.PooledDataSourceFactory;
import io.dropwizard.forms.MultiPartBundle;
import io.dropwizard.jdbi.DBIFactory;
import io.dropwizard.migrations.MigrationsBundle;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import io.github.cdimascio.dotenv.Dotenv;
import org.eclipse.jetty.servlets.CrossOriginFilter;
import org.glassfish.jersey.media.multipart.MultiPartFeature;
import org.skife.jdbi.v2.DBI;

import javax.servlet.DispatcherType;
import javax.servlet.FilterRegistration;
import java.util.EnumSet;

public class MainService extends Application<MainConfiguration> {
  public static final Dotenv env = Dotenv.configure()
      .directory(UploadPaths.LOCAL_RESOURCE_FOLDER_PATH)
      .load();
  public static TokenProvider tokenProvider;
  public static S3Service amazonS3;
  public static S3EncryptedService amazonS3Encrypted;


  /**
   * Execute server start
   *
   * @author Mazeyar Rezaei
   * @since 02-10-2019
   * @param args
   * @throws Exception
   */
  public static void main(String[] args) throws Exception {
    new MainService().run(args);
  }

  /**
   * Initialize Bundles
   *
   * @author Mazeyar Rezaei
   * @since 02-10-2019
   * @param bootstrap
   */
  @Override
  public void initialize(Bootstrap<MainConfiguration> bootstrap) {
    bootstrap.addBundle(new MigrationsBundle<MainConfiguration>() {
      @Override
      public PooledDataSourceFactory getDataSourceFactory(final MainConfiguration configuration) {
        return configuration.getDataSourceFactory();
      }
    });
    bootstrap.addBundle(new MultiPartBundle());
  }

  /**
   * Run server with main configurations
   *
   * @author Mazeyar Rezaei
   * @since 02-10-2019
   * @param configuration
   * @param environment
   */
  @Override
  public void run(MainConfiguration configuration, Environment environment) {
    final DBIFactory factory = new DBIFactory();
    final DBI jdbi = factory.build(environment, configuration.getDataSourceFactory(), "mysql");

    // Enable CORS headers
    final FilterRegistration.Dynamic cors =
            environment.servlets().addFilter("CORS", CrossOriginFilter.class);

    // Configure CORS parameters
    cors.setInitParameter(CrossOriginFilter.ACCESS_CONTROL_ALLOW_ORIGIN_HEADER, "*");
    cors.setInitParameter("allowedOrigins", "*");
    cors.setInitParameter("allowedHeaders", "X-Requested-With,Content-Type,Accept,Origin,Authorization,auth");
    cors.setInitParameter("allowedMethods", "OPTIONS,GET,PUT,POST,DELETE,HEAD");

    // Add URL mapping
    cors.addMappingForUrlPatterns(EnumSet.allOf(DispatcherType.class), true, "/*");

    environment.jersey().register(MultiPartFeature.class);

    /*
      Filters
     */
    environment.jersey().register(
            new AuthenticationFilter()
    );

    /*
      Modules
     */
    new UserModule(jdbi).registerModuleResources(environment);
    new InstituteModule(jdbi).registerModuleResources(environment);
    new EducationModule(jdbi).registerModuleResources(environment);
    new TagModule(jdbi).registerModuleResources(environment);
    new ProjectModule(jdbi).registerModuleResources(environment);
    new ResourceModule(jdbi).registerModuleResources(environment);


    /*
      Health Checks
     */
    environment.healthChecks().register("checks",
            new DatabaseHealthCheck(jdbi, configuration.getDataSourceFactory().getValidationQuery()));

    /*
      Initializable
     */
    tokenProvider = new TokenProvider();
    amazonS3 = new S3Service();
    amazonS3Encrypted = new S3EncryptedService();

    /*
      Module seeders
     */
    new UserTableSeeder(true);
    new InstituteTableSeeder(true);
    new EducationTableSeeder(true);
    new TagTableSeeder(true);
    new ProjectTableSeeder(true);
    new UploadTableSeeder(true);
  }
}
