package com.project.store.domain;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "TFORCE_UPDATE")
public class ForceUpdate {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @OneToOne
    private Product product;
}
