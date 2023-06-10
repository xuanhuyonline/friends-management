package com.friends.management.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.util.List;

@Getter
@Setter
public class FriendRequest {

    @NotNull(message = "Friends are required")
    private List<String> friends;
}
