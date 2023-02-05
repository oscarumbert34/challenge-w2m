package com.w2m.superheroe.services.domains;

import com.w2m.superheroe.models.entities.dtos.SuperHeroeDTO;

import java.util.List;

public interface SuperHeroeService {
    SuperHeroeDTO findById(Long id);
    List<SuperHeroeDTO> findAll();
    List<SuperHeroeDTO> findByNameLike(String name);
    void update(SuperHeroeDTO superHeroeDTO);
    void delete(Long id);
}
