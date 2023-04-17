package com.example.eventsystemmanager.user;

import com.example.eventsystemmanager.user.userAddress.UserAddressDto;
import com.example.eventsystemmanager.user.userAddress.UserAddressEntity;
import com.example.eventsystemmanager.user.userStatus.UserStatus;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;


import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
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
    private UserStatus userStatus;


    public UserEntity toUser() {
        return new UserEntity(
                id,
                userAddress.toUserAddress(),
                userName,
                userSurname,
                login,
                password,
                email,
                phone,
                UserStatus.NIEAKTYWNY
        );
    }
    public void setUserAddressToDto(UserAddressEntity userAddressEntity) {
        this.userAddress = new UserAddressDto(userAddressEntity);
    }

    public void setUserAddressToDtoId(Long userAddressEntityId) {
        this.userAddress = new UserAddressDto(userAddressEntityId);
    }
}