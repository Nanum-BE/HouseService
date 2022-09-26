package com.nanum.houseservice.house.domain;

import com.nanum.houseservice.config.BaseTimeEntity;
import com.nanum.houseservice.config.Gender;
import com.nanum.houseservice.config.HouseStatus;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor
public class House extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long houseId;

    @Column(nullable = false)
    private Long hostId;

    @Comment("도로명 주소")
    @Column(nullable = false)
    private String streetAddress;

    @Comment("지번 주소")
    @Column(nullable = false)
    private String lotAddress;

    @Column(nullable = false)
    private String zipCode;

    @Comment("하우스 소개")
    @Column(nullable = false)
    private String explanation;

    @Column(nullable = false)
    private String houseName;

    @Comment("건물 형태 ex)빌라, 원룸형 등")
    @Column(nullable = false)
    private String houseType;

    @Comment("위도")
    @Column(nullable = false)
    private String lat;

    @Comment("경도")
    @Column(nullable = false)
    private String lon;

    @Comment("하우스 성별 -> 남성, 여성, 공용")
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Gender houseGender;

    private String mainHouseImgPath;

    @Comment("도면 이미지 경로")
    private String floorPlanPath;

    @Comment("검색 키워드")
    private String keyWord;

    @Comment("하우스 상태 -> 승인 전, 승인 완료")
    @Enumerated(EnumType.STRING)
    private HouseStatus status;
}