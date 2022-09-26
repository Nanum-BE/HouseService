package com.nanum.houseservice.house.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor
public class HouseImg {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long houseImgId;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "houseId", nullable = false)
    private House house;

    @Column(nullable = false)
    private String originName;

    @Column(nullable = false)
    private String saveName;

    @Column(nullable = false)
    private String imgPath;
}
