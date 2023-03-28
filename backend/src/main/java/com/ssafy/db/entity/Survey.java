package com.ssafy.db.entity;

import lombok.*;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Survey extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    private LocalDate startDate;
    private LocalDate endDate;
    private char gender;
    private int ageRange;
    private int travelMemberCode;
    private boolean isCar;
    private int travelThemeCode;
    @Column(length = 4)
    private String season;
    private boolean isDelete;

    @Builder
    public Survey(User user, LocalDate startDate, LocalDate endDate, char gender, int ageRange, int travelMemberCode, boolean isCar, int travelThemeCode, String season, boolean isDelete) {
        this.user = user;
        this.startDate = startDate;
        this.endDate = endDate;
        this.gender = gender;
        this.ageRange = ageRange;
        this.travelMemberCode = travelMemberCode;
        this.isCar = isCar;
        this.travelThemeCode = travelThemeCode;
        this.season = season;
        this.isDelete = isDelete;
    }

    public static Survey of(Survey survey) {
        survey.setDelete(true);
        return survey;
    }
}
