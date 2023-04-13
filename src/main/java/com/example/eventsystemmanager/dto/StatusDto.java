package com.example.eventsystemmanager.dto;

import com.example.eventsystemmanager.enums.StatusType;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class StatusDto {
    private String name;
    private Integer value;
    private String description;
    private StatusType statusType;
}
