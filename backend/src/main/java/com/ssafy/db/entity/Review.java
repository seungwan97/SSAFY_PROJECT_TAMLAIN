package com.ssafy.db.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Review {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(updatable = false, nullable = false, columnDefinition = "INT UNSIGNED")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "schedule_id")
    private ScheduleList scheduleList;

    @ManyToOne
    @JoinColumn(name = "jeju_data_id")
    private JejuData jejuData;

    @Column(columnDefinition = "INT UNSIGNED")
    private Long rating;

    @Column(name = "created_date")
    private LocalDateTime createdDate;
}
