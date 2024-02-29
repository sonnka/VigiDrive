package com.VigiDrive.service.impl;

import com.VigiDrive.exceptions.AmazonException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.PutObjectRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Date;

@Service
public class AmazonClient {

    @Autowired
    private AmazonS3 s3client;

    @Value("${amazonProperties.endpointUrl}")
    private String endpointUrl;

    @Value("${amazonProperties.bucketName}")
    private String bucketName;

    public String uploadFile(MultipartFile multipartFile) throws AmazonException {
        String fileUrl = "";

        try {
            File file = convertMultiPartToFile(multipartFile);
            String fileName = generateFileName(multipartFile);

            fileUrl = endpointUrl + "/" + bucketName + "/" + fileName;

            uploadFileTos3bucket(fileName, file);

            file.delete();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            throw new AmazonException(AmazonException.AmazonExceptionProfile.SOMETHING_WRONG);
        }
        return fileUrl;
    }

    public void deleteFileFromS3Bucket(String fileUrl) {
        String fileName = fileUrl.substring(fileUrl.lastIndexOf("/") + 1);

        s3client.deleteObject(new DeleteObjectRequest(bucketName, fileName));
    }

    private File convertMultiPartToFile(MultipartFile file) throws AmazonException {
        String location = "/tmp/images/";

        File convFile = new File(location + file.getOriginalFilename());

        try {
            FileOutputStream fos = new FileOutputStream(convFile);
            fos.write(file.getBytes());
            fos.close();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            throw new AmazonException(AmazonException.AmazonExceptionProfile.SOMETHING_WRONG);
        }

        return convFile;
    }

    private String generateFileName(MultipartFile multiPart) {
        return new Date().getTime() + "-" + multiPart.getOriginalFilename().replace(" ", "_");
    }

    private void uploadFileTos3bucket(String fileName, File file) {
        s3client.putObject(new PutObjectRequest(bucketName, fileName, file)
                .withCannedAcl(CannedAccessControlList.PublicRead));
    }
}