package com.nanum.util.s3;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.PutObjectRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
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

    public List<S3UploadDto> upload(List<MultipartFile> multipartFile, String dirName) throws Exception {
        try {
            List<File> uploadFile = new ArrayList<>();
            List<String> originName = new ArrayList<>();

            multipartFile.forEach(m -> {
                try {
                    uploadFile.add(convert(m));
                    originName.add(m.getOriginalFilename());
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            });

            return upload(uploadFile, bucket, dirName, originName);

        } catch (Exception e) {
            throw new Exception();
        }
    }

    // S3로 파일 업로드하기
    private List<S3UploadDto> upload(List<File> uploadFile, String bucket, String dirName, List<String> originName) {

        List<S3UploadDto> s3UploadDtoList = new ArrayList<>();
        for(int i = 0; i < uploadFile.size(); i++) {
            String imgUrl = putS3(uploadFile.get(i), bucket, dirName + "/" + uploadFile.get(i).getName());
            s3UploadDtoList.add(S3UploadDto.builder()
                    .originName(originName.get(i))
                    .saveName(dirName + "/" + uploadFile.get(i).getName())
                    .imgUrl(imgUrl)
                    .build());
        }
        removeNewFile(uploadFile);

        return s3UploadDtoList;
    }

    private String putS3(File uploadFile, String bucket, String fileName) {
        amazonS3Client.putObject(new PutObjectRequest(bucket, fileName, uploadFile).withCannedAcl(CannedAccessControlList.PublicRead));
        return amazonS3Client.getUrl(bucket, fileName).toString();
    }

    // 로컬에 저장된 이미지 지우기
    private void removeNewFile(List<File> targetFile) {
        for (File f : targetFile) {
            if(!f.delete()) {
                log.info("File delete fail -> " + f.getName());
            }
        }
        log.info("Files delete success");
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
