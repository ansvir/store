package com.project.store.domain;

import lombok.Data;

import javax.persistence.*;
import java.util.Set;

@Data
@Entity
@Table(name = "TFORCE_UPDATE")
public class ForceUpdate {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long productId;
    private String productName;
    private String productDescription;
    @ManyToMany
    private Set<Tag> productTags;

}
