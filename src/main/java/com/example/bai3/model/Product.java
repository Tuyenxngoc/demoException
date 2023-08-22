package com.example.bai3.model;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "product")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @NotBlank(message = "Product name is required")
    @Column(unique = true)
    private String name;

    @Min(value = 0, message = "Price must be greater than or equal to 0")
    private double price;

    @NotNull
    @NotBlank(message = "Description is required")
    private String description;
}
