package com.w2m.superheroe.services.domains;

import com.w2m.superheroe.models.entities.dtos.SuperHeroeDTO;

public interface SuperHeroeService {
    SuperHeroeDTO findById(Long id);
}
