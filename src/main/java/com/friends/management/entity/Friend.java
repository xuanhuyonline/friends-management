package com.friends.management.entity;


import com.friends.management.dto.FriendStatus;
import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "friends")
@Entity
@Builder
public class Friend {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Boolean subscriber;

    @ManyToOne
    @JoinColumn(name = "userId")
    private User user;

    @ManyToOne
    @JoinColumn(name = "friendId")
    private User friend;

    @Enumerated(EnumType.STRING)
    private FriendStatus status;
}
