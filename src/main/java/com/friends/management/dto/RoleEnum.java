package com.friends.management.dto;

public enum RoleEnum {
    ROLE_USER,
    ROLE_ADMIN,
    ROLE_MODERATOR;

    public String getRole() {
        return name();
    }
}
