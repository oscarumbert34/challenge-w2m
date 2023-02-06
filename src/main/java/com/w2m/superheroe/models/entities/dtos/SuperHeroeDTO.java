package com.w2m.superheroe.models.entities.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SuperHeroeDTO {

    private Long id;
    private String name;
    private LocalDate birthday;
}
