package com.w2m.superheroe.services.domains;

import com.w2m.superheroe.UtilsTest;
import com.w2m.superheroe.exceptions.SuperHeroeException;
import com.w2m.superheroe.models.entities.SuperHeroe;
import com.w2m.superheroe.models.entities.dtos.SuperHeroeDTO;
import com.w2m.superheroe.repositories.SuperHeroeRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static com.w2m.superheroe.utils.Messages.NOT_EXIST_SUPER_HEROE_MESSAGE;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;


@SpringBootTest
public class SuperHeroeServiceIntegrationTest {

    @Autowired
    private SuperHeroeService superHeroeService;

    @Autowired
    private SuperHeroeRepository superHeroeRepository;

    private UtilsTest utilsTest = new UtilsTest();

    @Test
    @DisplayName("Validar que luego de crear un super heroes al buscarlo por el id este se retorna")
    void whenFindByIdExists(){
        SuperHeroe superHeroeFlash = superHeroeRepository.save(utilsTest.buildSuperHeroe(null,1950, "FLASH"));

        SuperHeroeDTO superHeroeDTO = superHeroeService.findById(superHeroeFlash.getId());
        assertAll(() -> assertEquals(superHeroeDTO.getId(), superHeroeFlash.getId()),
                () -> assertEquals(superHeroeDTO.getBirthday(), superHeroeFlash.getBirthday()),
                () -> assertEquals(superHeroeDTO.getName(), superHeroeFlash.getName()));
    }

    @Test
    @DisplayName("Validar que se arroja una excepcion cuando el superHeroe no existe")
    public void whenFindByNotExist(){

        Exception exception = assertThrows(SuperHeroeException.class, () -> {
            superHeroeService.findById(500L);
        });

        String actual = exception.getMessage();
        String expected = NOT_EXIST_SUPER_HEROE_MESSAGE.replace("#","500");
        assertEquals(expected, actual);
    }

    @Test
    @DisplayName("Validar que se devuelve una lista de Superheroe que estan en la base")
    public void whenFindAllReturnSuperHeroes(){
        superHeroeRepository.deleteAll();
        SuperHeroe superHeroeBatman = superHeroeRepository.save(utilsTest.buildSuperHeroe(null,1950, "BATMAN"));
        SuperHeroe superHeroeSpiderman = superHeroeRepository.save(utilsTest.buildSuperHeroe(null,1950, "SPIDERMAN"));

        List<SuperHeroeDTO> superHeroeDTOS = superHeroeService.findAll();
        assertEquals(superHeroeDTOS.size(), 2);
        assertAll(() -> assertEquals(superHeroeDTOS.get(0).getId(), superHeroeBatman.getId()),
                () -> assertEquals(superHeroeDTOS.get(0).getBirthday(), superHeroeBatman.getBirthday()),
                () -> assertEquals(superHeroeDTOS.get(0).getName(), superHeroeBatman.getName()),
                () -> assertEquals(superHeroeDTOS.get(1).getId(), superHeroeSpiderman.getId()),
                () -> assertEquals(superHeroeDTOS.get(1).getBirthday(), superHeroeSpiderman.getBirthday()),
                () -> assertEquals(superHeroeDTOS.get(1).getName(), superHeroeSpiderman.getName()));
    }

    @Test
    @DisplayName("Validar que se devuelve una lista de Superheroe vacia cuando no hay super heroes cargados en la base")
    public void whenFindAllReturnEmpty(){
        superHeroeRepository.deleteAll();

        List<SuperHeroeDTO> superHeroeDTOS = superHeroeService.findAll();
        assertEquals(superHeroeDTOS.size(), 0);
    }

    @Test
    @DisplayName("Validar que se devuelve una lista de Superheroe con el nombre pasado por parametro")
    public void whenFindNameLikeReturnSuperHeroes(){
        superHeroeRepository.deleteAll();
        SuperHeroe superHeroeSupermancito = superHeroeRepository.save(utilsTest.buildSuperHeroe(null,1950, "SUPERMANCITO"));
        superHeroeRepository.save(utilsTest.buildSuperHeroe(null,1950, "BATMAN"));

        List<SuperHeroeDTO> superHeroeDTOS = superHeroeService.findByNameLike("SUPERMANC");
        assertEquals(superHeroeDTOS.size(), 1);
        assertAll(() -> assertEquals(superHeroeDTOS.get(0).getId(), superHeroeSupermancito.getId()),
                () -> assertEquals(superHeroeDTOS.get(0).getBirthday(), superHeroeSupermancito.getBirthday()),
                () -> assertEquals(superHeroeDTOS.get(0).getName(), superHeroeSupermancito.getName()));

    }

    @Test
    @DisplayName("Validar que se devuelve una lista vacia de Superheroe con el nombre pasado por parametro")
    public void whenFindNameLikeReturnEmpty(){
        superHeroeRepository.deleteAll();
        superHeroeRepository.save(utilsTest.buildSuperHeroe(null,1950, "SUPERMANCITO"));

        List<SuperHeroeDTO> superHeroes = superHeroeService.findByNameLike("SPI");
        assertEquals(superHeroes.size(), 0);

    }

    @Test
    @DisplayName("Validar que cuando se pasa un dto para modificar el registro se hace correctamente")
    public void whenUpdateIsOk(){
        superHeroeRepository.deleteAll();
        SuperHeroe superHeroeSupermancito = superHeroeRepository.save(utilsTest.buildSuperHeroe(null,1950, "SUPERMANCITO"));

        SuperHeroeDTO superHeroeDTOOld = superHeroeService.findById(superHeroeSupermancito.getId());
        assertAll(() -> assertEquals(superHeroeDTOOld.getId(), superHeroeSupermancito.getId()),
                () -> assertEquals(superHeroeDTOOld.getBirthday(), superHeroeSupermancito.getBirthday()),
                () -> assertEquals(superHeroeDTOOld.getName(), superHeroeSupermancito.getName()));

        superHeroeDTOOld.setName("SUPERMANUPDATE");

        superHeroeService.update(superHeroeDTOOld);

        SuperHeroeDTO superHeroeDTONew = superHeroeService.findById(superHeroeSupermancito.getId());
        assertEquals(superHeroeDTONew.getName(), "SUPERMANUPDATE");
    }

    @Test
    @DisplayName("Validar que cuando se pasa un dto para modificar y este no existe se arroja una excepcion")
    public void whenUpdateFailBecauseSuperHeroeNotExist(){
        SuperHeroeDTO superHeroeDTO = utilsTest.buildSuperHeroeDTO(500L,2020,"prueba");

        Exception exception = assertThrows(SuperHeroeException.class, () -> {
            superHeroeService.update(superHeroeDTO);
        });

        String actual = exception.getMessage();
        String expected = NOT_EXIST_SUPER_HEROE_MESSAGE.replace("#","500");
        assertEquals(expected, actual);

    }

    @Test
    @DisplayName("Validar que cuando se pasa el identificador de un superheroe para eliminarlo y este existe se borra correctamente de la base")
    public void whenDeleteIsOk(){
        superHeroeRepository.deleteAll();
        SuperHeroe superHeroeSupermancito = superHeroeRepository.save(utilsTest.buildSuperHeroe(null,1950, "SUPERMANCITO"));

        superHeroeService.delete(superHeroeSupermancito.getId());

        Exception exception = assertThrows(SuperHeroeException.class, () -> {
            superHeroeService.findById(superHeroeSupermancito.getId());
        });

        String actual = exception.getMessage();
        String expected = NOT_EXIST_SUPER_HEROE_MESSAGE.replace("#",superHeroeSupermancito.getId().toString());
        assertEquals(expected, actual);

    }

    @Test
    @DisplayName("Validar que cuando se pasa el identificador de un superheroe para eliminarlo y este NO existe se arroja una excepcion")
    public void whenDeleteFailBecauseSuperHeroeNotExist(){

        Exception exception = assertThrows(SuperHeroeException.class, () -> {
            superHeroeService.delete(500L);
        });

        String actual = exception.getMessage();
        String expected = NOT_EXIST_SUPER_HEROE_MESSAGE.replace("#","500");
        assertEquals(expected, actual);
    }

}
