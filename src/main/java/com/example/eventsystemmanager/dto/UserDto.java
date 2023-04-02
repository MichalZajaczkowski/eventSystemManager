package com.example.eventsystemmanager.dto;

import com.example.eventsystemmanager.entity.UserEntity;
import com.example.eventsystemmanager.entity.UserAddressEntity;
import lombok.*;


import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDto {

    @NotBlank
    private Long id;
    @NotBlank
    private UserAddressDto userAddress;
    @NotBlank
    private String userName;
    @NotBlank
    private String userSurname;
    @NotBlank
    private String login;
    @NotBlank
    private String password;
    @Email
    private String email;
    private String phone;

    public UserEntity toUser() {
        return new UserEntity(
                id,
                userAddress.toUserAddress(),
                userName,
                userSurname,
                login,
                password,
                email,
                phone

        );
    }
    public void setUserAddressToDto(UserAddressEntity userAddressEntity) {
        this.userAddress = new UserAddressDto(userAddressEntity);
    }
}
