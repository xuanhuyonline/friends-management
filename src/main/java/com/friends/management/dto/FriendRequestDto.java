package com.friends.management.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.util.List;

@Getter
@Setter
public class FriendRequestDto {

    @NotNull(message = "friends are required")
    private List<String> friends;
}
