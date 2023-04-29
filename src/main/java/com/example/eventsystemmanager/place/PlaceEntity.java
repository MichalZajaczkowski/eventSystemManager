package com.example.eventsystemmanager.place;

import com.example.eventsystemmanager.address.AddressEntity;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@Getter
@Setter
@ToString
@Entity
@Table(name = "places")
public class PlaceEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true, name = "id")
    private Long id;

    @NotNull
    @OneToOne
    @JoinColumn(name = "address_id")
    private AddressEntity placeAddressEntity;

    @NotBlank
    @Size(max = 255)
    private String name;

    @NotBlank
    @Size(max = 50)
    private String shortName;

    @NotBlank(message = "Description cannot be null")
    @Size(min = 10,max = 1000, message = "Enter a description")
    private String description;
    private Integer quantityAvailablePlaces;
}
// TODO: 29.04.2023 add date of creat and mod place