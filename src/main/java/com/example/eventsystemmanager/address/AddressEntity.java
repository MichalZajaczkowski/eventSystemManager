package com.example.eventsystemmanager.address;

import com.example.eventsystemmanager.address.addressType.AddressType;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@Getter
@Setter
@ToString
@Builder
@Entity
@Table(name = "addresses")
public class AddressEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true, name = "id")
    private Long id;

    @Column(name = "country")
    private String country;

    @Column(name = "city")
    private String city;

    @Column(name = "street")
    private String street;

    @Column(name = "building_number")
    private Integer buildingNumber;

    @Column(name = "local_number")
    private Integer localNumber;

    @Column(name = "post_code")
    private Integer postCode;
    @Enumerated(EnumType.STRING)
    @Column(name = "address_type")
    private AddressType addressType;

    @Column(name = "created_date")
    private LocalDateTime createdDate;

    @Column(name = "modified_date")
    private LocalDateTime modifiedDate;

    public void updateFieldsFromDto(AddressDto dto) {
        if (dto.getCountry() != null) {
            this.setCountry(dto.getCountry());
        }
        if (dto.getCity() != null) {
            this.setCity(dto.getCity());
        }
        if (dto.getStreet() != null) {
            this.setStreet(dto.getStreet());
        }
        if (dto.getBuildingNumber() != null) {
            this.setBuildingNumber(dto.getBuildingNumber());
        }
        if (dto.getLocalNumber() != null) {
            this.setLocalNumber(dto.getLocalNumber());
        }
        if (dto.getPostCode() != null) {
            this.setPostCode(dto.getPostCode());
        }
    }

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
