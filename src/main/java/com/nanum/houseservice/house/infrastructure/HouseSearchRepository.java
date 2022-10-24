package com.nanum.houseservice.house.infrastructure;

import com.nanum.houseservice.house.domain.HouseDocument;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.List;

public interface HouseSearchRepository extends ElasticsearchRepository<HouseDocument, Long> {
    List<HouseDocument> findByHouseName(String houseName);
}