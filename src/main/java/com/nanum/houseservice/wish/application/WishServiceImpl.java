package com.nanum.houseservice.wish.application;

import com.nanum.exception.NotFoundException;
import com.nanum.exception.OverlapException;
import com.nanum.houseservice.house.domain.House;
import com.nanum.houseservice.house.infrastructure.HouseRepository;
import com.nanum.houseservice.wish.domain.Wish;
import com.nanum.houseservice.wish.dto.WishDto;
import com.nanum.houseservice.wish.infrastructure.WishRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class WishServiceImpl implements WishService{
    private final WishRepository wishRepository;
    private final HouseRepository houseRepository;

    @Override
    public void createWish(WishDto wishDto) {
        if (wishRepository.existsByUserIdAndHouseId(wishDto.getUserId(), wishDto.getHouseId())) {
            throw new OverlapException("이미 좋아요한 하우스입니다.");
        }

        House house = houseRepository.findById(wishDto.getHouseId()).orElse(null);

        if(house == null) {
            throw new NotFoundException("해당 하우스가 존재하지 않습니다.");
        }

        wishRepository.save(Wish.builder()
                        .house(house)
                        .userId(wishDto.getUserId())
                        .build());
    }
}
