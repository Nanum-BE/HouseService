package com.nanum.houseservice.room.domain;

import com.nanum.houseservice.option.domain.RoomOption;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor
public class RoomOptionConn {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long roomOptionConnId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "roomId", nullable = false)
    private Room room;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "roomOptionId", nullable = false)
    private RoomOption roomOption;
}
