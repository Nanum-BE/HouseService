package com.nanum.houseservice.house.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor
public class HouseOptionConn {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long houseOptionConnId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "houseId", nullable = false)
    private House house;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "houseOptionId", nullable = false)
    private HouseOption houseOption;
}
