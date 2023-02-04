package com.w2m.superheroe.models.entities;

import lombok.*;

import javax.persistence.*;
import java.time.Instant;
import java.time.LocalDate;

@Getter
@Setter
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "super_heroe")
public class SuperHeroe {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "birthday")
    private LocalDate birthday;

    @Column(name = "creation_date")
    private Instant creationDate;

    @Column(name = "update_date")
    private Instant updateDate;
}
