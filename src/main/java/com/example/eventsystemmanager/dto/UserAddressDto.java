package com.example.eventsystemmanager.dto;

import com.example.eventsystemmanager.entity.UserAddress;
import lombok.*;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@RequiredArgsConstructor
@Builder
public class UserAddressDto {

    @NotBlank
    private Long id;
    @NotBlank
    private String country;
    @NotBlank
    private String city;
    @NotBlank
    private String street;
    @NotBlank
    private Integer buildingNumber;
    @NotBlank
    private Integer localNumber;
    @NotBlank
    private Integer postCode;

    public UserAddressDto(UserAddress userAddress) {
        this.id = userAddress.getId();
        this.country = userAddress.getCountry();
        this.city = userAddress.getCity();
        this.street = userAddress.getStreet();
        this.buildingNumber = userAddress.getBuildingNumber();
        this.localNumber = userAddress.getLocalNumber();
        this.postCode = userAddress.getPostCode();
    }

    public UserAddress toUserAddress() {
        return new UserAddress(
                id,
                country,
                city,
                street,
                buildingNumber,
                localNumber,
                postCode
        );
    }
}
