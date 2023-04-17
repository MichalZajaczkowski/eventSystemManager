package com.example.eventsystemmanager.organizer;

import com.example.eventsystemmanager.organizer.OrganizerDto;
import com.example.eventsystemmanager.organizer.OrganizerEntity;
import com.example.eventsystemmanager.exception.OrganizerNotFoundException;
import com.example.eventsystemmanager.organizer.OrganizerMapper;
import com.example.eventsystemmanager.organizer.OrganizerRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.persistence.NonUniqueResultException;
import java.util.List;

import static org.springframework.data.jpa.domain.AbstractPersistable_.id;

@RequiredArgsConstructor
@Slf4j
@Service
public class OrganizerService {
    private static final String ORGANIZERWITHIDDOESNOTEXIST = "Organizer with id does not exist.";
    private static final String ORGANIZERWITHIDALREADYEXIST = "Organizer with id already exists";
    private static final String ORGANIZERWITHNAMEDOESNOTEXIST = "Organizer with name does not exist.";
    private static final String ORGANIZERWITHNAMEDOEALREADYEXIST = "Organizer with name already exists";
    private static final String ORGANIZERFOUND = "Organizer found ";
    private static final String ORGANIZERNOTFOUND = "Organizer not found ";

    private final OrganizerRepository organizerRepository;
    private final OrganizerMapper organizerMapper;

    public List<OrganizerDto> findAll() {
        if (organizerRepository.findAll().isEmpty()) {
            throw new IllegalArgumentException(ORGANIZERWITHIDDOESNOTEXIST);
        } else {
            return organizerRepository.findAll()
                    .stream()
                    .map(this::toDto)
                    .toList();
        }
    }

    public OrganizerDto findById(Long id) {
        return organizerRepository.findById(id)
                .map(organizerMapper::organizerMapToDto)
                .orElseThrow(() -> new IllegalArgumentException(ORGANIZERNOTFOUND));
    }


    public OrganizerDto findByName(String name) {
        if (organizerRepository.findByName(name).isPresent()) {
            log.info(ORGANIZERFOUND);
        } else {
            log.info(ORGANIZERNOTFOUND);
        }
        return organizerRepository.findByName(name)
                .map(organizerMapper::organizerMapToDto)
                .orElseThrow(() -> new OrganizerNotFoundException(ORGANIZERNOTFOUND));
    }

    private OrganizerDto toDto(OrganizerEntity organizerEntity) {
        return OrganizerDto.builder()
                .id(organizerEntity.getId())
                .name(organizerEntity.getName())
                .description(organizerEntity.getDescription())
                .build();
    }

    public void createOrganizer(OrganizerDto organizerDto) {
        if (organizerRepository.findById(id).isPresent()) {
            log.info("Log: " + ORGANIZERWITHIDALREADYEXIST);
            throw new IllegalStateException(ORGANIZERWITHIDALREADYEXIST);
        }
        if (organizerRepository.findByName(organizerDto.getName()).isPresent()) {
            log.info("Log: " + ORGANIZERWITHNAMEDOEALREADYEXIST);
            throw new IllegalStateException(ORGANIZERWITHNAMEDOEALREADYEXIST);
        }
        organizerRepository.save(organizerDto.toOrganizer());
    }

    public void updateOrganizer(OrganizerDto organizerDto) {
        organizerRepository.findById(organizerDto.getId())
                .orElseThrow(() -> new IllegalArgumentException(ORGANIZERWITHIDDOESNOTEXIST));
        log.info("LOG: " + ORGANIZERWITHIDDOESNOTEXIST);
        if (organizerRepository.findByName(organizerDto.getName()).isPresent()) {
            log.info(ORGANIZERWITHNAMEDOEALREADYEXIST);
            throw new IllegalArgumentException(ORGANIZERWITHNAMEDOEALREADYEXIST);
        } else if (organizerDto.getName() != null) {
            organizerDto.setName(organizerDto.getName());
        }
        if (organizerDto.getDescription() != null) {
            organizerDto.setDescription(organizerDto.getDescription());
        }
        organizerRepository.save(organizerDto.toOrganizer());
    }

    public void partialUpdate(OrganizerDto organizerDto) {
        OrganizerEntity organizer = organizerRepository.findById(organizerDto.getId())
                .orElseThrow(() -> new IllegalArgumentException("Organizer with id " + organizerDto.getId() + " does not exist"));
        if (organizerRepository.findByName(organizerDto.getName()).isPresent()) {
            throw new NonUniqueResultException("Organizer with name '" + organizerDto.getName() + "' already exists");
        } else if (organizerDto.getName() != null) {
            organizer.setName(organizerDto.getName());
        }
        if (organizerDto.getDescription() != null) {
            organizer.setDescription(organizerDto.getDescription());
        }
        organizerRepository.save(organizer);
    }
}
