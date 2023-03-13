package com.example.eventsystemmanager.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {
    @NotBlank
    private String userName;
    @NotBlank
    private String userSurName;
    @NotBlank
    private String login;
    @NotBlank
    private String password;
    @Email
    private String email;
    private String phone;
}
