package com.iipsen2.app.services.Amazon;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Encryption;
import com.amazonaws.services.s3.model.S3Object;

import java.io.File;

public interface S3ServiceMethods {
    S3Object getS3Object(String key);

    boolean putS3Object(String key, File object);

    boolean deleteS3Object(String key);
}
