package com.nanum.houseservice.room.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor
public class RoomImg {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long roomImgId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "roomId")
    private Room room;

    private String originName;

    private String saveName;

    private String houseImgPath;
}
