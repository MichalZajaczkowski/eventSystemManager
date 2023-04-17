package com.example.eventsystemmanager.organizer;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@Slf4j
@RequestMapping("/organizer")
public class OrganizerController {
    private static final String ORGANIZERWITHIDDOESNOTEXIST = "Organizer with id does not exist.";
    private static final String ORGANIZERWITHIDALREADYEXIST = "Organizer with id already exists";
    private static final String ORGANIZERWITHNAMEDOESNOTEXIST = "Organizer with name does not exist.";
    private static final String ORGANIZERWITHNAMEDOEALREADYEXIST = "Organizer with name already exists";
    private static final String ORGANIZERFOUND = "Organizer found: ";
    private static final String ORGANIZERNOTFOUND = "Organizer not found ";
    private final OrganizerService organizerService;

    @Operation(summary = "Gets all organizer", description = "Gets list of all organizer")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful response",
                    content = @Content(schema = @Schema(implementation = OrganizerDto[].class))),
            @ApiResponse(responseCode = "500", description = "Organizer cannot be found")
    })
    @GetMapping("/all")
    public ResponseEntity<List<OrganizerDto>> getAll() {
        List<OrganizerDto> organizerDtoList = organizerService.findAll();
        if (organizerDtoList.isEmpty()) {
            log.error(ORGANIZERNOTFOUND);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else {
            log.info("{}", ORGANIZERFOUND + organizerDtoList);
            return ResponseEntity.ok(organizerDtoList);
//            return new ResponseEntity<>(HttpStatus.OK);
        }
    }

    @Operation(summary = "Gets all organizer", description = "Gets list of all organizer")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful response",
                    content = @Content(schema = @Schema(implementation = OrganizerDto[].class))),
            @ApiResponse(responseCode = "404", description = "Organizer cannot be found")
    })
    @GetMapping("/{id}")
    public ResponseEntity<OrganizerDto> getOrganizationById(@PathVariable Long id) {
        OrganizerDto organizer = organizerService.findById(id);
        if (organizer != null) {
            log.info("{}", ORGANIZERFOUND + organizer);
            return ResponseEntity.ok().body(organizer);

        } else {
            log.error(ORGANIZERNOTFOUND);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/name/{name}")
    public ResponseEntity<String> getOrganizationByName(@PathVariable String name) {
        OrganizerDto organizerDto = organizerService.findByName(name);
        if (organizerDto != null) {
            log.info("{}", ORGANIZERFOUND + organizerDto.getName());
            return ResponseEntity.ok().body(organizerDto.getName());

        } else {
            log.error(ORGANIZERNOTFOUND);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping
    public ResponseEntity<OrganizerDto> createOrganizer(@RequestBody OrganizerDto organizerDto) {
        organizerService.createOrganizer(organizerDto);
        log.info("Organizer created");
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PutMapping
    public ResponseEntity<OrganizerDto> updateOrganizer(@RequestBody OrganizerDto organizerDto) {
        organizerService.updateOrganizer(organizerDto);
        log.info("Log: Update organizer was successfully");
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PatchMapping
    public ResponseEntity<OrganizerDto> partialUpdateOrganizer(@RequestBody OrganizerDto organizerDto) {
        organizerService.partialUpdate(organizerDto);
        log.info("Log: Update organizer was successfully");
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
