package com.friends.management.entity;


import com.friends.management.dto.FriendStatus;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Table(name = "friends")
@Entity
public class Friend {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "userId")
    private User user;

    @ManyToOne
    @JoinColumn(name = "friendId")
    private User friend;

    @Enumerated(EnumType.STRING)
    private FriendStatus status;


}
