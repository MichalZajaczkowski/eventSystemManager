package com.example.eventsystemmanager.dto;

import com.example.eventsystemmanager.entity.User;
import com.example.eventsystemmanager.entity.UserAddress;
import lombok.*;


import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;


@Getter
@Setter
@RequiredArgsConstructor
@Builder
public class UserDto {

    @NotBlank
    private Long id;
    @NotBlank
    private UserAddressDto userAddress;
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

    public User toUser() {
        return new User(
                id,
                userAddress.toUserAddress(),
                userName,
                userSurName,
                login,
                password,
                email,
                phone

        );
    }
    public void setUserAddressToDto(UserAddress userAddress) {
        this.userAddress = new UserAddressDto(userAddress);
    }
}