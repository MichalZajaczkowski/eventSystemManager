package com.example.eventsystemmanager.place;

import com.example.eventsystemmanager.user.userAddress.UserAddressEntity;
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
    @JoinColumn(name = "user_address_id")
    private UserAddressEntity userAddressEntity;

    @NotBlank
    @Size(max = 255)
    private String name;

    @NotBlank
    @Size(max = 50)
    private String shortName;

    @NotBlank
    @Size(max = 1000)
    private String description;

}
