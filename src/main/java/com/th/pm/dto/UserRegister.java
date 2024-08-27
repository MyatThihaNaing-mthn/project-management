package com.th.pm.dto;

import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class UserRegister {
    @NotBlank(message = "first name is mandatory")
    private String firstname;
    @NotBlank(message = "last name is mandatory")
    private String lastname;
    @NotBlank(message = "email is mandatory")
    @Email
    private String email;
    @NotBlank(message = "password name is mandatory")
    private String password;
    private MultipartFile profileImage;
}
