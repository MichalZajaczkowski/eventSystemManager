package com.example.eventsystemmanager.address;

import com.example.eventsystemmanager.address.addressType.AddressType;
import com.example.eventsystemmanager.exception.AddressCreationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("AddressService should")
class AddressServiceTest {

    @Mock
    private AddressRepository addressRepository;

    @Mock
    private AddressMapper addressMapper;

    @InjectMocks
    private AddressService addressService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        addressMapper =
                new AddressMapperImpl(); // Replace with your actual AddressMapper implementation
        addressService = new AddressService(addressRepository, addressMapper);
    }

    private AddressEntity createAddressEntity(Long id, String country, String city, String street) {
        AddressEntity addressEntity = new AddressEntity();
        addressEntity.setId(id);
        addressEntity.setCountry(country);
        addressEntity.setCity(city);
        addressEntity.setStreet(street);
        return addressEntity;
    }

    private AddressDto createAddressDto(String country, String city, String street) {
        return AddressDto.builder().country(country).city(city).street(street).build();
    }

    private AddressEntity createAddressEntity(Long id) {
        AddressEntity addressEntity = new AddressEntity();
        addressEntity.setId(id);
        return addressEntity;
    }

    @Test
    @DisplayName("Should create an address and set the ID when the input is valid")
    void createAddressWhenInputIsValid() {
        AddressDto addressDto = createAddressDto("Poland", "Krakow", "Main Street");
        AddressEntity addressEntity = createAddressEntity(null, "Poland", "Krakow", "Main Street");

        when(addressRepository.save(any(AddressEntity.class))).thenReturn(addressEntity);

        addressService.createAddress(addressDto);

        assertNotNull(addressDto.getId());
        assertEquals(addressEntity.getId(), addressDto.getId());
    }

    @Test
    @DisplayName("Should throw an AddressCreationException when the address could not be created")
    void createAddressWhenAddressCreationFailsThenThrowException() {
        AddressDto addressDto = createAddressDto("Poland", "Krakow", "Main Street");
        AddressEntity addressEntity = addressDto.toAddressEntity();
        when(addressRepository.save(addressEntity)).thenReturn(null);

        assertThrows(
                AddressCreationException.class,
                () -> addressService.createAddress(addressDto),
                "Address could not be created");

        verify(addressRepository, times(1)).save(addressEntity);
    }

    @Test
    @DisplayName("Should return all addresses as AddressDto objects")
    void findAllReturnsAllAddressesAsAddressDtoObjects() {
        List<AddressEntity> addressEntities = new ArrayList<>();
        AddressEntity addressEntity1 = new AddressEntity();
        addressEntity1.setId(1L);
        addressEntity1.setCountry("Poland");
        addressEntity1.setCity("Krakow");
        addressEntity1.setStreet("Main Street");
        addressEntity1.setBuildingNumber(1);
        addressEntity1.setLocalNumber(1);
        addressEntity1.setPostCode(31_000);
        addressEntity1.setAddressType(AddressType.USER_ADDRESS);
        AddressEntity addressEntity2 = new AddressEntity();
        addressEntity2.setId(2L);
        addressEntity2.setCountry("USA");
        addressEntity2.setCity("New York");
        addressEntity2.setStreet("Broadway");
        addressEntity2.setBuildingNumber(10);
        addressEntity2.setLocalNumber(5);
        addressEntity2.setPostCode(10001);
        addressEntity2.setAddressType(AddressType.PLACE_ADDRESS);

        addressEntities.add(addressEntity1);
        addressEntities.add(addressEntity2);

        when(addressRepository.findAll()).thenReturn(addressEntities);

        List<AddressDto> addressDtos = addressService.findAll();

        assertThat(addressDtos).isNotNull();
        assertThat(addressDtos).hasSize(addressEntities.size());

        for (int i = 0; i < addressEntities.size(); i++) {
            AddressEntity addressEntity = addressEntities.get(i);
            AddressDto addressDto = addressDtos.get(i);

            assertThat(addressDto.getId()).isEqualTo(addressEntity.getId());
            assertThat(addressDto.getCountry()).isEqualTo(addressEntity.getCountry());
            assertThat(addressDto.getCity()).isEqualTo(addressEntity.getCity());
            assertThat(addressDto.getStreet()).isEqualTo(addressEntity.getStreet());
            assertThat(addressDto.getBuildingNumber()).isEqualTo(addressEntity.getBuildingNumber());
            assertThat(addressDto.getLocalNumber()).isEqualTo(addressEntity.getLocalNumber());
            assertThat(addressDto.getPostCode()).isEqualTo(addressEntity.getPostCode());
            assertThat(addressDto.getAddressType()).isEqualTo(addressEntity.getAddressType());
            assertThat(addressDto.getCreatedDate()).isEqualTo(addressEntity.getCreatedDate());
            assertThat(addressDto.getModifiedDate()).isEqualTo(addressEntity.getModifiedDate());
        }

        verify(addressRepository, times(1)).findAll();
    }

    @Test
    public void testFindAll_ShouldReturnAllAddresses() {
        // Given
        AddressEntity address1 = createAddressEntity(1L, "Country 1", "City 1", "Street 1");
        AddressEntity address2 = createAddressEntity(2L, "Country 2", "City 2", "Street 2");
        List<AddressEntity> addressEntities = Arrays.asList(address1, address2);

        when(addressRepository.findAll()).thenReturn(addressEntities);

        // When
        List<AddressDto> addressDtos = addressService.findAll();

        // Then
        assertEquals(2, addressDtos.size());
        assertEquals("Country 1", addressDtos.get(0).getCountry());
        assertEquals("City 1", addressDtos.get(0).getCity());
        assertEquals("Street 1", addressDtos.get(0).getStreet());
        assertEquals("Country 2", addressDtos.get(1).getCountry());
        assertEquals("City 2", addressDtos.get(1).getCity());
        assertEquals("Street 2", addressDtos.get(1).getStreet());
    }

    @Test
    public void testFindAll_ShouldReturnEmptyList_WhenNoAddressesFound() {
        // Given
        when(addressRepository.findAll()).thenReturn(Collections.emptyList());

        // When
        List<AddressDto> addressDtos = addressService.findAll();

        // Then
        assertEquals(0, addressDtos.size());
    }

    @Test
    public void testFindById_ShouldReturnAddressDto_WhenAddressExists() {
        // Given
        AddressEntity addressEntity = createAddressEntity(1L, "Country 1", "City 1", "Street 1");
        when(addressRepository.findById(1L)).thenReturn(Optional.of(addressEntity));

        // When
        AddressDto addressDto = addressService.findById(1L);

        // Then
        assertEquals(1L, addressDto.getId());
        assertEquals("Country 1", addressDto.getCountry());
        assertEquals("City 1", addressDto.getCity());
        assertEquals("Street 1", addressDto.getStreet());
    }

    @Test
    public void testFindById_ShouldThrowException_WhenAddressDoesNotExist() {
        // Given
        when(addressRepository.findById(1L)).thenReturn(Optional.empty());

        // When
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            addressService.findById(1L);
        });

        // Then
        assertEquals("Address with id 1 does not exist.", exception.getMessage());
    }

    @Test
    @DisplayName("Create Address - Success")
    public void testCreateAddress_Success() {
        // Given
        AddressDto addressDto = createAddressDto("Country 1", "City 1", "Street 1");
        AddressEntity addressEntity = addressDto.toAddressEntity();
        when(addressRepository.save(any(AddressEntity.class))).thenReturn(addressEntity);

        // When
        addressService.createAddress(addressDto);

        // Then
        assertNotNull(addressDto.getId());
        verify(addressRepository, times(1)).save(any(AddressEntity.class));
    }

    @Test
    @DisplayName("Create Address - Exception")
    public void testCreateAddress_Exception() {
        // Given
        AddressDto addressDto = createAddressDto("Country 1", "City 1", "Street 1");
        AddressEntity addressEntity = addressDto.toAddressEntity();
        when(addressRepository.save(addressEntity)).thenReturn(null);

        // When/Then
        AddressCreationException exception = assertThrows(AddressCreationException.class, () -> {
            addressService.createAddress(addressDto);
        });
        assertEquals("Address could not be created", exception.getMessage());
    }

    @Test
    @DisplayName("Partial Update Address - Success")
    public void testPartialUpdateAddress_Success() {
        // Given
        Long addressId = 1L;
        AddressDto addressDto = createAddressDto("Country 1", "City 1", "Street 1");
        AddressEntity addressEntity = createAddressEntity(addressId, "Country 1", "City 1", "Street 1");
        when(addressRepository.findById(addressId)).thenReturn(java.util.Optional.of(addressEntity));
        when(addressRepository.save(addressEntity)).thenReturn(addressEntity);

        // When
        Map<String, Object> changedFields = addressService.partialUpdateAddress(addressId, addressDto);

        // Then
        assertNotNull(changedFields);
        assertEquals(3, changedFields.size());
        assertTrue(changedFields.containsKey("country"));
        assertTrue(changedFields.containsKey("city"));
        assertTrue(changedFields.containsKey("street"));
        assertEquals("Country 1", addressEntity.getCountry());
        assertEquals("City 1", addressEntity.getCity());
        assertEquals("Street 1", addressEntity.getStreet());
        verify(addressRepository, times(1)).findById(addressId);
        verify(addressRepository, times(1)).save(addressEntity);
    }

    @Test
    @DisplayName("Partial Update Address - Address Not Found")
    public void testPartialUpdateAddress_AddressNotFound() {
        // Given
        Long addressId = 1L;
        AddressDto addressDto = createAddressDto("Country 1", "City 1", "Street 1");
        when(addressRepository.findById(addressId)).thenReturn(java.util.Optional.empty());

        // When and Then
        assertThrows(IllegalArgumentException.class, () -> addressService.partialUpdateAddress(addressId, addressDto));
        verify(addressRepository, times(1)).findById(addressId);
        verify(addressRepository, never()).save(any());
    }

    @Test
    @DisplayName("Remove Address - Success")
    public void testRemoveAddress_Success() {
        // Given
        Long addressId = 1L;
        AddressEntity addressEntity = createAddressEntity(addressId);
        when(addressRepository.findById(addressId)).thenReturn(Optional.of(addressEntity));

        // When
        addressService.removeAddress(addressId);

        // Then
        verify(addressRepository, times(1)).findById(addressId);
        verify(addressRepository, times(1)).delete(addressEntity);
    }

    @Test
    @DisplayName("Remove Address - Address Not Found")
    public void testRemoveAddress_AddressNotFound() {
        // Given
        Long addressId = 1L;
        when(addressRepository.findById(addressId)).thenReturn(Optional.empty());

        // When and Then
        assertThrows(IllegalArgumentException.class, () -> addressService.removeAddress(addressId));
        verify(addressRepository, times(1)).findById(addressId);
        verify(addressRepository, never()).delete(any());
    }
}