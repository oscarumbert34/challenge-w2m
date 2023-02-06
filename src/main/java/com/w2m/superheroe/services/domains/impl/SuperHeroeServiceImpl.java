package com.w2m.superheroe.services.domains.impl;

import com.w2m.superheroe.exceptions.SuperHeroeException;
import com.w2m.superheroe.models.entities.SuperHeroe;
import com.w2m.superheroe.models.entities.dtos.SuperHeroeDTO;
import com.w2m.superheroe.repositories.SuperHeroeRepository;
import com.w2m.superheroe.services.domains.SuperHeroeService;
import com.w2m.superheroe.services.mapper.SuperHeroMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.w2m.superheroe.utils.Messages.NOT_EXIST_SUPER_HEROE_MESSAGE;

@Service
public class SuperHeroeServiceImpl implements SuperHeroeService {

    public static final String LIKE = "%#%";
    private SuperHeroeRepository superHeroeRepository;

    private SuperHeroMapper superHeroMapper;

    @Autowired
    public SuperHeroeServiceImpl(SuperHeroeRepository superHeroeRepository,
                                 SuperHeroMapper superHeroMapper){
        this.superHeroeRepository = superHeroeRepository;
        this.superHeroMapper = superHeroMapper;
    }
    @Override
    public SuperHeroeDTO findById(Long id) {
        return superHeroeRepository.findById(id)
                .map(superHeroMapper::toDTO)
                .orElseThrow(() -> new SuperHeroeException(NOT_EXIST_SUPER_HEROE_MESSAGE.replace("#", id.toString())));
    }

    @Override
    public List<SuperHeroeDTO> findAll() {
        return superHeroMapper.toDTO(superHeroeRepository.findAll());
    }

    @Override
    public List<SuperHeroeDTO> findByNameLike(String name) {
        return superHeroMapper.toDTO(superHeroeRepository.findByNameLike(LIKE.replace("#",name.toUpperCase())));
    }

    @Override
    public void update(SuperHeroeDTO superHeroeDTO) {
        SuperHeroe superHeroe = superHeroeRepository.findById(superHeroeDTO.getId())
                .orElseThrow(() -> new SuperHeroeException(NOT_EXIST_SUPER_HEROE_MESSAGE.replace("#", superHeroeDTO.getId().toString())));
        superHeroe.setName(superHeroeDTO.getName());
        superHeroe.setBirthday(superHeroeDTO.getBirthday());

        superHeroeRepository.save(superHeroe);
    }

    @Override
    public void delete(Long id) {
        SuperHeroe superHeroe = superHeroeRepository.findById(id)
                .orElseThrow(() -> new SuperHeroeException(NOT_EXIST_SUPER_HEROE_MESSAGE.replace("#", id.toString())));
        superHeroeRepository.delete(superHeroe);
    }
}
