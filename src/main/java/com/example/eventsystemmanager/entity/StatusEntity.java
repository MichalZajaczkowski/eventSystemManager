package com.example.eventsystemmanager.entity;

import com.example.eventsystemmanager.enums.StatusType;
import lombok.*;

import javax.persistence.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "status")
@Entity
public class StatusEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true, nullable = false, name = "id")
    private Long id;

    @Column(nullable = false)
    private String name;
    @Column(nullable = false)
    private Integer value;
    @Column(nullable = false)
    private String description;
    @Column(nullable = false)
    private StatusType statusType;
}
