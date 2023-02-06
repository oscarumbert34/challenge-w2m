package com.w2m.superheroe.services.domains;


import com.w2m.superheroe.UtilsTest;
import com.w2m.superheroe.exceptions.SuperHeroeException;
import com.w2m.superheroe.models.entities.SuperHeroe;
import com.w2m.superheroe.models.entities.dtos.SuperHeroeDTO;
import com.w2m.superheroe.repositories.SuperHeroeRepository;
import com.w2m.superheroe.services.domains.impl.SuperHeroeServiceImpl;
import com.w2m.superheroe.services.mapper.SuperHeroMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static com.w2m.superheroe.services.domains.impl.SuperHeroeServiceImpl.LIKE;
import static com.w2m.superheroe.utils.Messages.NOT_EXIST_SUPER_HEROE_MESSAGE;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class SuperHeroeServiceTest {

    @Mock
    private SuperHeroeRepository superHeroeRepository;

    @Mock
    private SuperHeroMapper superHeroMapper;

    private UtilsTest utilsTest = new UtilsTest();

    private SuperHeroeService superHeroeService;

    @BeforeEach
    public void setUp(){
        superHeroeService = new SuperHeroeServiceImpl(superHeroeRepository, superHeroMapper);
    }
    @Test
    @DisplayName("Validar que existe el superHeroe pasado por parametro")
    public void whenFindByExist(){
        Long id = 1L;
        SuperHeroe superHeroeMock = utilsTest.buildSuperHeroe();
        SuperHeroeDTO superHeroeDTOMock = utilsTest.buildSuperHeroeDTO();

        when(superHeroeRepository.findById(id)).thenReturn(Optional.of(superHeroeMock));
        when(superHeroMapper.toDTO(superHeroeMock)).thenReturn(superHeroeDTOMock);
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
        String expected = NOT_EXIST_SUPER_HEROE_MESSAGE.replace("#","1");
        assertEquals(expected, actual);
    }

    @Test
    @DisplayName("Validar que se devuelve una lista de Superheroe que estan en la base")
    public void whenFindAllReturnSuperHeroes(){
        List<SuperHeroe> superHeroes = utilsTest.buildSuperheroes();
        List<SuperHeroeDTO> superHeroesDTOMocks = utilsTest.buildSuperheroeDTOS();

        when(superHeroeRepository.findAll()).thenReturn(superHeroes);
        when(superHeroMapper.toDTO(superHeroes)).thenReturn(superHeroesDTOMocks);

        List<SuperHeroeDTO> superHeroeDTOS = superHeroeService.findAll();
        assertEquals(superHeroeDTOS.size(), 2);
        assertAll(() -> assertEquals(superHeroeDTOS.get(0).getId(), superHeroesDTOMocks.get(0).getId()),
                () -> assertEquals(superHeroeDTOS.get(0).getBirthday(), superHeroesDTOMocks.get(0).getBirthday()),
                () -> assertEquals(superHeroeDTOS.get(0).getName(), superHeroesDTOMocks.get(0).getName()),
                () -> assertEquals(superHeroeDTOS.get(1).getId(), superHeroesDTOMocks.get(1).getId()),
                () -> assertEquals(superHeroeDTOS.get(1).getBirthday(), superHeroesDTOMocks.get(1).getBirthday()),
                () -> assertEquals(superHeroeDTOS.get(1).getName(), superHeroesDTOMocks.get(1).getName()));
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
        List<SuperHeroe> superHeroes = utilsTest.buildSuperheroes();
        List<SuperHeroeDTO> superHeroesDTOMocks = utilsTest.buildSuperheroeDTOS();
        when(superHeroeRepository.findByNameLike(LIKE.replace("#",name.toUpperCase()))).thenReturn(superHeroes);
        when(superHeroMapper.toDTO(superHeroes)).thenReturn(superHeroesDTOMocks);

        List<SuperHeroeDTO> superHeroeDTOS = superHeroeService.findByNameLike(name);
        assertEquals(superHeroes.size(), 2);
        assertAll(() -> assertEquals(superHeroeDTOS.get(0).getId(), superHeroesDTOMocks.get(0).getId()),
                () -> assertEquals(superHeroeDTOS.get(0).getBirthday(), superHeroesDTOMocks.get(0).getBirthday()),
                () -> assertEquals(superHeroeDTOS.get(0).getName(), superHeroesDTOMocks.get(0).getName()),
                () -> assertEquals(superHeroeDTOS.get(1).getId(), superHeroesDTOMocks.get(1).getId()),
                () -> assertEquals(superHeroeDTOS.get(1).getBirthday(), superHeroesDTOMocks.get(1).getBirthday()),
                () -> assertEquals(superHeroeDTOS.get(1).getName(), superHeroesDTOMocks.get(1).getName()));

    }

    @Test
    @DisplayName("Validar que se devuelve una lista vacia de Superheroe con el nombre pasado por parametro")
    public void whenFindNameLikeReturnEmpty(){
        String name = "man";
        when(superHeroeRepository.findByNameLike(LIKE.replace("#",name.toUpperCase()))).thenReturn(List.of());

        List<SuperHeroeDTO> superHeroes = superHeroeService.findByNameLike(name);
        assertEquals(superHeroes.size(), 0);

    }

    @Test
    @DisplayName("Validar que cuando se pasa un dto para modificar el registro se hace correctamente")
    public void whenUpdateIsOk(){
        SuperHeroeDTO superHeroeDTO = utilsTest.buildSuperHeroeDTO();
        SuperHeroe superHeroeMockOld = utilsTest.buildSuperHeroe();

        when(superHeroeRepository.findById(superHeroeDTO.getId())).thenReturn(Optional.of(superHeroeMockOld));
        superHeroeMockOld.setName("SUPERMANCITO");

        when(superHeroeRepository.save(superHeroeMockOld)).thenReturn(superHeroeMockOld);

        superHeroeService.update(superHeroeDTO);

        verify(superHeroeRepository).findById(superHeroeDTO.getId());
        verify(superHeroeRepository).save(superHeroeMockOld);
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
        String expected = NOT_EXIST_SUPER_HEROE_MESSAGE.replace("#","1");
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
        String expected = NOT_EXIST_SUPER_HEROE_MESSAGE.replace("#","1");
        assertEquals(expected, actual);
    }

}
