package com.example.eventsystemmanager.place;

//import com.example.eventsystemmanager.place.placeStatus.PlaceStatus;
import com.example.eventsystemmanager.address.AddressDto;
import com.example.eventsystemmanager.address.AddressEntity;
import com.example.eventsystemmanager.address.addressType.AddressType;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@Getter
@Setter
@ToString
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PlaceDto {

    @NotBlank
    private Long id;
    @NotNull
    @Valid
    private AddressDto placeAddress;
    @NotBlank
    private String name;
    @NotBlank
    private String shortName;
    @NotBlank
    private String description;
    private Integer quantityAvailablePlaces;
//    private PlaceStatus placeStatus;

    public void setPlaceAddressToDto(AddressEntity addressEntity) {
        this.placeAddress = new AddressDto(addressEntity);
    }

    public PlaceEntity toPlaceEntity() {
        return new PlaceEntity(
                id,
                placeAddress.toAddress(),
                name,
                shortName,
                description,
                quantityAvailablePlaces
        );
    }
}
