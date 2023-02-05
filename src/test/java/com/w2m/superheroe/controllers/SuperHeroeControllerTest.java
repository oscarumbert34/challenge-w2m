package com.w2m.superheroe.controllers;


import com.w2m.superheroe.UtilsTest;
import com.w2m.superheroe.configuration.SecurityConfig;
import com.w2m.superheroe.services.domains.SuperHeroeService;
import com.w2m.superheroe.services.security.TokenService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

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

        when(superHeroeService.finAll()).thenReturn(utilsTest.buildSuperheroeDTOS());

        mockMvc.perform(get("/super-heroes").contentType(MediaType.APPLICATION_JSON )
                        .header("Authorization", "Bearer " + getToken()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.size").value(2L));
    }

    @Test
    public void whenGetSuperHeroeAllIsOk() throws Exception {

        when(superHeroeService.findByNameLike()).thenReturn(utilsTest.buildSuperheroeDTOS());

        mockMvc.perform(get("/super-heroes/SUPERMAN").contentType(MediaType.APPLICATION_JSON )
                        .header("Authorization", "Bearer " + getToken()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.size").value(2L));
    }

    @Test
    public void whenUpdateSuperHeroeIsOk() throws Exception {

        mockMvc.perform(patch("/super-heroes/SUPERMAN").contentType(MediaType.APPLICATION_JSON )
                        .header("Authorization", "Bearer " + getToken()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    public void whenDeleteSuperHeroeIsOk() throws Exception {

        mockMvc.perform(delete("/super-heroes/SUPERMAN").contentType(MediaType.APPLICATION_JSON )
                        .header("Authorization", "Bearer " + getToken()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    private String getToken() throws Exception {
        MvcResult result = mockMvc.perform(post("/token")
                        .with(httpBasic("jefe", "password")))
                .andExpect(status().isOk())
                .andReturn();

        return result.getResponse().getContentAsString();
    }
}
