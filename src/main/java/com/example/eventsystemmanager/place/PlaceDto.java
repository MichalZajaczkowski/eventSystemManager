package com.example.eventsystemmanager.place;

//import com.example.eventsystemmanager.place.placeStatus.PlaceStatus;

import com.example.eventsystemmanager.address.AddressDto;
import com.example.eventsystemmanager.address.AddressEntity;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;

@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@Getter
@Setter
@ToString
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PlaceDto {

    @NotBlank
    private Long id;
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


    public PlaceDto(PlaceEntity placeEntity) {
        this.id = placeEntity.getId();
        this.name = placeEntity.getName();
        this.shortName = placeEntity.getShortName();
        this.description = placeEntity.getDescription();
        this.quantityAvailablePlaces = placeEntity.getQuantityAvailablePlaces();
    }

    public void setPlaceAddressToDto(AddressEntity addressEntity) {
        this.placeAddress = new AddressDto(addressEntity);
    }


//    public PlaceEntity toPlaceEntity() {
//        return new PlaceEntity(
//                id,
//                placeAddress.toAddressEntity(),
//                name,
//                shortName,
//                description,
//                quantityAvailablePlaces
//        );
//    }


    public PlaceEntity toPlaceEntity() {
        PlaceEntity placeEntity = new PlaceEntity();
        placeEntity.setId(this.id);
        placeEntity.setName(this.name);
        placeEntity.setShortName(this.shortName);
        placeEntity.setDescription(this.description);
        placeEntity.setQuantityAvailablePlaces(this.quantityAvailablePlaces);
        if (this.placeAddress != null) {
            placeEntity.setPlaceAddressEntity(this.placeAddress.toAddressEntity());
        }
        return placeEntity;
    }
}
