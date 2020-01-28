package com.iipsen2.app.modules.User.dao.seeders;

import com.github.javafaker.Faker;
import com.github.javafaker.service.FakeValuesService;
import com.github.javafaker.service.RandomService;
import com.iipsen2.app.interfaces.abstracts.SeederMethods;
import com.iipsen2.app.interfaces.enums.ModuleType;
import com.iipsen2.app.interfaces.enums.UserRoleType;
import com.iipsen2.app.modules.User.dao.UserDao;
import com.iipsen2.app.modules.User.models.User;
import com.iipsen2.app.modules.User.services.PasswordEncryptService;
import com.iipsen2.app.services.CoreService;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UserTableSeeder extends CoreService implements SeederMethods {
    public static final int DEFAULT_USERS = 2;
    public static final int TOTAL_RANDOM_USERS = 300;

    public UserTableSeeder(boolean seed) {
        if (seed) {
            run();
        } else {
            reset();
        }
    }

    private static boolean defaultUsersExist() {
        try {
            return getDao(ModuleType.USER, UserDao.class).findUserById(1)
                    .getUsername()
                    .equals("mazeyarr@gmail.com");
        } catch (NullPointerException e) {
            return false;
        }
    }

    @Override
    public boolean isAlreadySeeded() {
        return defaultUsersExist();
    }

    @Override
    public void run() {
        if (!isAlreadySeeded()) {
            insertDefaultUsers();
            insertRandomUsers();
        }
    }

    /**
     * CHANGE DEFAULT_USERS total to amount of default users added VERY IMPORTANT!
     */
    private void insertDefaultUsers() {
        try {
            // 1
            long userId = getDao(ModuleType.USER, UserDao.class).createUser(
                    new User(
                            "mazeyarr@gmail.com",
                            PasswordEncryptService.generateStrongPasswordHash("mazeyar123"),
                            "Mazeyar",
                            "Rezaei"
                    )
            );
            getDao(ModuleType.USER, UserDao.class).addRoleToUser(userId, UserRoleType.ADMIN);

            // 2
            userId = getDao(ModuleType.USER, UserDao.class).createUser(
                    new User(
                            "joeri@gmail.com",
                            PasswordEncryptService.generateStrongPasswordHash("12345"),
                            "Joeri",
                            "Duikeren"
                    )
            );
            getDao(ModuleType.USER, UserDao.class).addRoleToUser(userId, UserRoleType.ADMIN);
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            // TODO: Logger!
            System.err.println("Could not hash password...");
        }
    }

    private void insertRandomUsers() {
        FakeValuesService fakeValuesService = new FakeValuesService(
                new Locale("nl"), new RandomService()
        );
        Faker faker = new Faker(new Locale("nl"));

        for (int i = 0; i < TOTAL_RANDOM_USERS; i++) {
            String email = fakeValuesService.bothify("???????#####@gmail.com");
            Matcher emailMatcher = Pattern.compile("\\w{4}\\d{2}@gmail.com").matcher(email);

            try {
                if (emailMatcher.find()) {
                    if (getDao(ModuleType.USER, UserDao.class).findUserByUsername(email) == null) {
                        long userId = getDao(ModuleType.USER, UserDao.class).createUser(
                                new User(
                                        email,
                                        PasswordEncryptService.generateStrongPasswordHash("12345"),
                                        faker.name().firstName(),
                                        faker.name().lastName()
                                )
                        );

                        getDao(ModuleType.USER, UserDao.class).addRoleToUser(userId, UserRoleType.USER);
                    } else {
                        throw new Exception("User found!");
                    }
                } else {
                    throw new Exception("Fake email was faulty");
                }
            } catch (Exception e) {
                if (i == 0) {
                    break;
                }

                i--;
            }
        }
    }

    @Override
    public void reset() {

    }
}
