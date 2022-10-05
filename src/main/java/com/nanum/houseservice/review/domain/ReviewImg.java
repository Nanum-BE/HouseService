package com.nanum.houseservice.review.domain;

import com.nanum.config.BaseTimeEntity;
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
@SQLDelete(sql = "update review_img set delete_at=now() where id=?")
@Where(clause = "delete_at is null")
public class ReviewImg extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false)
    Review review;

    private String originName;
    private String saveName;
    private String imgPath;
}
