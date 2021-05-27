package com.project.store.domain;

import lombok.Data;

import javax.persistence.*;

@Entity
@Data
@Table(name = "TTAG")
public class Tag {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
}
