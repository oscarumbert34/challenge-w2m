package com.w2m.superheroe.models.entities.dtos;

import lombok.*;

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
