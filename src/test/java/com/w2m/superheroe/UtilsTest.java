package com.w2m.superheroe;

import com.w2m.superheroe.models.entities.SuperHeroe;
import com.w2m.superheroe.models.entities.dtos.SuperHeroeDTO;

import java.time.Instant;
import java.time.LocalDate;
import java.util.List;

public class UtilsTest {
    public SuperHeroe buildSuperHeroe(Long id, int year, String name){
        return SuperHeroe.builder().birthday(LocalDate.of(year,9,22))
                .id(id).name(name).creationDate(Instant.now()).updateDate(Instant.now()).build();
    }
    public SuperHeroe buildSuperHeroe(){
        return buildSuperHeroe(1L, 2000,"SUPERMAN");
    }

    public List<SuperHeroe> buildSuperheroes(){
        return List.of(buildSuperHeroe(),buildSuperHeroe(2L,1998,"SPIDERMAN"));
    }

    public SuperHeroeDTO buildSuperHeroeDTO(Long id, int year, String name){
        return SuperHeroeDTO.builder().birthday(LocalDate.of(year,9,22))
                .id(id).name(name).build();
    }

    public SuperHeroeDTO buildSuperHeroeDTO(){
        return buildSuperHeroeDTO(1L, 2000,"SUPERMAN");
    }

    public List<SuperHeroeDTO> buildSuperheroeDTOS(){
        return List.of(buildSuperHeroeDTO(),buildSuperHeroeDTO(2L,1998,"SPIDERMAN"));
    }
}
