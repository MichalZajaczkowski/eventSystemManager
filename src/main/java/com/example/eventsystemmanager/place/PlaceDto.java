package com.example.eventsystemmanager.place;

//import com.example.eventsystemmanager.place.placeStatus.PlaceStatus;
import com.example.eventsystemmanager.user.userAddress.UserAddressDto;
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
    private UserAddressDto userAddress;
    @NotBlank
    private String name;
    @NotBlank
    private String shortName;
    @NotBlank
    private String description;
//    private PlaceStatus placeStatus;

    public PlaceEntity toPlaceEntity() {
        return new PlaceEntity(
                id,
                userAddress.toUserAddress(),
                name,
                shortName,
                description
//                PlaceStatus.IN_PROGRESS
        );
    }
}
