package com.example.eventsystemmanager.address;

import com.example.eventsystemmanager.address.addressType.AddressType;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.*;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AddressDto {

    @JsonIgnore
    private Long id;

    @NotBlank
    @Size(min = 2, max = 100)
    private String country;

    @NotBlank
    @Size(min = 2, max = 100)
    private String city;

    @NotBlank
    @Size(min = 2, max = 100)
    private String street;

    @NotNull
    @Min(1)
    @Max(9999)
    private Integer buildingNumber;

    @NotNull
    @Min(1)
    @Max(9999)
    private Integer localNumber;

    @NotNull
    @Min(1)
    @Max(9999)
    private Integer postCode;

    @Enumerated(EnumType.STRING)
    private AddressType addressType;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdDate;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime modifiedDate;

    public AddressDto(AddressEntity addressEntity) {
        if (addressEntity == null) {
            throw new IllegalArgumentException("AddressEntity cannot be null");
        }
        this.id = addressEntity.getId();
        this.country = addressEntity.getCountry();
        this.city = addressEntity.getCity();
        this.street = addressEntity.getStreet();
        this.buildingNumber = addressEntity.getBuildingNumber();
        this.localNumber = addressEntity.getLocalNumber();
        this.postCode = addressEntity.getPostCode();
        this.addressType = addressEntity.getAddressType();
        this.createdDate = addressEntity.getCreatedDate();
        this.modifiedDate = addressEntity.getModifiedDate();
    }

    public AddressDto(Long id) {
        this.id = id;
    }

    public AddressDto(Long id, String street, String city, String country) {
        this.id = id;
        this.street = street;
        this.city = city;
        this.country = country;
    }

    public AddressEntity toAddressEntity() {
        return new AddressEntity(
                id,
                country,
                city,
                street,
                buildingNumber,
                localNumber,
                postCode,
                addressType,
                createdDate,
                modifiedDate
        );
    }
}
