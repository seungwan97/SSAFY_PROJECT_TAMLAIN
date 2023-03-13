package com.ssafy.db.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class JejuData {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(updatable = false, nullable = false, columnDefinition = "INT UNSIGNED")
    private Long id;

    @Column(length = 150)
    private String name;

    private Double latitude;
    private Double longitude;

    @Column(name = "road_address")
    private String roadAddress;

    @Column(name = "place_url")
    private String placeUrl;

    @Column(name = "img_url")
    private String imgUrl;

    @Column(name = "phone_number", length = 13)
    private String phoneNumber;

    @Column(name = "sum_rating",columnDefinition = "INT UNSIGNED")
    private Long sumRating;

    @Column(name = "total_rating",columnDefinition = "INT UNSIGNED")
    private Long totalRating;

    @Enumerated(EnumType.STRING)
    private Category category;

}
