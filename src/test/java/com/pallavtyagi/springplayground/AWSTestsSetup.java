package com.pallavtyagi.springplayground;


import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.S3ClientOptions;
import com.amazonaws.services.s3.model.CreateBucketRequest;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;

import static com.pallavtyagi.springplayground.AWSTestConstants.DEFAULT_REGION;
import static com.pallavtyagi.springplayground.AWSTestConstants.S3_ENDPOINT;

public class AWSTestsSetup {

    public static String S3_BUCKET = "test-datalake-bucket1";

    private static AmazonS3 getS3Client() {
        AmazonS3 s3Client = new AmazonS3Client();
        s3Client.setRegion(Region.getRegion(Regions.US_WEST_2));
        s3Client.setEndpoint(S3_ENDPOINT);
        s3Client.setS3ClientOptions(S3ClientOptions.builder().setPathStyleAccess(true).disableChunkedEncoding().build());
        return s3Client;
    }

    @BeforeAll
    public static void cleanTestData() {
        AmazonS3 s3Client = getS3Client();
        if(s3Client.doesBucketExistV2(S3_BUCKET)) {
            s3Client.deleteObject(S3_BUCKET, "app1/test-app1.json");
            s3Client.deleteObject(S3_BUCKET, "app2/test-app2.json");
            s3Client.deleteBucket(S3_BUCKET);
        }
    }

    @Test
    public void createS3Bucket() {
        AmazonS3 s3Client = getS3Client();
        CreateBucketRequest request = new CreateBucketRequest(S3_BUCKET, DEFAULT_REGION);
        s3Client.createBucket(request);
        assert  s3Client.doesBucketExistV2(S3_BUCKET);
    }

    @Test
    public void setupApplicationFolders() throws IOException {
        AmazonS3 s3Client = getS3Client();
        File file1 = new File(getClass().getClassLoader().getResource("app1/test-app1.json").getFile());
        s3Client.putObject(S3_BUCKET,"app1/test-app1.json", file1);

        File file2 = new File(getClass().getClassLoader().getResource("app2/test-app2.json").getFile());
        s3Client.putObject(S3_BUCKET,"app2/test-app2.json", file2);


    }


}
