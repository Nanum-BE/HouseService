package com.nanum.houseservice.room.domain;

import com.nanum.config.BaseTimeEntity;
import com.nanum.config.Gender;
import com.nanum.config.RoomStatus;
import com.nanum.houseservice.house.domain.House;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@SQLDelete(sql = "update room set delete_at=now() where id=?                                                                                                                                                           ")
@Where(clause = "delete_at is null")
public class Room extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false)
    private House house;

    @Comment("방별 이용 가능한 성별 -> 남성, 여성, 공용")
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Gender roomGender;

    @Comment("방 면적")
    @Column(nullable = false)
    private String area;

    @Comment("방 이름 또는 방 호수")
    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private int totalMember;

    @Comment("보증금")
    @Column(nullable = false)
    private int deposit;

    @Comment("월 임대료")
    @Column(nullable = false)
    private int monthlyRent;

    @Comment("관리비")
    @Column(nullable = false)
    private int maintenanceFee;

    @Comment("선불공과금")
    @Column(nullable = false)
    private int prepaidUtilityBill;

    @Comment("계약 종료일")
    private LocalDateTime contractEndAt;

    @Comment("방 상태 -> 대기 중, 진행 중, 입주 완료, 정비 중")
    @Enumerated(EnumType.STRING)
    private RoomStatus status;

    private String mainRoomImgPath;
    private String mainRoomImgOriginName;
    private String mainRoomImgSaveName;
}
