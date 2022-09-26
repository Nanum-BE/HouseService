package com.nanum.houseservice.house.application;

import com.nanum.config.HouseStatus;
import com.nanum.houseservice.house.domain.House;
import com.nanum.houseservice.house.dto.HouseDto;
import com.nanum.houseservice.house.infrastructure.HouseRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class HouseServiceImpl implements HouseService {

    private final HouseRepository houseRepository;

    @Override
    public void createHouse(HouseDto houseDto) {

        houseDto.setStatus(HouseStatus.BEFORE_APPROVAL);

        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

        House house = modelMapper.map(houseDto, House.class);
        houseRepository.save(house);
    }
}
