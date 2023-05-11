package com.example.eventsystemmanager.user;

import com.example.eventsystemmanager.address.AddressDto;
import com.example.eventsystemmanager.address.AddressEntity;
import com.example.eventsystemmanager.address.addressType.AddressType;
import com.example.eventsystemmanager.user.userStatus.UserStatus;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;


import javax.validation.Valid;
import javax.validation.constraints.*;
import java.time.LocalDateTime;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserDto {

    private Long id;

    @Valid
    @NotNull(message = "User address cannot be null")
    private AddressDto userAddress;

    @NotBlank(message = "Username cannot be blank")
    private String userName;

    @NotBlank(message = "User surname cannot be blank")
    private String userSurname;

    @NotBlank(message = "Login cannot be blank")
    private String login;

    @NotBlank(message = "Password cannot be blank")
    @Size(min = 8, message = "Password must be at least 8 characters long")
    private String password;

    @Email(message = "Invalid email format")
    @NotBlank(message = "Email cannot be blank")
    private String email;

    @Pattern(regexp = "^\\+(?:[0-9] ?){6,14}[0-9]$", message = "Invalid phone number format")
    private String phone;

    private UserStatus userStatus;
    private AddressType addressType;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdDate;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime modifiedDate;

    public UserDto(Long id, String userName, String userSurname) {
        this.id = id;
        this.userName = userName;
        this.userSurname = userSurname;
    }

    public UserDto(long l, long l1, String john, String doe, String mail, String number, UserStatus userStatus) {

    }


    public UserEntity toUserEntity() {
        return new UserEntity(
                id,
                userAddress.toAddressEntity(),
                userName,
                userSurname,
                login,
                password,
                email,
                phone,
                UserStatus.NIEAKTYWNY,
                createdDate,
                modifiedDate
        );
    }

    public static UserDto fromUserEntity(UserEntity userEntity) {
        UserDto userDto = new UserDto();
        userDto.setId(userEntity.getId());
        userDto.setUserAddress(AddressDto.fromAddressEntity(userEntity.getAddressEntity()));
        userDto.setUserName(userEntity.getUserName());
        userDto.setUserSurname(userEntity.getUserSurname());
        userDto.setLogin(userEntity.getLogin());
        userDto.setPassword(userEntity.getPassword());
        userDto.setEmail(userEntity.getEmail());
        userDto.setPhone(userEntity.getPhone());
        userDto.setUserStatus(userEntity.getUserStatus());
        userDto.setCreatedDate(userEntity.getCreatedDate());
        userDto.setModifiedDate(userEntity.getModifiedDate());

        return userDto;
    }

    public void setUserAddressToDto(AddressEntity addressEntity) {
        this.userAddress = new AddressDto(addressEntity);
    }

    public void setUserAddressToDtoId(Long userAddressEntityId) {
        this.userAddress = new AddressDto(userAddressEntityId);
    }
}