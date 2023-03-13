package com.ssafy.db.entity;

import jdk.jfr.Unsigned;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor

public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(updatable = false, nullable = false, columnDefinition = "INT UNSIGNED")
    private Long id;

    @Column(name = "nick_name", length = 64)
    private String nickName;

    @Column(name = "refresh_token", length = 64)
    private String refreshToken;

    @Column(name = "created_date")
    private LocalDateTime createdDate;
}
