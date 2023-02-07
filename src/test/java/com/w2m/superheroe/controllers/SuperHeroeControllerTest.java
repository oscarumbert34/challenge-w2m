package com.w2m.superheroe.controllers;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.w2m.superheroe.UtilsTest;
import com.w2m.superheroe.configuration.SecurityConfig;
import com.w2m.superheroe.exceptions.SuperHeroeException;
import com.w2m.superheroe.models.entities.dtos.SuperHeroeDTO;
import com.w2m.superheroe.services.domains.SuperHeroeService;
import com.w2m.superheroe.services.security.TokenService;
import com.w2m.superheroe.utils.Messages;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest({SuperHeroeController.class, AuthController.class})
@Import({SecurityConfig.class, TokenService.class})
public class SuperHeroeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private SuperHeroeService superHeroeService;

    private UtilsTest utilsTest = new UtilsTest();
    private ObjectMapper objectMapper;

    @BeforeEach
    public void setUp(){
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
    }

    @Test
    public void whenGetSuperHeroeIsOk() throws Exception {

        when(superHeroeService.findById(1L)).thenReturn(utilsTest.buildSuperHeroeDTO());

        mockMvc.perform(get("/super-heroes/1").contentType(MediaType.APPLICATION_JSON )
                        .header("Authorization", "Bearer " + getToken()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("SUPERMAN"))
                .andExpect(jsonPath("$.birthday").value("2000-09-22"));

    }
    @Test
    public void whenGetSuperHeroeAllIsOk() throws Exception {

        when(superHeroeService.findAll()).thenReturn(utilsTest.buildSuperheroeDTOS());

        mockMvc.perform(get("/super-heroes").contentType(MediaType.APPLICATION_JSON )
                        .header("Authorization", "Bearer " + getToken()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[1].id").value(2L))
                .andExpect(jsonPath("$[0].name").value("SUPERMAN"))
                .andExpect(jsonPath("$[1].name").value("SPIDERMAN"))
                .andExpect(jsonPath("$[0].birthday").value("2000-09-22"))
                .andExpect(jsonPath("$[1].birthday").value("1998-09-22"))
                .andExpect(jsonPath("$", hasSize(2)));
    }

    @Test
    public void whenGetSuperHeroeNameLikeIsOk() throws Exception {

        when(superHeroeService.findByNameLike("MAN")).thenReturn(utilsTest.buildSuperheroeDTOS());

        mockMvc.perform(get("/super-heroes/name/MAN").contentType(MediaType.APPLICATION_JSON )
                        .header("Authorization", "Bearer " + getToken()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[1].id").value(2L))
                .andExpect(jsonPath("$[0].name").value("SUPERMAN"))
                .andExpect(jsonPath("$[1].name").value("SPIDERMAN"))
                .andExpect(jsonPath("$[0].birthday").value("2000-09-22"))
                .andExpect(jsonPath("$[1].birthday").value("1998-09-22"))
                .andExpect(jsonPath("$", hasSize(2)));
    }

    @Test
    public void whenUpdateSuperHeroeIsOk() throws Exception {

        SuperHeroeDTO superHeroeDTO = utilsTest.buildSuperHeroeDTO();
        doNothing().when(superHeroeService).update(any());

        mockMvc.perform(patch("/super-heroes").contentType(MediaType.APPLICATION_JSON )
                        .header("Authorization", "Bearer " + getToken())
                        .content(objectMapper.writeValueAsString(superHeroeDTO)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));

        verify(superHeroeService).update(any());
    }

    @Test
    public void whenDeleteSuperHeroeIsOk() throws Exception {
        doNothing().when(superHeroeService).delete(1L);

        mockMvc.perform(delete("/super-heroes/1").contentType(MediaType.APPLICATION_JSON )
                        .header("Authorization", "Bearer " + getToken()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));

        verify(superHeroeService).delete(1L);

    }

    @Test
    public void whenGetSuperHeroeNotExists() throws Exception {

        when(superHeroeService.findById(1L)).thenThrow(new SuperHeroeException(Messages.NOT_EXIST_SUPER_HEROE_MESSAGE));

        mockMvc.perform(get("/super-heroes/1").contentType(MediaType.APPLICATION_JSON )
                        .header("Authorization", "Bearer " + getToken()))
                .andExpect(status().isNotFound());

    }

    @Test
    public void whenGetSuperHeroeAllFailBecauseTokenInvalid() throws Exception {

        when(superHeroeService.findAll()).thenReturn(utilsTest.buildSuperheroeDTOS());

        mockMvc.perform(get("/super-heroes").contentType(MediaType.APPLICATION_JSON )
                        .header("Authorization", "Bearer ddadasdasdasdas"))
                .andExpect(status().isUnauthorized());
    }
    @Test
    public void whenUpdateSuperHeroeFailBecauseIdNull() throws Exception {

        SuperHeroeDTO superHeroeDTO = utilsTest.buildSuperHeroeDTO(null,2020,"prueba");

        mockMvc.perform(patch("/super-heroes").contentType(MediaType.APPLICATION_JSON )
                        .header("Authorization", "Bearer " + getToken())
                        .content(objectMapper.writeValueAsString(superHeroeDTO)))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void whenDeleteSuperHeroeFailBecauseDBNotFound() throws Exception {
        doThrow(new RuntimeException("Not found database")).when(superHeroeService).delete(1L);

        mockMvc.perform(delete("/super-heroes/1").contentType(MediaType.APPLICATION_JSON )
                        .header("Authorization", "Bearer " + getToken()))
                .andExpect(status().isInternalServerError());

        verify(superHeroeService).delete(1L);

    }

    private String getToken() throws Exception {
        MvcResult result = mockMvc.perform(post("/token")
                        .with(httpBasic("oscar", "password")))
                .andExpect(status().isOk())
                .andReturn();

        return result.getResponse().getContentAsString();
    }
}
