package com.w2m.superheroe.services.domains;


import com.w2m.superheroe.UtilsTest;
import com.w2m.superheroe.exceptions.SuperHeroeException;
import com.w2m.superheroe.models.entities.SuperHeroe;
import com.w2m.superheroe.models.entities.dtos.SuperHeroeDTO;
import com.w2m.superheroe.repositories.SuperHeroeRepository;
import com.w2m.superheroe.services.domains.impl.SuperHeroeServiceImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class SuperHeroeServiceTest {

    private SuperHeroeService superHeroeService = new SuperHeroeServiceImpl();

    @Mock
    private SuperHeroeRepository superHeroeRepository;

    private UtilsTest utilsTest = new UtilsTest();

    @Test
    @DisplayName("Validar que existe el superHeroe pasado por parametro")
    public void whenFindByExist(){
        Long id = 1L;
        SuperHeroe superHeroeMock = utilsTest.buildSuperHeroe();
        when(superHeroeRepository.findById(id)).thenReturn(Optional.of(superHeroeMock));

        SuperHeroeDTO superHeroeDTO = superHeroeService.findById(id);

        assertAll(() -> assertEquals(superHeroeDTO.getId(), superHeroeMock.getId()),
                () -> assertEquals(superHeroeDTO.getBirthday(), superHeroeMock.getBirthday()),
                () -> assertEquals(superHeroeDTO.getName(), superHeroeMock.getName()));
    }

    @Test
    @DisplayName("Validar que se arroja una excepcion cuando el superHeroe no existe")
    public void whenFindByNotExist(){

        when(superHeroeRepository.findById(1L)).thenReturn(Optional.ofNullable(null));

        Exception exception = assertThrows(SuperHeroeException.class, () -> {
            superHeroeService.findById(1L);
        });

        String actual = exception.getMessage();
        String expected = "El super heroe con el id 1 no existe";
        assertEquals(expected, actual);


    }

    @Test
    @DisplayName("Validar que se devuelve una lista de Superheroe que estan en la base")
    public void whenFindAllReturnSuperHeroes(){
        when(superHeroeRepository.findAll()).thenReturn(utilsTest.buildSuperheroes());

        List<SuperHeroeDTO> superHeroeDTOS = superHeroeService.findAll();
        assertEquals(superHeroeDTOS.size(), 2);

    }

    @Test
    @DisplayName("Validar que se devuelve una lista vacia de Superheroe cuando no hay registro en la base de datos")
    public void whenFindAllReturnEmpty(){
        when(superHeroeRepository.findAll()).thenReturn(List.of());

        List<SuperHeroeDTO> superHeroes = superHeroeService.findAll();
        assertEquals(superHeroes.size(), 0);
    }

    @Test
    @DisplayName("Validar que se devuelve una lista de Superheroe con el nombre pasado por parametro")
    public void whenFindNameLikeReturnSuperHeroes(){
        String name = "man";
        when(superHeroeRepository.findByNameLike(name)).thenReturn(utilsTest.buildSuperheroes());

        List<SuperHeroeDTO> superHeroes = superHeroeService.findByNameLike(name);
        assertEquals(superHeroes.size(), 2);

    }

    @Test
    @DisplayName("Validar que se devuelve una lista vacia de Superheroe con el nombre pasado por parametro")
    public void whenFindNameLikeReturnEmpty(){
        String name = "man";
        when(superHeroeRepository.findByNameLike(name)).thenReturn(List.of());

        List<SuperHeroeDTO> superHeroes = superHeroeService.findByNameLike(name);
        assertEquals(superHeroes.size(), 0);

    }

    @Test
    @DisplayName("Validar que cuando se pasa un dto para modificar el registro se hace correctamente")
    public void whenUpdateIsOk(){
        SuperHeroeDTO superHeroeDTO = utilsTest.buildSuperHeroeDTO();
        SuperHeroe superHeroeMockOld = utilsTest.buildSuperHeroe();
        SuperHeroe superHeroeMockNew = utilsTest.buildSuperHeroe();
        superHeroeMockNew.setName("SUPERMANCITO");

        when(superHeroeRepository.findById(superHeroeDTO.getId())).thenReturn(Optional.of(superHeroeMockOld));
        when(superHeroeRepository.save(superHeroeMockNew)).thenReturn(superHeroeMockNew);

        superHeroeService.update(superHeroeDTO);

        verify(superHeroeRepository).findById(superHeroeDTO.getId());
        verify(superHeroeRepository).save(superHeroeMockNew);
    }

    @Test
    @DisplayName("Validar que cuando se pasa un dto para modificar el cual no existe se arroja una excepcion")
    public void whenUpdateFailBecauseSuperHeroeNotExist(){
        SuperHeroeDTO superHeroeDTO = utilsTest.buildSuperHeroeDTO();

        when(superHeroeRepository.findById(superHeroeDTO.getId())).thenReturn(Optional.ofNullable(null));

        Exception exception = assertThrows(SuperHeroeException.class, () -> {
            superHeroeService.update(superHeroeDTO);
        });

        verify(superHeroeRepository).findById(superHeroeDTO.getId());

        String actual = exception.getMessage();
        String expected = "El super heroe con el id 1 no existe";
        assertEquals(expected, actual);

    }

    @Test
    @DisplayName("Validar que cuando se pasa el identificador de un superheroe para eliminarlo y este existe termina exitosamente")
    public void whenDeleteIsOk(){
        Long id = 1L;
        SuperHeroe superHeroeMock = utilsTest.buildSuperHeroe();

        when(superHeroeRepository.findById(id)).thenReturn(Optional.of(superHeroeMock));
        doNothing().when(superHeroeRepository).delete(superHeroeMock);

        superHeroeService.delete(id);

        verify(superHeroeRepository).findById(id);
        verify(superHeroeRepository).delete(superHeroeMock);
    }

    @Test
    @DisplayName("Validar que cuando se pasa el identificador de un superheroe para eliminarlo y este NO existe se arroja una excepcion")
    public void whenDeleteFailBecauseSuperHeroeNotExist(){
        Long id = 1L;
        when(superHeroeRepository.findById(id)).thenReturn(Optional.ofNullable(null));

        Exception exception = assertThrows(SuperHeroeException.class, () -> {
            superHeroeService.delete(id);
        });

        verify(superHeroeRepository).findById(id);
        String actual = exception.getMessage();
        String expected = "El super heroe con el id 1 no existe";
        assertEquals(expected, actual);
    }

}
