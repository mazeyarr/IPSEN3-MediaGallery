package com.iipsen2.app.services.Amazon;

import com.amazonaws.auth.BasicAWSCredentials;
import com.iipsen2.app.Credentials;

public class AmazonService {
    static final String AWS_S3_BUCKET_NAME = "ipsen2";
    static final BasicAWSCredentials AWSCredentials = new BasicAWSCredentials(
            Credentials.AWS_ACCESS_KEY_ID,
            Credentials.AWS_SECRET_ACCESS_KEY
    );
}
