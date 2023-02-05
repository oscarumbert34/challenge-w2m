package com.w2m.superheroe.services.mapper;

import com.w2m.superheroe.models.entities.SuperHeroe;
import com.w2m.superheroe.models.entities.dtos.SuperHeroeDTO;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE, uses = {})
public interface SuperHeroMapper {

    SuperHeroe toEntity(SuperHeroeDTO superHeroeDTO);

    SuperHeroeDTO toDTO(SuperHeroe superHeroe);

    List<SuperHeroe> toEntity(List<SuperHeroeDTO> superHeroeDTO);

    List<SuperHeroeDTO> toDTO(List<SuperHeroe> superHeroe);
}
