package com.nanum.util.s3;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.nanum.util.s3.S3UploadDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

@Service
@Slf4j
public class S3UploaderService {

    private final AmazonS3Client amazonS3Client;

    @Value("${cloud.aws.s3.bucket}")
    public String bucket;

    @Autowired
    public S3UploaderService(AmazonS3Client amazonS3Client) {
        this.amazonS3Client = amazonS3Client;
    }

    public S3UploadDto upload(MultipartFile multipartFile, String dirName) throws Exception {

        try {
            File uploadFile = convert(multipartFile);
            return upload(uploadFile, bucket, dirName);

        } catch (Exception e) {
            throw new Exception();
        }
    }

    // S3로 파일 업로드하기
    private S3UploadDto upload(File uploadFile, String bucket, String dirName) {
        String originName = uploadFile.getName();
        String fileName = dirName + "/" + UUID.randomUUID() + uploadFile.getName(); // S3에 저장된 파일 이름
        String uploadImageUrl = putS3(uploadFile, bucket, fileName); // S3로 업로드
        removeNewFile(uploadFile);

        return S3UploadDto.builder()
                .originName(originName)
                .saveName(fileName)
                .imgUrl(uploadImageUrl)
                .build();
    }

    private String putS3(File uploadFile, String bucket, String fileName) {
        amazonS3Client.putObject(new PutObjectRequest(bucket, fileName, uploadFile).withCannedAcl(CannedAccessControlList.PublicRead));
        return amazonS3Client.getUrl(bucket, fileName).toString();
    }

    // 로컬에 저장된 이미지 지우기
    private void removeNewFile(File targetFile) {
        if (targetFile.delete()) {
            log.info("File delete success");
            return;
        }
        log.info("File delete fail");
    }

    private File convert(MultipartFile multipartFile) throws Exception {

        String originalFilename = multipartFile.getOriginalFilename();
        String storeFileName = createStoreFileName(originalFilename);

        //파일 업로드
        File file = new File(System.getProperty("user.dir") + storeFileName);

        try {
            multipartFile.transferTo(file);
        } catch (IOException exception) {
            throw new Exception();
        }

        return file;
    }

    private String createStoreFileName(String originalFilename) {
        String ext = extractExt(originalFilename);
        String uuid = UUID.randomUUID().toString();
        return uuid + "." + ext;
    }

    private String extractExt(String originalFilename) {
        int pos = originalFilename.lastIndexOf(".");
        return originalFilename.substring(pos + 1);
    }
}
