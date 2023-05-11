package com.example.eventsystemmanager.user;

import com.example.eventsystemmanager.address.AddressEntity;
import com.example.eventsystemmanager.address.addressType.AddressType;
import com.example.eventsystemmanager.user.userStatus.UserStatus;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

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

    @Column(name = "created_date")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdDate;

    @Column(name = "modified_date")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime modifiedDate;

    public UserEntity(String userName, String userSurname) {
        this.userName = userName;
        this.userSurname = userSurname;
        // set other fields to default values
    }

    public Long getUserAddressEntityId() {
        return addressEntity.getId();
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
