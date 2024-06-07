package com.itzroma.showme.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.itzroma.showme.domain.FileType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class AmazonS3Service {
    private final AmazonS3 s3Client;

    @Value("${app.aws.s3.path}")
    private String path;

    @Value("${app.aws.s3.bucket-name}")
    private String bucketName;

    public String uploadFile(MultipartFile multipartFile, FileType fileType, String userId) {
        try {
            String fileName = constructFileName(multipartFile, fileType, userId);
            File file = convertMultipartFileToIOFile(multipartFile);
            uploadFileToS3Bucket(fileName, file);
            file.delete();
            return constructFileUrl(fileName);
        } catch (IOException e) {
            log.error("Failed to upload file to S3 bucket: {}", e.getLocalizedMessage());
            throw new RuntimeException("Failed to upload file to S3 bucket: " + e.getLocalizedMessage(), e);
        }
    }

    private String constructFileName(MultipartFile multipartFile, FileType fileType, String userId) {
        String commonFileName = fileType.getFolderName() + "/" + userId;
        if (fileType == FileType.AVATAR) {
            return commonFileName + "." + FilenameUtils.getExtension(multipartFile.getOriginalFilename());
        }
        return commonFileName + "/" + System.currentTimeMillis() + "__" + multipartFile.getOriginalFilename();
    }

    private File convertMultipartFileToIOFile(MultipartFile multipartFile) throws IOException {
        String originalFilename = multipartFile.getOriginalFilename();
        File converted = new File(originalFilename == null ? UUID.randomUUID().toString() : originalFilename);
        FileOutputStream fos = new FileOutputStream(converted);
        fos.write(multipartFile.getBytes());
        fos.close();
        return converted;
    }

    private void uploadFileToS3Bucket(String fileName, File file) {
        if (s3Client.doesObjectExist(bucketName, fileName)) {
            s3Client.deleteObject(new DeleteObjectRequest(bucketName, fileName));
        }
        s3Client.putObject(new PutObjectRequest(bucketName, fileName, file)
                .withCannedAcl(CannedAccessControlList.PublicRead));
    }

    private String constructFileUrl(String fileName) {
        return path + "/" + bucketName + "/" + fileName;
    }
}
