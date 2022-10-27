package com.nanum.houseservice.house.infrastructure;

import com.nanum.houseservice.house.domain.HouseDocument;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface HouseSearchRepository extends ElasticsearchRepository<HouseDocument, Long> {
    Long countAllBy();
}