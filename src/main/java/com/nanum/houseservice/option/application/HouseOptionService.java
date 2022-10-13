package com.nanum.houseservice.option.application;

import com.nanum.houseservice.option.vo.HouseOptionCheckResponse;

import java.util.List;

public interface HouseOptionService {
    List<HouseOptionCheckResponse> retrieveOptions();
}
