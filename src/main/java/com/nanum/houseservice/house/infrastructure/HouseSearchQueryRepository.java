package com.nanum.houseservice.house.infrastructure;

import com.google.common.collect.Lists;
import com.nanum.houseservice.house.domain.HouseDocument;
import com.nanum.houseservice.house.dto.HouseSearchDto;
import com.nanum.houseservice.house.vo.HouseCountResponse;
import com.nanum.houseservice.house.vo.HouseElasticSearchResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.GeoDistanceQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.elasticsearch.index.query.QueryBuilders.boolQuery;

@Repository
@RequiredArgsConstructor
@Slf4j
public class HouseSearchQueryRepository {

    private final ElasticsearchOperations operations;
    private final HouseSearchRepository houseSearchRepository;

    public List<String> findBySearchWord(String searchWord) {

        BoolQueryBuilder boolQueryBuilder = boolQuery()
                .should(QueryBuilders.matchQuery("lotAddress.jaso", searchWord))
                .should(QueryBuilders.matchQuery("lotAddress.ngram", searchWord))
                .should(QueryBuilders.matchQuery("streetAddress.jaso", searchWord))
                .should(QueryBuilders.matchQuery("streetAddress.ngram", searchWord))
                .minimumShouldMatch(1);

        NativeSearchQueryBuilder queryBuilder = new NativeSearchQueryBuilder();
        queryBuilder.withQuery(boolQueryBuilder);

        SearchHits<HouseDocument> articles = operations
                .search(queryBuilder.build(), HouseDocument.class, IndexCoordinates.of("house"));

        return articles.stream()
                .map(SearchHit -> SearchHit.getContent().getHouseName())
                .collect(Collectors.toList());
    }

    public List<HouseElasticSearchResponse> findByElastic(String searchWord) {

        BoolQueryBuilder boolQueryBuilder = new BoolQueryBuilder();

        if(!searchWord.equals("")) {
            boolQueryBuilder.should(QueryBuilders.wildcardQuery("lotAddress", "*" + searchWord + "*"))
                            .should(QueryBuilders.wildcardQuery("streetAddress", "*" + searchWord + "*"))
                            .should(QueryBuilders.wildcardQuery("keyWord", "*" + searchWord + "*"))
                            .minimumShouldMatch(1);
        }

        NativeSearchQueryBuilder queryBuilder = new NativeSearchQueryBuilder();
        queryBuilder.withQuery(boolQueryBuilder).withPageable(PageRequest.of(0, 10000));

        SearchHits<HouseDocument> articles = operations
                .search(queryBuilder.build(), HouseDocument.class, IndexCoordinates.of("house"));

        return articles.stream()
                .map(HouseElasticSearchResponse::from)
                .collect(Collectors.toList());
    }

    public List<HouseElasticSearchResponse> findByOption(HouseSearchDto houseSearchDto) {

        // 중심 좌표를 기준으로 지정한 Distance(m단위)에 포함되는 결과 조회 쿼리
        GeoDistanceQueryBuilder distanceQueryBuilder = QueryBuilders
                .geoDistanceQuery("location")
                .point(houseSearchDto.getCenterY(), houseSearchDto.getCenterX())
                .distance(String.valueOf(houseSearchDto.getDistance()));

        // 검색어 및 필터를 포함하는 결과 조회 쿼리
        BoolQueryBuilder boolQueryBuilder = new BoolQueryBuilder();

        if(!houseSearchDto.getSearchWord().equals("")) {
            boolQueryBuilder.should(QueryBuilders.wildcardQuery("lotAddress", "*" + houseSearchDto.getSearchWord() + "*"))
                            .should(QueryBuilders.wildcardQuery("streetAddress", "*" + houseSearchDto.getSearchWord() + "*"))
                            .should(QueryBuilders.wildcardQuery("keyWord", "*" + houseSearchDto.getSearchWord() + "*"))
                            .minimumShouldMatch(1);
        }
        if(!houseSearchDto.getGenderType().equals("")) {
            boolQueryBuilder.must(QueryBuilders.termQuery("houseGender.keyword", houseSearchDto.getGenderType()));
        }
        if(!houseSearchDto.getHouseType().equals("")) {
            boolQueryBuilder.must(QueryBuilders.termQuery("houseType.keyword", houseSearchDto.getHouseType()));
        }

        NativeSearchQueryBuilder searchQuery = new NativeSearchQueryBuilder();
        searchQuery.withQuery(distanceQueryBuilder).withFilter(boolQueryBuilder).withPageable(PageRequest.of(0, 10000));

        SearchHits<HouseDocument> articles = operations.search(searchQuery.build(), HouseDocument.class, IndexCoordinates.of("house"));

        return articles.stream()
                .map(HouseElasticSearchResponse::from)
                .collect(Collectors.toList());
    }

    public List<HouseElasticSearchResponse> findByRegion(String region) {
        BoolQueryBuilder boolQueryBuilder = new BoolQueryBuilder();

        if(region.equals("전국")) {
            List<HouseDocument> houseDocuments = Lists.newArrayList(houseSearchRepository.findAll());

            return houseDocuments.stream()
                    .map(HouseElasticSearchResponse::fromDoc)
                    .collect(Collectors.toList());
        }

        if(!region.equals("")) {
            boolQueryBuilder.must(QueryBuilders.wildcardQuery("streetAddress", "*" + region + "*"))
                    .must(QueryBuilders.wildcardQuery("lotAddress", "*" + region + "*"));
        }

        NativeSearchQueryBuilder queryBuilder = new NativeSearchQueryBuilder();
        queryBuilder.withQuery(boolQueryBuilder).withPageable(PageRequest.of(0, 10000));

        SearchHits<HouseDocument> articles = operations
                .search(queryBuilder.build(), HouseDocument.class, IndexCoordinates.of("house"));

        return articles.stream()
                .map(HouseElasticSearchResponse::from)
                .collect(Collectors.toList());
    }

    public List<HouseCountResponse> countByRegion() {
        List<HouseCountResponse> houseCountResponses = new ArrayList<>();

        List<String> region = Arrays.asList("전국", "서울", "경기", "인천", "부산", "대구", "대전", "경남", "전남",
                "충남", "광주", "울산", "경북", "전북", "충북", "강원", "제주", "세종");

        for (String s : region) {
            Long houseCount;

            if(s.equals("전국")) {
                houseCount = houseSearchRepository.countAllBy();
            } else {
                BoolQueryBuilder boolQueryBuilder = new BoolQueryBuilder();
                NativeSearchQueryBuilder queryBuilder = new NativeSearchQueryBuilder();

                boolQueryBuilder.must(QueryBuilders.wildcardQuery("streetAddress", "*" + s + "*"))
                        .must(QueryBuilders.wildcardQuery("lotAddress", "*" + s + "*"));
                queryBuilder.withQuery(boolQueryBuilder);

                houseCount = operations.count(queryBuilder.build(), HouseDocument.class, IndexCoordinates.of("house"));
            }

            houseCountResponses.add(HouseCountResponse.builder()
                            .region(s)
                            .houseCount(houseCount)
                            .build());
        }

        return houseCountResponses;
    }
}