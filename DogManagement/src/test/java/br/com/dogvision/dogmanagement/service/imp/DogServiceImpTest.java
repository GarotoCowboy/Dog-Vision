package br.com.dogvision.dogmanagement.service.imp;

import br.com.dogvision.dogmanagement.dto.CreateDogRequest;
import br.com.dogvision.dogmanagement.dto.DogResponse;
import br.com.dogvision.dogmanagement.dto.UpdateDogRequest;
import br.com.dogvision.dogmanagement.dto.mapper.DogMapper;
import br.com.dogvision.dogmanagement.infra.exceptions.BusinessException;
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

        Dog entity = new Dog(UUID.fromString("de13240d-e6b2-4dbf-a2bd-fe58afdd67cb"),"lili",DogRace.LABRADOR,DogStatus.TREINAMENTO,'F',DATE_OF_BIRTH,"avatar_001",CREATED_AT,UPDATED_AT,true);
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

        Dog entity = new Dog(id,"lili", DogRace.LABRADOR, DogStatus.TREINAMENTO, 'F', DATE_OF_BIRTH,"avatar-001" ,CREATED_AT, UPDATED_AT, true);

        UpdateDogRequest updateDogRequest = new UpdateDogRequest(id, DogStatus.REPRODUCAO);
        DogResponse response = new DogResponse(id,"lili", DogRace.LABRADOR, DogStatus.REPRODUCAO, 'F', "avatar-001",DATE_OF_BIRTH, CREATED_AT, UPDATED_AT);


        Mockito.when(dogRepository.findById(id)).thenReturn(Optional.of(entity));
        Mockito.doNothing().when(mapper).updateFromDto(updateDogRequest,entity);
        Mockito.when(dogRepository.save(entity)).thenReturn(entity);
        Mockito.when(mapper.toResponse(entity)).thenReturn(response);

        DogResponse responseAct = service.update(id,updateDogRequest);


        Assertions.assertEquals("lili", responseAct.name());
        Assertions.assertEquals(DogRace.LABRADOR, responseAct.race());
        Assertions.assertEquals(DogStatus.REPRODUCAO, responseAct.status());
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
                "lili", DogRace.LABRADOR, DogStatus.TREINAMENTO, 'F',DATE_OF_BIRTH,"avatar-001" ,CREATED_AT, UPDATED_AT,true);


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

        Dog entity = new Dog(id, "lili", DogRace.LABRADOR, DogStatus.TREINAMENTO, 'F', DATE_OF_BIRTH, "avatar-001",CREATED_AT, UPDATED_AT,true);

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

    @Test
    @DisplayName("Should throw BusinessException when creating dog with status DOADO and onKennel true")
    void shouldThrowWhenCreatingDogWithStatusDoadoAndOnKennelTrue() {
        CreateDogRequest request = new CreateDogRequest(
                "Bob", DogRace.LABRADOR, DogStatus.DOADO, 'M', "avatar-001", DATE_OF_BIRTH, true
        );

        Assertions.assertThrows(BusinessException.class, () -> service.save(request));
        Mockito.verify(dogRepository, Mockito.never()).save(Mockito.any());
    }

    @Test
    @DisplayName("Should throw BusinessException when creating dog with status FALECIDO and onKennel true")
    void shouldThrowWhenCreatingDogWithStatusFalecidoAndOnKennelTrue() {
        CreateDogRequest request = new CreateDogRequest(
                "Bob", DogRace.LABRADOR, DogStatus.FALECIDO, 'M', "avatar-001", DATE_OF_BIRTH, true
        );

        Assertions.assertThrows(BusinessException.class, () -> service.save(request));
        Mockito.verify(dogRepository, Mockito.never()).save(Mockito.any());
    }

    @Test
    @DisplayName("Should save dog with status DOADO and ensure onKennel is false")
    void shouldSaveDogWithStatusDoadoAndSetOnKennelFalse() {
        CreateDogRequest request = new CreateDogRequest(
                "Bob", DogRace.LABRADOR, DogStatus.DOADO, 'M', "avatar-001", DATE_OF_BIRTH, false
        );
        Dog entity = new Dog(UUID.randomUUID(), "Bob", DogRace.LABRADOR, DogStatus.DOADO, 'M', DATE_OF_BIRTH, "avatar-001", CREATED_AT, UPDATED_AT, false);
        DogResponse response = new DogResponse(entity.getID(), "Bob", DogRace.LABRADOR, DogStatus.DOADO, 'M', "avatar-001", DATE_OF_BIRTH, CREATED_AT, UPDATED_AT, false);

        Mockito.when(mapper.toEntity(request)).thenReturn(entity);
        Mockito.when(dogRepository.save(entity)).thenReturn(entity);
        Mockito.when(mapper.toResponse(entity)).thenReturn(response);

        DogResponse result = service.save(request);

        Assertions.assertFalse(entity.getOnKennel());
        Assertions.assertFalse(result.onKennel());
        Mockito.verify(dogRepository).save(entity);
    }

    @Test
    @DisplayName("Should automatically set onKennel to false when updating dog status to DOADO")
    void shouldAutomaticallySetOnKennelFalseWhenUpdatingToStatusDoado() {
        UUID id = UUID.randomUUID();
        Dog entity = new Dog(id, "Bob", DogRace.LABRADOR, DogStatus.TREINAMENTO, 'M', DATE_OF_BIRTH, "avatar-001", CREATED_AT, UPDATED_AT, true);
        UpdateDogRequest updateRequest = new UpdateDogRequest(id, DogStatus.DOADO);
        DogResponse response = new DogResponse(id, "Bob", DogRace.LABRADOR, DogStatus.DOADO, 'M', "avatar-001", DATE_OF_BIRTH, CREATED_AT, UPDATED_AT, false);

        Mockito.when(dogRepository.findById(id)).thenReturn(Optional.of(entity));
        Mockito.doAnswer(invocation -> {
            entity.setStatus(DogStatus.DOADO);
            return null;
        }).when(mapper).updateFromDto(updateRequest, entity);
        Mockito.when(dogRepository.save(entity)).thenReturn(entity);
        Mockito.when(mapper.toResponse(entity)).thenReturn(response);

        DogResponse result = service.update(id, updateRequest);

        Assertions.assertFalse(entity.getOnKennel());
        Assertions.assertEquals(DogStatus.DOADO, result.status());
        Mockito.verify(dogRepository).save(entity);
    }

    @Test
    @DisplayName("Should throw BusinessException when updating dog with status DOADO and onKennel true")
    void shouldThrowWhenUpdatingDogToStatusDoadoAndOnKennelTrue() {
        UUID id = UUID.randomUUID();
        Dog entity = new Dog(id, "Bob", DogRace.LABRADOR, DogStatus.TREINAMENTO, 'M', DATE_OF_BIRTH, "avatar-001", CREATED_AT, UPDATED_AT, true);
        UpdateDogRequest updateRequest = new UpdateDogRequest(id, DogStatus.DOADO, true);

        Mockito.when(dogRepository.findById(id)).thenReturn(Optional.of(entity));

        Assertions.assertThrows(BusinessException.class, () -> service.update(id, updateRequest));
        Mockito.verify(dogRepository, Mockito.never()).save(Mockito.any());
    }
}
