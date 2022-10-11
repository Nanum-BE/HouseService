package com.nanum.houseservice.house.domain;

import com.nanum.config.BaseTimeEntity;
import com.nanum.config.Gender;
import com.nanum.config.HouseStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLDeleteAll;
import org.hibernate.annotations.Where;

import javax.persistence.*;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@SQLDelete(sql = "update house set delete_at=now() where id=?")
@Where(clause = "delete_at is null")
public class House extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long hostId;

    @Comment("도로명 주소")
    @Column(nullable = false)
    private String streetAddress;

    @Comment("지번 주소")
    @Column(nullable = false)
    private String lotAddress;

    @Comment("상세 주소")
    @Column(nullable = false)
    private String detailAddress;

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
    private String mainHouseImgOriginName;
    private String mainHouseImgSaveName;

    @Comment("도면 이미지 경로")
    private String floorPlanPath;
    private String floorPlanOriginName;
    private String floorPlanSaveName;

    @Comment("검색 키워드")
    private String keyWord;

    @Comment("하우스 상태 -> 승인 전, 승인 완료")
    @Enumerated(EnumType.STRING)
    private HouseStatus status;
}
