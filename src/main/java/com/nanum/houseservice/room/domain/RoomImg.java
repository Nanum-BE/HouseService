package com.nanum.houseservice.room.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor
public class RoomImg {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false)
    private Room room;

    private String originName;

    private String saveName;

    private String houseImgPath;

    @Comment("우선 순위(정렬 기준)")
    private int priority;
}
