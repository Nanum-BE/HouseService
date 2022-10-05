package com.nanum.houseservice.review.domain;

import com.nanum.config.BaseTimeEntity;
import com.nanum.houseservice.room.domain.Room;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.util.List;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@SQLDelete(sql = "update review set delete_at=now() where id=?")
@Where(clause = "delete_at is null")
public class Review extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false)
    Room room;

    @Column(nullable = false)
    private Long userId;

    @Column(nullable = false)
    private String nickname;

    @Comment("리뷰 점수 - 1~5점")
    @Column(nullable = false)
    private int score;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;

    @OneToMany(mappedBy = "review", cascade = CascadeType.REMOVE)
    private List<ReviewImg> reviewImg;
}
