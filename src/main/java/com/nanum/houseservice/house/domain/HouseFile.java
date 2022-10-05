package com.nanum.houseservice.house.domain;

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
@SQLDelete(sql = "update house_file set delete_at=now() where id=?")
@Where(clause = "delete_at is null")
public class HouseFile extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false)
    private House house;

    @Column(nullable = false)
    private String originName;

    @Column(nullable = false)
    private String saveName;

    @Column(nullable = false)
    private String filePath;
}
