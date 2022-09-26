package com.nanum.util.s3;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class S3UploadDto {

    private String originName;
    private String saveName;
    private String imgUrl;
}
