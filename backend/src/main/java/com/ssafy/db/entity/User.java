package com.ssafy.db.entity;

import jdk.jfr.Unsigned;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.util.Lazy;

import javax.persistence.*;
import java.time.LocalDateTime;

import static javax.persistence.CascadeType.ALL;
import static javax.persistence.EnumType.STRING;
import static javax.persistence.FetchType.LAZY;
import static javax.persistence.GenerationType.IDENTITY;

@Entity
@Getter
@NoArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    private int id;

    private String email;

    @OneToOne(fetch = LAZY, cascade = ALL, orphanRemoval = true)
    @JoinColumn(name = "user_profile_id")
    private UserProfile userProfile;

    @Enumerated(STRING)
    private Role role;

    private LocalDateTime createdDate;


    @Builder
    public User(int id, String email, Role role, LocalDateTime createdDate) {
        this.id = id;
        this.email = email;
        this.role = role;
        this.createdDate = createdDate;
    }

    public static User createUser(String email, String nickName,LocalDateTime createdDate, String provider, String providerId) {

        UserProfile profile = UserProfile.createProfile(nickName, provider, providerId);

        User user = User.builder()
                .email(email)
                .role(Role.USER)
                .createdDate(createdDate)
                .build();

        user.addUserProfile(profile);

        return user;
    }


    public void addUserProfile(UserProfile userProfile){
        this.userProfile = userProfile;
        userProfile.setUser(this);
    }

}
