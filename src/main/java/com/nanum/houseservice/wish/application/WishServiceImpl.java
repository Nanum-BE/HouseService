package com.nanum.houseservice.wish.application;

import com.nanum.houseservice.wish.infrastructure.WishRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class WishServiceImpl implements WishService{
    private final WishRepository wishRepository;


}
