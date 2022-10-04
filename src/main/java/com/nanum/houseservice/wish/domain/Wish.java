package com.nanum.houseservice.wish.domain;

import com.nanum.config.BaseTimeEntity;
import com.nanum.houseservice.house.domain.House;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.*;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@SQLDelete(sql = "update wish_id set delete_at=now() where id=?")
@Where(clause = "delete_at is null")
public class Wish extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false)
    private House house;

    @Column(nullable = false)
    private Long userId;
}
