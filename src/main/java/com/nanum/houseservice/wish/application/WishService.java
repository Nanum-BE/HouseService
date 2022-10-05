package com.nanum.houseservice.wish.application;

import com.nanum.houseservice.wish.dto.WishDto;
import com.nanum.houseservice.wish.vo.WishResponse;

import java.util.List;

public interface WishService {
    void createWish(WishDto wishDto);
    void deleteWish(Long userId, Long wishId);
    List<WishResponse> retrieveWish(Long userId);
}
