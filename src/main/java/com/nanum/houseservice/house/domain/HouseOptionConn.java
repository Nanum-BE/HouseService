package com.nanum.houseservice.house.domain;

import com.nanum.houseservice.option.domain.HouseOption;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
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
