package com.example.eventsystemmanager.dto;

import com.example.eventsystemmanager.entity.OrganizerEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class OrganizerDto {

    @NotBlank
    @Schema(description = "Organizer id", example = "1")
    private Long id;
    @NotBlank
    @Schema(description = "Organizer name", example = "chPtYSzy", required = true)
    private String name;
    @NotBlank
    @Schema(description = "Organizer description", example = "KopYTkO", required = true)
    private String description;

    public OrganizerEntity toOrganizer() {
        OrganizerEntity organizer = new OrganizerEntity();
        organizer.setId(id);
        organizer.setName(name);
        organizer.setDescription(description);
        return organizer;
    }
}
