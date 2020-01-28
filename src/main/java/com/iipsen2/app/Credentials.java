package com.iipsen2.app;

import com.iipsen2.app.helpers.CoreHelper;

public class Credentials extends CoreHelper {
    public static final String AWS_KMS_CMK_KEY_ID = getEnv().get("AWS_KMS_CMK_KEY_ID");
    public static final String AWS_USER = getEnv().get("AWS_USER");
    public static final String AWS_ACCESS_KEY_ID = getEnv().get("AWS_ACCESS_KEY_ID");
    public static final String AWS_SECRET_ACCESS_KEY = getEnv().get("AWS_SECRET_ACCESS_KEY");
}
