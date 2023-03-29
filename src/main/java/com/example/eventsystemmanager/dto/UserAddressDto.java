package com.example.eventsystemmanager.dto;

import com.example.eventsystemmanager.entity.UserAddressEntity;
import lombok.*;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
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

    public UserAddressDto(UserAddressEntity userAddressEntity) {
        this.id = userAddressEntity.getId();
        this.country = userAddressEntity.getCountry();
        this.city = userAddressEntity.getCity();
        this.street = userAddressEntity.getStreet();
        this.buildingNumber = userAddressEntity.getBuildingNumber();
        this.localNumber = userAddressEntity.getLocalNumber();
        this.postCode = userAddressEntity.getPostCode();
    }

    public UserAddressEntity toUserAddress() {
        return new UserAddressEntity(
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
