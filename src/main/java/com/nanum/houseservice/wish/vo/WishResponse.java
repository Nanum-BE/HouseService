package com.nanum.houseservice.wish.vo;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class WishResponse {
    private Long wishId;
    private Long houseId;
    private String lotAddress;
    private String houseName;
    private String mainHouseImgPath;
    private Long userId;
}
