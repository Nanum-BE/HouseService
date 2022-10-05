package com.nanum.houseservice.wish.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WishDto {
    private Long id;
    private Long houseId;
    private Long userId;
}
