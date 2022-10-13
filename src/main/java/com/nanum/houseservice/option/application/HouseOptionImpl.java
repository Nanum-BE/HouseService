package com.nanum.houseservice.option.application;

import com.nanum.houseservice.option.domain.HouseOption;
import com.nanum.houseservice.option.infrastructure.HouseOptionRepository;
import com.nanum.houseservice.option.vo.HouseOptionCheckResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class HouseOptionImpl implements HouseOptionService {

    private final HouseOptionRepository houseOptionRepository;

    @Override
    public List<HouseOptionCheckResponse> retrieveOptions() {

        List<HouseOption> houseOptions = houseOptionRepository.findAll();
        List<HouseOptionCheckResponse> responses = new ArrayList<>();

        houseOptions.forEach(houseOption -> responses.add(
            HouseOptionCheckResponse.builder()
                    .houseOptionId(houseOption.getId())
                    .optionName(houseOption.getOptionName())
                    .isChecked(false)
                    .iconPath(houseOption.getIconPath())
                    .build()
        ));
        return responses;
    }
}
