package com.nanum.houseservice.wish.application;

import com.nanum.exception.NotFoundException;
import com.nanum.exception.OverlapException;
import com.nanum.houseservice.house.domain.House;
import com.nanum.houseservice.house.infrastructure.HouseRepository;
import com.nanum.houseservice.wish.domain.Wish;
import com.nanum.houseservice.wish.dto.WishDto;
import com.nanum.houseservice.wish.infrastructure.WishRepository;
import com.nanum.houseservice.wish.vo.WishResponse;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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

    @Override
    public void deleteWish(Long userId, Long wishId) {
        if(wishRepository.existsByIdAndUserId(wishId, userId)) {
            wishRepository.deleteById(wishId);
        } else {
            throw new NotFoundException("해당 좋아요가 존재하지 않습니다.");
        }
    }

    @Override
    public List<WishResponse> retrieveWish(Long userId) {

        List<Wish> wishes = wishRepository.findAllByUserId(userId);
        List<WishDto> wishDtos = new ArrayList<>();

        if(wishes != null) {
            wishes.forEach(wish -> wishDtos.add(wish.entityToWishDto()));
        }

        ModelMapper mapper = new ModelMapper();
        mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

        return Arrays.asList(mapper.map(wishDtos, WishResponse[].class));
    }
}
