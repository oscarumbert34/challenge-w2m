package com.w2m.superheroe.repositories;

import com.w2m.superheroe.models.entities.SuperHeroe;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SuperHeroeRepository extends JpaRepository<SuperHeroe, Long> {

    List<SuperHeroe> findByNameLike(String name);
}
