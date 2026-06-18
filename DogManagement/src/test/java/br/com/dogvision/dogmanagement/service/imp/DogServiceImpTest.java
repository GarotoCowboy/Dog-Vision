package br.com.dogvision.dogmanagement.service.imp;

import br.com.dogvision.dogmanagement.dto.CreateDogRequest;
import br.com.dogvision.dogmanagement.dto.DogResponse;
import br.com.dogvision.dogmanagement.dto.UpdateDogRequest;
import br.com.dogvision.dogmanagement.dto.mapper.DogMapper;
import br.com.dogvision.dogmanagement.infra.exceptions.DogNotFoundException;
import br.com.dogvision.dogmanagement.model.Dog;
import br.com.dogvision.dogmanagement.model.enums.DogRace;
import br.com.dogvision.dogmanagement.model.enums.DogStatus;
import br.com.dogvision.dogmanagement.repository.DogRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;


@ExtendWith(MockitoExtension.class)
class DogServiceImpTest {

    private static final Timestamp DATE_OF_BIRTH = Timestamp.valueOf(LocalDateTime.of(2024, 4, 21, 0, 0));
    private static final Timestamp CREATED_AT = Timestamp.valueOf(LocalDateTime.of(2026, 4, 30, 10, 0));
    private static final Timestamp UPDATED_AT = Timestamp.valueOf(LocalDateTime.of(2026, 5, 1, 9, 30));

    @InjectMocks
    private DogServiceImp service;

    @Mock
    private  DogRepository dogRepository;

    @Mock
    private  DogMapper mapper;

    @Test
    @DisplayName("Should return a DogResponse when a dog is valid")
    void shouldCreateADogWithValidData(){

        //ARRANGE
        CreateDogRequest dto = new CreateDogRequest("lili", DogRace.LABRADOR, DogStatus.TREINAMENTO, 'F',"avatar-001"  ,DATE_OF_BIRTH);

        Dog entity = new Dog(UUID.fromString("de13240d-e6b2-4dbf-a2bd-fe58afdd67cb"),
                "lili", DogRace.LABRADOR, DogStatus.TREINAMENTO, 'F', DATE_OF_BIRTH,"avatar-001" ,CREATED_AT, UPDATED_AT);

        DogResponse response = new DogResponse(UUID.fromString("de13240d-e6b2-4dbf-a2bd-fe58afdd67cb"),"lili", DogRace.LABRADOR, DogStatus.TREINAMENTO, 'F',"avatar-001"  ,DATE_OF_BIRTH, CREATED_AT, UPDATED_AT);

        Mockito.when(mapper.toEntity(dto)).thenReturn(entity);
        Mockito.when(dogRepository.save(entity)).thenReturn(entity);
        Mockito.when(mapper.toResponse(entity)).thenReturn(response);

        //ACT

        DogResponse responseAct = service.save(dto);

        //ASSERT

        Assertions.assertEquals("lili",responseAct.name());
        Assertions.assertEquals(DogRace.LABRADOR,responseAct.race());
        Assertions.assertEquals(DogStatus.TREINAMENTO,responseAct.status());
        Assertions.assertEquals('F',responseAct.sex());
        Assertions.assertEquals(DATE_OF_BIRTH, responseAct.dateOfBirth());

        Mockito.verify(mapper, Mockito.times(1)).toEntity(dto);
        Mockito.verify(dogRepository, Mockito.times(1)).save(entity);
        Mockito.verify(mapper, Mockito.times(1)).toResponse(entity);
    }

    @Test
    void shouldUpdateADogWithValidData(){
        var id = UUID.fromString("de13240d-e6b2-4dbf-a2bd-fe58afdd67cb");

        Dog entity = new Dog(id,"lili", DogRace.LABRADOR, DogStatus.TREINAMENTO, 'F', DATE_OF_BIRTH,"avatar-001" ,CREATED_AT, UPDATED_AT);

        UpdateDogRequest updateDogRequest = new UpdateDogRequest(id, DogStatus.CEDIDO);
        DogResponse response = new DogResponse(id,"lili", DogRace.LABRADOR, DogStatus.CEDIDO, 'F', "avatar-001",DATE_OF_BIRTH, CREATED_AT, UPDATED_AT);


        Mockito.when(dogRepository.findById(id)).thenReturn(Optional.of(entity));
        Mockito.doNothing().when(mapper).updateFromDto(updateDogRequest,entity);
        Mockito.when(dogRepository.save(entity)).thenReturn(entity);
        Mockito.when(mapper.toResponse(entity)).thenReturn(response);

        DogResponse responseAct = service.update(id,updateDogRequest);


        Assertions.assertEquals("lili", responseAct.name());
        Assertions.assertEquals(DogRace.LABRADOR, responseAct.race());
        Assertions.assertEquals(DogStatus.CEDIDO, responseAct.status());
        Assertions.assertEquals('F', responseAct.sex());
        Assertions.assertEquals(DATE_OF_BIRTH, responseAct.dateOfBirth());


        Mockito.verify(dogRepository, Mockito.times(1)).findById(id);
        Mockito.verify(mapper, Mockito.times(1)).updateFromDto(updateDogRequest, entity);
        Mockito.verify(dogRepository, Mockito.times(1)).save(entity);
        Mockito.verify(mapper, Mockito.times(1)).toResponse(entity);
    }

    @Test
    @DisplayName("Should return DogResponse when dog id exists")
    void findById(){

        //ARRANGE
        var id = "de13240d-e6b2-4dbf-a2bd-fe58afdd67cb";

        Dog entity = new Dog(UUID.fromString(id),
                "lili", DogRace.LABRADOR, DogStatus.TREINAMENTO, 'F',DATE_OF_BIRTH,"avatar-001" ,CREATED_AT, UPDATED_AT);


        DogResponse response = new DogResponse(UUID.fromString(id),"lili", DogRace.LABRADOR, DogStatus.TREINAMENTO, 'F', "avatar-001" ,DATE_OF_BIRTH, CREATED_AT, UPDATED_AT);

        Mockito.when(mapper.toResponse(entity)).thenReturn(response);
        Mockito.when(dogRepository.findById(UUID.fromString(id))).thenReturn(Optional.of(entity));

        //ACT

        DogResponse responseAct = service.getById(UUID.fromString(id));

        //ASSERT

        Assertions.assertEquals("lili",responseAct.name());
        Assertions.assertEquals(DogRace.LABRADOR,responseAct.race());
        Assertions.assertEquals(DogStatus.TREINAMENTO,responseAct.status());
        Assertions.assertEquals('F',responseAct.sex());
        Assertions.assertEquals(DATE_OF_BIRTH,responseAct.dateOfBirth());


        Mockito.verify(dogRepository, Mockito.times(1)).findById(UUID.fromString(id));
        Mockito.verify(mapper, Mockito.times(1)).toResponse(entity);
    }

    @Test
    @DisplayName("Should throw DogNotFoundException when dog id does not exist")
    void findIdNotFound(){

        //ARRANGE
        var id = UUID.fromString("de13240d-e6b2-4dbf-a2bd-fe58afdd67cb");

        //ACT
        Mockito.when(dogRepository.findById(id)).thenReturn(Optional.empty());

        //ASSERT
        Assertions.assertThrows(DogNotFoundException.class, () -> service.getById(id));
    }

    @Test
    @DisplayName("Should delete a dog with valid id")
    void shouldDeleteADogWithValidId(){
        var id = UUID.fromString("de13240d-e6b2-4dbf-a2bd-fe58afdd67cb");

        Dog entity = new Dog(id, "lili", DogRace.LABRADOR, DogStatus.TREINAMENTO, 'F', DATE_OF_BIRTH, "avatar-001",CREATED_AT, UPDATED_AT);

        Mockito.when(dogRepository.findById(id)).thenReturn(Optional.of(entity));
        Mockito.doNothing().when(dogRepository).delete(entity);

        service.delete(id);

        Mockito.verify(dogRepository, Mockito.times(1)).findById(id);
        Mockito.verify(dogRepository, Mockito.times(1)).delete(entity);
    }

    @Test
    @DisplayName("Should throw DogNotFoundException when deleting with invalid id")
    void shouldThrowWhenDeletingWithInvalidId(){
        var id = UUID.fromString("de13240d-e6b2-4dbf-a2bd-fe58afdd67cb");

        Mockito.when(dogRepository.findById(id)).thenReturn(Optional.empty());

        Assertions.assertThrows(DogNotFoundException.class, () -> service.delete(id));

        Mockito.verify(dogRepository, Mockito.never()).delete(Mockito.any());
    }
}
