package com.friends.management.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import java.util.List;

@Getter
@Setter
public class FriendRequestDto {

    @NotNull(message = "friends are required")
    //@Email(message = "Email is invalid")
    private List<String> friends;
}
