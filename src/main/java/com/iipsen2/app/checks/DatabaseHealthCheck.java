package com.iipsen2.app.checks;

import com.codahale.metrics.health.HealthCheck;
import org.skife.jdbi.v2.DBI;
import org.skife.jdbi.v2.Handle;

/**
 * Checks if database is connected to the system
 *
 * @author Mazeyar Rezaei
 * @since 22-10-2019
 * @version 1.0
 */
public class DatabaseHealthCheck extends HealthCheck {
    private final DBI dbi;
    private final String validationQuery;

    /**
     * Initiates the health check, by running a validation query
     *
     * @author Mazeyar Rezaei
     * @param dbi
     * @param validationQuery
     */
    public DatabaseHealthCheck(DBI dbi, String validationQuery) {
        this.dbi = dbi;
        this.validationQuery = validationQuery;
    }

    @Override
    protected Result check() throws Exception {
        try {
            final Handle handle = dbi.open();

            handle.execute(validationQuery);

            handle.close();
        } catch (Exception e) {
            return Result.unhealthy("Database is not running! :(");
        }

        return Result.healthy("Database is healthy :)");
    }
}
