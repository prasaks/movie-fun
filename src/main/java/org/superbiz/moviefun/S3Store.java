package org.superbiz.moviefun;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.S3Object;
import org.superbiz.moviefun.blobstore.Blob;
import org.superbiz.moviefun.blobstore.BlobStore;

import java.io.IOException;
import java.util.Optional;

public class S3Store implements BlobStore {

    private AmazonS3Client s3Client;
    private String photoStorageBucket;


    public S3Store(AmazonS3Client s3Client, String photoStorageBucket) {
        this.s3Client = s3Client;
        this.photoStorageBucket = photoStorageBucket;
    }

    @Override
    public void put(Blob blob) throws IOException {
        System.out.println("Inside S3B put");
        ObjectMetadata objectMetadata = new ObjectMetadata();

        objectMetadata.setContentType("Image/JPEG");

        s3Client.putObject(photoStorageBucket, blob.name, blob.inputStream, objectMetadata);
    }

    @Override
    public Optional<Blob> get(String name) throws IOException {

        S3Object s3 = s3Client.getObject(photoStorageBucket, name);

        Blob blob = new Blob(s3.getBucketName(), s3.getObjectContent(), s3.getObjectMetadata().getContentType());

        return Optional.of(blob);

    }

    @Override
    public void deleteAll() {

        s3Client.deleteBucket(photoStorageBucket);

    }
}
