package com.w2m.superheroe.services.domains.impl;

import com.w2m.superheroe.models.entities.dtos.SuperHeroeDTO;
import com.w2m.superheroe.services.domains.SuperHeroeService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SuperHeroeServiceImpl implements SuperHeroeService {
    @Override
    public SuperHeroeDTO findById(Long id) {
        return null;
    }

    @Override
    public List<SuperHeroeDTO> findAll() {
        return null;
    }

    @Override
    public List<SuperHeroeDTO> findByNameLike(String name) {
        return null;
    }

    @Override
    public void update(SuperHeroeDTO superHeroeDTO) {

    }

    @Override
    public void delete(Long id) {

    }
}
