package com.nanum.houseservice.room.domain;

import com.nanum.houseservice.config.BaseTimeEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor
public class RoomOption extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long roomOptionId;

    @Column(nullable = false)
    private String optionName;

    @Column(nullable = false)
    private String iconPath;
}
