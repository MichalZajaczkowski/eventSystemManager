package com.example.eventsystemmanager.place;

import com.example.eventsystemmanager.address.AddressEntity;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@Getter
@Setter
@ToString
@Builder
@Entity
@JsonInclude(JsonInclude.Include.NON_NULL)
@Table(name = "places")
public class PlaceEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true, name = "id")
    private Long id;


    @OneToOne
    @JoinColumn(name = "address_id")
    private AddressEntity placeAddressEntity;


    @Size(max = 255)
    private String name;


    @Size(max = 50)
    private String shortName;


    @Size(min = 10,max = 1000, message = "Enter a description")
    private String description;

    private Integer quantityAvailablePlaces;

    @Column(name = "created_date")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdDate;

    @Column(name = "modified_date")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime modifiedDate;

    @PrePersist
    public void onCreate() {
        createdDate = LocalDateTime.now();
        modifiedDate = LocalDateTime.now();
    }

    @PreUpdate
    public void onUpdate() {
        modifiedDate = LocalDateTime.now();
    }
}