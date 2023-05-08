package com.example.eventsystemmanager.AdditionalEndpoints;

import com.example.eventsystemmanager.user.UserDto;

import java.util.List;

public class AddressUsersDto {

    private Long addressId;
    private String street;
    private String city;
    private Integer zipCode;
    private int userCount;
    private List<UserDto> users;

    public AddressUsersDto(Long addressId, String street, String city, Integer zipCode, int userCount, List<UserDto> users) {
        this.addressId = addressId;
        this.street = street;
        this.city = city;
        this.zipCode = zipCode;
        this.userCount = userCount;
        this.users = users;
    }

    public Long getAddressId() {
        return addressId;
    }

    public void setAddressId(Long addressId) {
        this.addressId = addressId;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public Integer getZipCode() {
        return zipCode;
    }

    public void setZipCode(Integer zipCode) {
        this.zipCode = zipCode;
    }

    public int getUserCount() {
        return userCount;
    }

    public void setUserCount(int userCount) {
        this.userCount = userCount;
    }

    public List<UserDto> getUsers() {
        return users;
    }

    public void setUsers(List<UserDto> users) {
        this.users = users;
    }
}

