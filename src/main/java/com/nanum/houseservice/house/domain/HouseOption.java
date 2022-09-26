package com.nanum.houseservice.house.domain;

import com.nanum.config.BaseTimeEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor
public class HouseOption extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long houseOptionId;

    @Column(nullable = false)
    private String optionName;

    @Column(nullable = false)
    private String iconPath;
}
