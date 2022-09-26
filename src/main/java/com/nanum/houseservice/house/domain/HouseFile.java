package com.nanum.houseservice.house.domain;

import com.nanum.config.BaseTimeEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor
public class HouseFile extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long houseFileId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "houseId", nullable = false)
    private House house;

    @Column(nullable = false)
    private String originName;

    @Column(nullable = false)
    private String saveName;

    @Column(nullable = false)
    private String filePath;
}
