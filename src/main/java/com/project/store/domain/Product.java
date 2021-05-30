package com.project.store.domain;

import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Set;

@Entity
@Data
@Table(name = "TPRODUCT")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotBlank
    private String name;
    @Column(length = 2000)
    @NotBlank
    @Size(max = 2000)
    private String description;
    @ManyToMany
    @NotEmpty
    private Set<Tag> tags;
}
