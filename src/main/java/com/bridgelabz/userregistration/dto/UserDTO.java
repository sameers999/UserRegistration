package com.bridgelabz.userregistration.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@AllArgsConstructor
@NoArgsConstructor
public @Data class UserDTO {
    @Email(message = "Insert valid email")
    private String email;
    @NotBlank(message = "Please enter your password")
    private String password;
    private String confirmPassword;
    @Pattern(regexp = "^[A-Z]{1}[a-zA-Z\\s]{2,}$", message = "User firstName is Not valid. given first letter in upper case")
    private String firstName;
    @Pattern(regexp = "[A-Za-z\\s]+", message = " User lastname is invalid!")
    private String lastName;
    @Pattern(regexp = "^\\d{2}-\\d{2}-\\d{4}$", message = "date of birth must be in YYYY-MM-DD format")
    private String dateOfBirth;
    @Pattern(regexp = "[A-Za-z\\s]+", message = "First letter of City should be in upperCase")
    private String city;
}



//    Email as username
//     Password
//    Confirm Password
//    First Name
//    Last Name
//    DOB and City