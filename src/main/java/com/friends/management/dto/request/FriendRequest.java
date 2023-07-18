package com.friends.management.dto.request;

import lombok.Getter;
import lombok.Setter;
import java.util.List;

@Getter
@Setter
public class FriendRequest {
    private List<String> friends;
}
