package com.nanum.houseservice.house.infrastructure;

import com.nanum.houseservice.house.domain.HouseDocument;
import com.nanum.houseservice.house.dto.HouseSearchDto;
import com.nanum.houseservice.house.vo.HouseElasticSearchResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.GeoDistanceQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.functionscore.FunctionScoreQueryBuilder;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Collectors;

import static org.elasticsearch.index.query.QueryBuilders.boolQuery;

@Repository
@RequiredArgsConstructor
@Slf4j
public class HouseSearchQueryRepository {

    private final ElasticsearchOperations operations;

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

    public List<HouseElasticSearchResponse> findByByElastic(String searchWord) {

        BoolQueryBuilder boolQueryBuilder = new BoolQueryBuilder();

        if(!searchWord.equals("")) {
            boolQueryBuilder.should(QueryBuilders.wildcardQuery("lotAddress", "*" + searchWord + "*"))
                            .should(QueryBuilders.wildcardQuery("streetAddress", "*" + searchWord + "*"))
                            .should(QueryBuilders.wildcardQuery("keyWord", "*" + searchWord + "*"))
                            .minimumShouldMatch(1);
        }

        FunctionScoreQueryBuilder functionScoreQueryBuilder =
                QueryBuilders.functionScoreQuery(boolQueryBuilder);

        NativeSearchQueryBuilder queryBuilder = new NativeSearchQueryBuilder();
        queryBuilder.withQuery(functionScoreQueryBuilder);

        SearchHits<HouseDocument> articles = operations
                .search(queryBuilder.build(), HouseDocument.class, IndexCoordinates.of("house"));

        return articles.stream()
                .map(HouseElasticSearchResponse::from)
                .collect(Collectors.toList());
    }

    public List<HouseElasticSearchResponse> findByByRegion(HouseSearchDto houseSearchDto) {

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
        searchQuery.withQuery(distanceQueryBuilder).withFilter(boolQueryBuilder);

        SearchHits<HouseDocument> articles = operations.search(searchQuery.build(), HouseDocument.class, IndexCoordinates.of("house"));

        return articles.stream()
                .map(HouseElasticSearchResponse::from)
                .collect(Collectors.toList());
    }
}