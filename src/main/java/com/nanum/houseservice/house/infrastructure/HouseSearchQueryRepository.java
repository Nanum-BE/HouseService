package com.nanum.houseservice.house.infrastructure;

import com.nanum.houseservice.house.domain.HouseDocument;
import com.nanum.houseservice.house.dto.HouseSearchDto;
import lombok.RequiredArgsConstructor;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.query.Criteria;
import org.springframework.data.elasticsearch.core.query.CriteriaQuery;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class HouseSearchQueryRepository {
    private final ElasticsearchOperations operations;

    public List<HouseDocument> findByCondition(HouseSearchDto houseSearchDto) {
        CriteriaQuery query = createConditionCriteriaQuery(houseSearchDto);

        SearchHits<HouseDocument> search = operations.search(query, HouseDocument.class);
        return search.stream()
                .map(SearchHit::getContent)
                .collect(Collectors.toList());
    }

    public List<String> findBySearchWord(String searchWord) {
        CriteriaQuery query = new CriteriaQuery(new Criteria());
        query.addCriteria(Criteria.where("houseName.jaso").matches(searchWord));

        SearchHits<HouseDocument> search = operations.search(query, HouseDocument.class);
        return search.stream()
                .map(SearchHit -> SearchHit.getContent().getHouseName())
                .collect(Collectors.toList());
    }

    private CriteriaQuery createConditionCriteriaQuery(HouseSearchDto houseSearchDto) {
        CriteriaQuery query = new CriteriaQuery(new Criteria());

        if (houseSearchDto == null)
            return query;

        if (StringUtils.hasText(houseSearchDto.getHouseName()))
            query.addCriteria(Criteria.where("houseName.jaso").matches(houseSearchDto.getHouseName())
                    .or(Criteria.where("houseName.ngram")).matches(houseSearchDto.getStreetAddress()));
//                    .or(Criteria.where("houseName.ngram").matches(houseSearchDto.getStreetAddress())));

        QueryBuilders.boolQuery()
                .should(QueryBuilders.matchQuery("houseName.jaso", houseSearchDto.getHouseName()))
                .should(QueryBuilders.matchQuery("houseName.jaso", houseSearchDto.getHouseName()));

        if (StringUtils.hasText(houseSearchDto.getStreetAddress()))
            query.addCriteria(Criteria.where("houseName.ngram").matches(houseSearchDto.getStreetAddress()));

//        if (StringUtils.hasText(houseSearchDto.getHouseName()))
//            query.addCriteria(Criteria
//                    .where("houseName")
//                    .contains(houseSearchDto.getHouseName()));
//
//        if (StringUtils.hasText(houseSearchDto.getStreetAddress()))
//            query.addCriteria(Criteria.where("streetAddress").contains(houseSearchDto.getStreetAddress()));
//
//        if (StringUtils.hasText(houseSearchDto.getZipCode()))
//            query.addCriteria(Criteria.where("zipCode").is(houseSearchDto.getZipCode()));
//
//        if(houseSearchDto.getHouseGender() != null)
//            query.addCriteria(Criteria.where("status").is(houseSearchDto.getHouseGender()));

        return query;
    }
}