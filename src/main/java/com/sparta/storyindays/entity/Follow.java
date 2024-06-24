package com.sparta.storyindays.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "follow")
public class Follow {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    long id;

    @Column(name = "follow_check", nullable = false)
    boolean isFollow;

    @Column(name = "follow_user_id",nullable = false)
    String followUserId;

    @ManyToOne
    @JoinColumn(name = "followee_user_id")
    User followeeUser;

    public Follow(boolean isFollow, String followUserId, User followeeUser) {
        this.isFollow = isFollow;
        this.followUserId = followUserId;
        this.followeeUser = followeeUser;
    }

    public void changeFollow(boolean isFollow){
        this.isFollow = isFollow;
    }
}
