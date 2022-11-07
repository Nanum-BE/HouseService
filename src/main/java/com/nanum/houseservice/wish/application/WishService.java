package com.nanum.houseservice.wish.application;

import com.nanum.houseservice.wish.dto.WishDto;
import com.nanum.houseservice.wish.vo.WishCountResponse;
import com.nanum.houseservice.wish.vo.WishIdResponse;
import com.nanum.houseservice.wish.vo.WishResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface WishService {
    WishIdResponse createWish(WishDto wishDto);
    void deleteWish(Long userId, Long wishId);
    Page<WishResponse> retrieveWish(Long userId, Pageable pageable);
    WishCountResponse retrieveWishCount(Long userId);
}
