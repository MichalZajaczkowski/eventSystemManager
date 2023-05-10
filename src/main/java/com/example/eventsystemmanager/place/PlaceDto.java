package com.example.eventsystemmanager.place;

//import com.example.eventsystemmanager.place.placeStatus.PlaceStatus;

import com.example.eventsystemmanager.address.AddressDto;
import com.example.eventsystemmanager.address.AddressEntity;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import javax.validation.Valid;
import javax.validation.constraints.*;
import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@Getter
@Setter
@ToString
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PlaceDto {

    @Positive(message = "Place ID must be a positive number")
    private Long id;


    @Valid
    private AddressDto placeAddress;

    @Size(min = 2, max = 50, message = "Place name must be between 2 and 50 characters long")
    private String name;

    @Size(min = 2, max = 20, message = "Place short name must be between 2 and 20 characters long")
    private String shortName;

    @Size(min = 10, max = 500, message = "Place description must be between 10 and 500 characters long")
    private String description;

    @NotNull(message = "Available places quantity must be specified")
    @Min(value = 0, message = "Available places quantity must be a non-negative number")
    @Max(value = 10000, message = "Available places quantity must be less than or equal to 10000")
    private Integer quantityAvailablePlaces;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdDate;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime modifiedDate;

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

