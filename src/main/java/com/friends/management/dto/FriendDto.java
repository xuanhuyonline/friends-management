package com.friends.management.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
@NoArgsConstructor
public class FriendDto {
    private Long id;
    private Long userId;
    private Long friendId;
    private FriendStatus status;
}
