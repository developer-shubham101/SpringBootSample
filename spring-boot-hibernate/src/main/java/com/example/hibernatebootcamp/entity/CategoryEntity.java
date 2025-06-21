package com.example.hibernatebootcamp.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.HashSet;
import java.util.Set;


@Data
@Entity
@Table(name = "categories", indexes = {
        @Index(name = "idx_category_name", columnList = "name")
})
public class CategoryEntity {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @Column(unique = true, nullable = false)
    private String name;

    @ManyToMany(mappedBy = "categories")
    private Set<BlogEntity> blogEntities = new HashSet<>();

} 