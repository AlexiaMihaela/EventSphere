package org.example.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public class UpdateUserRequest {

    @NotBlank(message = "fullName is required")
    public String fullName;

    @Email(message = "email must be valid")
    @NotBlank(message = "email is required")
    public String email;
}
