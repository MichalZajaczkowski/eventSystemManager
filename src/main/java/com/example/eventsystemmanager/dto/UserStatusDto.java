package com.example.eventsystemmanager.dto;

import com.example.eventsystemmanager.enums.UserStatus;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class UserStatusDto {
    private String name;
    private Integer value;
    private String description;
    private UserStatus userStatus;
}
