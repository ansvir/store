package com.project.store.dto;

import com.project.store.domain.Product;
import lombok.Data;

import java.util.Set;

@Data
public class CartDTO {
    private Long userId;
    private Set<Product> products;
}
