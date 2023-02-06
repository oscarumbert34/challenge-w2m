package com.w2m.superheroe.controllers;

import com.w2m.superheroe.models.entities.SuperHeroe;
import com.w2m.superheroe.models.entities.dtos.SuperHeroeDTO;
import com.w2m.superheroe.services.domains.SuperHeroeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static com.w2m.superheroe.utils.Messages.DELETE_SUPER_HEROE_OK;
import static com.w2m.superheroe.utils.Messages.UPDATE_SUPER_HEROE_OK;

@RestController
@RequestMapping("/super-heroes")
public class SuperHeroeController {

    @Autowired
    private SuperHeroeService superHeroeService;

    @GetMapping(value="/{id}", produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<SuperHeroeDTO> getSuperHeroe(@PathVariable("id")Long id){
        return ResponseEntity.status(HttpStatus.OK).body(superHeroeService.findById(id));
    }

    @GetMapping(produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<List<SuperHeroeDTO>> getAll(){
        return ResponseEntity.status(HttpStatus.OK).body(superHeroeService.findAll());
    }

    @GetMapping(value="/name/{name}",produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<List<SuperHeroeDTO>> getByNameLike(@PathVariable("name")String name){
        List<SuperHeroeDTO> superHeroeDTOS = superHeroeService.findByNameLike(name);
        return ResponseEntity.status(HttpStatus.OK).body(superHeroeDTOS);
    }

    @PatchMapping(produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<String> update(@RequestBody @Validated SuperHeroeDTO superHeroeDTO){
        superHeroeService.update(superHeroeDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(UPDATE_SUPER_HEROE_OK);
    }

    @DeleteMapping(value="/{id}",produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<String> delete(@PathVariable("id")Long id){
        superHeroeService.delete(id);
        return ResponseEntity.status(HttpStatus.OK).body(DELETE_SUPER_HEROE_OK);
    }


}
