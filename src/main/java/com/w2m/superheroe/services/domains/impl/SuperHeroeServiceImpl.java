package com.w2m.superheroe.services.domains.impl;

import com.w2m.superheroe.exceptions.SuperHeroeException;
import com.w2m.superheroe.models.entities.SuperHeroe;
import com.w2m.superheroe.models.entities.dtos.SuperHeroeDTO;
import com.w2m.superheroe.repositories.SuperHeroeRepository;
import com.w2m.superheroe.services.domains.SuperHeroeService;
import com.w2m.superheroe.services.mapper.SuperHeroMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.w2m.superheroe.utils.Messages.NOT_EXIST_SUPER_HEROE_MESSAGE;

@Service
@Slf4j
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
    @Cacheable(cacheNames="superHeroeDTO", key = "#id")
    public SuperHeroeDTO findById(Long id) {
        log.info("Ejecutando SuperHeroeServiceImpl findById");
        SuperHeroeDTO superHeroeDTO =  superHeroeRepository.findById(id)
                .map(superHeroMapper::toDTO)
                .orElseThrow(() -> new SuperHeroeException(NOT_EXIST_SUPER_HEROE_MESSAGE.replace("#", id.toString())));
        return superHeroeDTO;

    }

    @Override
    @Cacheable("superHeroeDTOS")
    public List<SuperHeroeDTO> findAll() {
        log.info("Ejecutando SuperHeroeServiceImpl findByAll");
        return superHeroMapper.toDTO(superHeroeRepository.findAll());
    }

    @Override
    @Cacheable(cacheNames="superHeroeDTOSName", key = "#name")
    public List<SuperHeroeDTO> findByNameLike(String name) {
        log.info("Ejecutando SuperHeroeServiceImpl findByNameLike");
        return superHeroMapper.toDTO(superHeroeRepository.findByNameLike(LIKE.replace("#",name.toUpperCase())));
    }

    @Override
    @CacheEvict(cacheNames="superHeroeDTO", key="#superHeroeDTO.id" )
    public void update(SuperHeroeDTO superHeroeDTO) {
        SuperHeroe superHeroe = superHeroeRepository.findById(superHeroeDTO.getId())
                .orElseThrow(() -> new SuperHeroeException(NOT_EXIST_SUPER_HEROE_MESSAGE.replace("#", superHeroeDTO.getId().toString())));
        superHeroe.setName(superHeroeDTO.getName());
        superHeroe.setBirthday(superHeroeDTO.getBirthday());

        superHeroeRepository.save(superHeroe);
    }

    @Override
    @Caching(evict = {
            @CacheEvict(cacheNames = "superHeroeDTO", key="#id" ),
            @CacheEvict(cacheNames = "superHeroeDTOS", allEntries = true),
            @CacheEvict(cacheNames = "superHeroeDTOSName", allEntries = true)})
    public void delete(Long id) {
        SuperHeroe superHeroe = superHeroeRepository.findById(id)
                .orElseThrow(() -> new SuperHeroeException(NOT_EXIST_SUPER_HEROE_MESSAGE.replace("#", id.toString())));
        superHeroeRepository.delete(superHeroe);
    }
}
