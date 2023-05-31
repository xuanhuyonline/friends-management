package com.friends.management.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class FriendRequestDto {
    private List<String> friends;
}
