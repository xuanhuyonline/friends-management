package com.friends.management.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Getter
@Setter
@NoArgsConstructor
public class SubscriptionRequest {
    @Email(message = "Invalid email format")
    @NotBlank(message = "Email cannot be empty")
    private String requestor;

    @Email(message = "Invalid email format")
    @NotBlank(message = "Email cannot be empty")
    private String target;
}
