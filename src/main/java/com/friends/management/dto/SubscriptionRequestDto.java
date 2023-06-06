package com.friends.management.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@AllArgsConstructor
public class SubscriptionRequestDto {
    @NotBlank(message = "Email is required")
    private String requestor;

    @NotBlank(message = "Email is required")
    private String target;
}
