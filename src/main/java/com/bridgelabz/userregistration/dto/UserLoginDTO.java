package com.bridgelabz.userregistration.dto;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

public @Data class UserLoginDTO {
    @Email
    private String email;
    @NotEmpty(message = "Password cant be null")
    private String password;

    public UserLoginDTO(String email, String password) {
        this.email = email;
        this.password = password;
    }

}
