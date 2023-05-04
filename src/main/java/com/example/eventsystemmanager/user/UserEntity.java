package com.example.eventsystemmanager.user;

import com.example.eventsystemmanager.address.AddressEntity;
import com.example.eventsystemmanager.address.addressType.AddressType;
import com.example.eventsystemmanager.user.userStatus.UserStatus;
import lombok.*;

import javax.persistence.*;

@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@Getter
@Setter
@ToString
@Entity
@Table(name = "users")
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true, name = "id")
    private Long id;

    @OneToOne
    @JoinColumn(name = "address_id")
    private AddressEntity addressEntity;

    @Column(name = "user_name")
    private String userName;
    @Column(name = "user_surname")
    private String userSurname;

    @Column(name = "login")
    private String login;

    @Column(name = "password")
    private String password;

    @Column(name = "email")
    private String email;

    @Column(name = "phone")
    private String phone;
    @Enumerated(EnumType.STRING)
    private UserStatus userStatus;

//    @Enumerated(EnumType.STRING)
//    private AddressType addressType;

    public Long getUserAddressEntityId() {
        return addressEntity.getId();
    }
}
// TODO: 29.04.2023 add date of creat and mod user