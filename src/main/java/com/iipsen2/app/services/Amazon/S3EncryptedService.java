package com.iipsen2.app.services.Amazon;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.services.s3.*;
import com.iipsen2.app.Credentials;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.model.*;

import java.io.*;

public class S3EncryptedService extends AmazonService implements S3ServiceMethods{
    private static final AmazonS3Encryption S3 = AmazonS3EncryptionClientBuilder
            .standard()
            .withRegion(Regions.EU_CENTRAL_1)
            .withCredentials(new AWSStaticCredentialsProvider(AWSCredentials))
            .withCryptoConfiguration(
                    new CryptoConfiguration(CryptoMode.EncryptionOnly)
                            .withAwsKmsRegion(Region.getRegion(Regions.EU_CENTRAL_1))
            )
            .withEncryptionMaterials(new KMSEncryptionMaterialsProvider(Credentials.AWS_KMS_CMK_KEY_ID))
            .build();

    private static AmazonS3Encryption getS3() {
        return S3;
    }

    @Override
    public S3Object getObject(String key) {
        return getS3().getObject(AWS_S3_BUCKET_NAME, key);
    }

    @Override
    public boolean putObject(String key, File object) {
        try {
            getS3().putObject(AWS_S3_BUCKET_NAME, key, object);

            return true;
        } catch (AmazonS3Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean deleteObject(String key) {
        try {
            getS3().deleteObject(AWS_S3_BUCKET_NAME, key);

            return true;
        } catch (AmazonS3Exception e) {
            return false;
        }
    }
}
