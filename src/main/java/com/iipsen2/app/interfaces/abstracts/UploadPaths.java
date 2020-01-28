package com.iipsen2.app.interfaces.abstracts;

import com.iipsen2.app.interfaces.enums.UploadType;

import java.util.UUID;

/**
 * Contains constants of upload paths
 */
public abstract class UploadPaths {
    public static final String LOCAL_RESOURCE_FOLDER_PATH = "src/main/resources";
    public static final String RESOURCE_RELATED_ENTITY_FORM_DATA_KEY = "entities";
    public static final String RESOURCE_FORM_DATA_KEY = "resource";

    private static final String ENCRYPTED_PATH = "encrypted/";

    private static final String CLIENT_SIDE_ENCRYPTED_PATH = "client_encryption/";
    private static final String SERVER_SIDE_ENCRYPTED_PATH = "sse_encryption/";

    private static final String PROJECT_PATH = "projects/";
    private static final String AVATAR_PATH = "avatars/";

    private static final String TEMP_FOLDER = "src/main/resources/uploads/temp/";


    public static String generateUploadKey(String encryptedFilename, UploadType uploadType, boolean clientSideEncrypt) {
        switch (uploadType) {
            case AVATAR:
                if (clientSideEncrypt) {
                    return ENCRYPTED_PATH + CLIENT_SIDE_ENCRYPTED_PATH + AVATAR_PATH + encryptedFilename;
                }

                return ENCRYPTED_PATH + SERVER_SIDE_ENCRYPTED_PATH + AVATAR_PATH + encryptedFilename;

            case PROJECT:
                if (clientSideEncrypt) {
                    return ENCRYPTED_PATH + CLIENT_SIDE_ENCRYPTED_PATH + PROJECT_PATH + encryptedFilename;
                }

                return ENCRYPTED_PATH + SERVER_SIDE_ENCRYPTED_PATH + PROJECT_PATH + encryptedFilename;

            default:
                throw new IllegalArgumentException("Wrong UploadType: " + uploadType);
        }
    }

    public static String generateTempFilePath(String filename, UploadType uploadType) {
        switch (uploadType) {
            case PROJECT:
                return TEMP_FOLDER + PROJECT_PATH + UUID.randomUUID().toString() + "-" + filename;
            case AVATAR:
                return TEMP_FOLDER + AVATAR_PATH + UUID.randomUUID().toString() + "-" + filename;
            default:
                return TEMP_FOLDER + UUID.randomUUID().toString() + "-" + filename;
        }
    }
}
