package br.com.dogvision.dogmanagement.controller;


import br.com.dogvision.dogmanagement.dto.CreateDogRequest;
import br.com.dogvision.dogmanagement.dto.DogResponse;
import br.com.dogvision.dogmanagement.dto.UpdateDogRequest;
import br.com.dogvision.dogmanagement.infra.exceptions.DogNotFoundException;
import br.com.dogvision.dogmanagement.infra.security.TokenService;
import br.com.dogvision.dogmanagement.model.enums.DogRace;
import br.com.dogvision.dogmanagement.model.enums.DogStatus;
import br.com.dogvision.dogmanagement.service.imp.DogServiceImp;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SuppressWarnings("DataFlowIssue")
@WebMvcTest(controllers = DogController.class)
@AutoConfigureMockMvc(addFilters = false)
class DogControllerTest {

    private static final Timestamp DATE_OF_BIRTH = Timestamp.valueOf(LocalDateTime.of(2024, 4, 21, 0, 0));
    private static final Timestamp CREATED_AT = Timestamp.valueOf(LocalDateTime.of(2026, 4, 30, 10, 0));
    private static final Timestamp UPDATED_AT = Timestamp.valueOf(LocalDateTime.of(2026, 5, 1, 9, 30));

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private TokenService tokenService;

    @MockitoBean
    private DogServiceImp service;

    @Autowired
    private ObjectMapper objectMapper;

    private DogResponse response;


    @BeforeEach
    public void createDogResponse(){
        this.response = new DogResponse(UUID.fromString("de13240d-e6b2-4dbf-a2bd-fe58afdd67cb"),"lili", DogRace.LABRADOR, DogStatus.TREINAMENTO, 'F',"avatar-001" ,DATE_OF_BIRTH, CREATED_AT, UPDATED_AT);
    }



    @Test
    void SearchByIdAndReturns200WithBodyifExists() throws Exception{
        //Arrange

        var id = UUID.fromString("de13240d-e6b2-4dbf-a2bd-fe58afdd67cb");
        Mockito.when(service.getById(id)).thenReturn(response);


        //assert
        mockMvc.perform(get("/api/v1/dogs/{id}",id)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("lili"))
                .andExpect(jsonPath("$.race").value("LABRADOR"));


        //act
        Mockito.verify(service).getById(id);

    }

    @Test
    void SearchByIdIfNotExistsAndReturns404() throws Exception{

        //Arrange

        var inexistId = UUID.fromString("051ea3d8-693e-4775-a313-e0c768f984c4");

        Mockito.when(service.getById(inexistId))
                .thenThrow(new DogNotFoundException(inexistId));

        //assert
        mockMvc.perform(get("/api/v1/dogs/{id}",inexistId))
                .andExpect(status().isNotFound());

        //act
        Mockito.verify(service).getById(inexistId);
    }

    @Test
    void listDogsShouldReturn200WithBody() throws Exception {
        DogResponse dog1 = new DogResponse(
                UUID.fromString("de13240d-e6b2-4dbf-a2bd-fe58afdd67cb"),"Pandora", DogRace.BORDER_COLLIER, DogStatus.TREINAMENTO, 'F', "avatar-001",DATE_OF_BIRTH, CREATED_AT, UPDATED_AT
        );

        DogResponse dog2 = new DogResponse(
                UUID.fromString("de13240d-e6b2-4dbf-a2bd-fe58afdd67cb"),"Lili", DogRace.LABRADOR, DogStatus.TREINAMENTO, 'F', "avatar-001",DATE_OF_BIRTH, CREATED_AT, UPDATED_AT
        );

        Mockito.when(service.getAll()).thenReturn(List.of(dog1, dog2));

        mockMvc.perform(get("/api/v1/dogs"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$", Matchers.hasSize(2)))
                .andExpect(jsonPath("$[0].name").value("Pandora"))
                .andExpect(jsonPath("$[0].race").value("BORDER_COLLIER"))
                .andExpect(jsonPath("$[1].name").value("Lili"))
                .andExpect(jsonPath("$[1].race").value("LABRADOR"));

        Mockito.verify(service).getAll();
    }

    @Test
    void listDogsShouldReturn200WhenListIsEmpty() throws Exception {
        Mockito.when(service.getAll()).thenReturn(List.of());

        mockMvc.perform(get("/api/v1/dogs"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$", Matchers.hasSize(0)));

        Mockito.verify(service).getAll();
    }

    @Test
    void CreateADogAndReturns200WithABody() throws Exception{

        //Arrange

       CreateDogRequest request = new CreateDogRequest("lili", DogRace.LABRADOR, DogStatus.TREINAMENTO, 'F', "avatar-001",DATE_OF_BIRTH);

       Mockito.when(service.save(Mockito.any(CreateDogRequest.class)))
                       .thenReturn(response);

       //act
        mockMvc.perform(post("/api/v1/dogs")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))


       //assert
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("lili"))
                .andExpect(jsonPath("$.race").value("LABRADOR"));



        Mockito.verify(service).save(Mockito.any(CreateDogRequest.class));
    }

    @Test
    void CreateADogWithErrorAndReturns400() throws Exception{
        CreateDogRequest invalidRequest = new CreateDogRequest("lili", null, null, 'F', "avatar-001",DATE_OF_BIRTH);

            // arrange
        mockMvc.perform(post("/api/v1/dogs")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRequest)))
                // assert
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.message").value("Validation Error"));

        //act
        Mockito.verify(service, Mockito.never()).save(Mockito.any());

    }

    @Test
    void deleteDogShouldReturn204WhenSuccessful() throws Exception{
        //arrange
        var id = UUID.fromString("051ea3d8-693e-4775-a313-e0c768f984c4");

        mockMvc.perform(delete("/api/v1/dogs/{id}",id))

        //assert
                .andExpect(status().isNoContent());

        //act
        Mockito.verify(service).delete(id);

    }

    @Test
    void deleteDogShouldReturn404WhenError() throws Exception{
        //arrange
        var id = UUID.fromString("051ea3d8-693e-4775-a313-e0c768f984c4");

    Mockito.doThrow(new DogNotFoundException(id))
                    .when(service).delete(id);

        mockMvc.perform(delete("/api/v1/dogs/{id}",id))
                //assert
                .andExpect(status().isNotFound())
        ;

        //act
        Mockito.verify(service).delete(id);

    }

    @Test
    void UpdateDogShouldReturn200WhenSuccessful() throws Exception{
        //arrange

        var id = UUID.fromString("051ea3d8-693e-4775-a313-e0c768f984c4");

        UpdateDogRequest updateDogRequest = new UpdateDogRequest(
                id, DogStatus.TREINAMENTO);

        DogResponse updateResponse = new DogResponse(
                UUID.fromString("de13240d-e6b2-4dbf-a2bd-fe58afdd67cb"),"Pandora", DogRace.BORDER_COLLIER, DogStatus.TREINAMENTO, 'F', "avatar-001",DATE_OF_BIRTH, CREATED_AT, UPDATED_AT);

        Mockito.when(service.update(Mockito.eq(id),Mockito.any(UpdateDogRequest.class)))
                .thenReturn(updateResponse);


        mockMvc.perform(put("/api/v1/dogs/{id}",id)
                .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateDogRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Pandora"))
                .andExpect(jsonPath("$.race").value("BORDER_COLLIER"));

        Mockito.verify(service).update(Mockito.eq(id),Mockito.any(UpdateDogRequest.class));

    }

    @Test
    void UpdateDogShouldReturn400WhenError() throws Exception{


        var id = UUID.fromString("051ea3d8-693e-4775-a313-e0c768f984c4");

        UpdateDogRequest invalidUpdateRequest = new UpdateDogRequest(
                null, DogStatus.TREINAMENTO);



        mockMvc.perform(put("/api/v1/dogs/{id}",id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidUpdateRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.message").value("Validation Error"));


        Mockito.verify(service, Mockito.never()).update(Mockito.eq(id),Mockito.any());

    }

    @Test
    void updateDogShouldReturn404WhenDogDoesNotExist() throws Exception {
        var id = UUID.fromString("051ea3d8-693e-4775-a313-e0c768f984c4");

        UpdateDogRequest request = new UpdateDogRequest(
                id, DogStatus.TREINAMENTO
        );

        Mockito.when(service.update(Mockito.eq(id), Mockito.any(UpdateDogRequest.class)))
                .thenThrow(new DogNotFoundException(id));

        mockMvc.perform(put("/api/v1/dogs/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.message").exists());

        Mockito.verify(service).update(Mockito.eq(id), Mockito.any(UpdateDogRequest.class));
    }

}
