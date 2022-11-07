package com.nanum.config;

import com.amazonaws.services.ec2.model.Route;
import com.nanum.houseservice.house.dto.PopularHouseDto;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.NumberExpression;
import com.querydsl.jpa.impl.JPAQuery;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

import static com.nanum.houseservice.house.domain.QHouse.house;

@RequiredArgsConstructor
@Repository
public class HouseRepositoryCustom {
    private final EntityManager entityManger;

    public List<PopularHouseDto> findByHouseInfo(int count) {

        JPAQuery<Route> query = new JPAQuery<>(entityManger, MySQLJPATemplates.DEFAULT);

        return query
                .select(Projections.bean(PopularHouseDto.class,
                        house.id,
                        house.mainHouseImgPath,
                        house.houseName,
                        house.houseGender,
                        house.streetAddress
                        ))
                .from(house)
                .orderBy(NumberExpression.random().asc())
                .limit(count)
                .fetch();

    }
}
