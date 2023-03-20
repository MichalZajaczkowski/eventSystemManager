package com.example.eventsystemmanager.entity;

import lombok.*;

import javax.persistence.*;

@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@RequiredArgsConstructor
@Getter
@Setter
@ToString
@Entity
@Table(name = "user_address")
public class UserAddress {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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
}
