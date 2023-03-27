package com.ssafy.db.entity;

import com.sun.istack.NotNull;
import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor
public class JejuPlace {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category category;

    @Column(length = 150)
    private String name;
    private Double latitude;
    private Double longitude;
    private String roadAddress;
    private String placeUrl;
    private String imgUrl;
    private int reviewScoreSum;
    private int reviewCount;
    @Column(length = 150)
    private String tag;

    @Builder
    public JejuPlace(int reviewScoreSum, int reviewCount) {
        this.reviewScoreSum = reviewScoreSum;
        this.reviewCount = reviewCount;
    }
}
