package com.nanum.houseservice.wish.domain;

import com.nanum.config.BaseTimeEntity;
import com.nanum.houseservice.house.domain.House;
import com.nanum.houseservice.house.dto.HouseSearch;
import com.nanum.houseservice.wish.dto.WishDto;
import io.micrometer.core.instrument.search.Search;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.*;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@SQLDelete(sql = "update wish set delete_at=now() where id=?")
@Where(clause = "delete_at is null")
public class Wish extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false)
    private House house;

    @Column(nullable = false)
    private Long userId;

    public WishDto entityToWishDto(HouseSearch houseTotal) {
        return WishDto.builder()
                .wishId(id)
                .houseId(house.getId())
                .houseName(house.getHouseName())
                .lotAddress(house.getLotAddress())
                .mainHouseImgPath(house.getMainHouseImgPath())
                .houseGender(house.getHouseGender())
                .houseType(house.getHouseType())
                .userId(userId)
                .minMonthlyRent(houseTotal.getMinMonthlyRent() == null ? 0 : houseTotal.getMinMonthlyRent())
                .maxMonthlyRent(houseTotal.getMaxMonthlyRent() == null ? 0 : houseTotal.getMaxMonthlyRent())
                .wishCount(houseTotal.getWishCount())
                .reviewCount(houseTotal.getReviewCount())
                .reviewAvg(houseTotal.getReviewAvg() == null ? 0 : houseTotal.getReviewAvg())
                .build();
    }
}
