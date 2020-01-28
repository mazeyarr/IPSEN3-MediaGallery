package com.iipsen2.app.services.Amazon;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.HttpMethod;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.*;

import java.io.File;
import java.net.URL;
import java.util.Date;

public class S3Service extends AmazonService implements S3ServiceMethods{
    private static final AmazonS3 S3 = AmazonS3ClientBuilder
            .standard()
            .withCredentials(new AWSStaticCredentialsProvider(AWSCredentials))
            .withRegion(Regions.EU_CENTRAL_1)
            .build();

    @Override
    public S3Object getObject(String key) {
        return getS3().getObject(AWS_S3_BUCKET_NAME, key);
    }

    @Override
    public boolean putObject(String key, File object) {
        try {
            PutObjectRequest putObjectRequest = new PutObjectRequest(AWS_S3_BUCKET_NAME, key, object);
            ObjectMetadata objectMetadata = new ObjectMetadata();

            objectMetadata.setSSEAlgorithm(ObjectMetadata.AES_256_SERVER_SIDE_ENCRYPTION);
            putObjectRequest.setMetadata(objectMetadata);

            PutObjectResult putObjectResult = getS3().putObject(putObjectRequest);

            // TODO: Log
            // System.out.println("Uploaded: " + object.getName() + " | encryption status: " + putObjectResult.getSSEAlgorithm());

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

    public String generatePreSignedObjectUrl(String objectKey, long expirationDateInMillis) {
        Date expirationDate = new Date();

        expirationDate.setTime(
                expirationDate.getTime() + expirationDateInMillis
        );

        try {
            GeneratePresignedUrlRequest generatePresignedUrlRequest =
                    new GeneratePresignedUrlRequest(AWS_S3_BUCKET_NAME, objectKey)
                            .withMethod(HttpMethod.GET)
                            .withExpiration(expirationDate);

            URL url = getS3().generatePresignedUrl(generatePresignedUrlRequest);

            return url.toString();
        } catch (AmazonServiceException e) {
            //TODO: Log
            e.printStackTrace();

            return "";
        }
    }

    private static AmazonS3 getS3() {
        return S3;
    }
}
