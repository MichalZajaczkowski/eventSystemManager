package com.example.eventsystemmanager.address;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AddressDto {

    @NotBlank
    @JsonIgnore
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

    public AddressDto(AddressEntity addressEntity) {
        this.id = addressEntity.getId();
        this.country = addressEntity.getCountry();
        this.city = addressEntity.getCity();
        this.street = addressEntity.getStreet();
        this.buildingNumber = addressEntity.getBuildingNumber();
        this.localNumber = addressEntity.getLocalNumber();
        this.postCode = addressEntity.getPostCode();
    }

    public AddressDto(Long id) {
        this.id = id;
    }

    public AddressEntity toUserAddress() {
        return new AddressEntity(
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
