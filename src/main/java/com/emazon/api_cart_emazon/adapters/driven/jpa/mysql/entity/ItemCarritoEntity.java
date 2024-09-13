package com.emazon.api_cart_emazon.adapters.driven.jpa.mysql.entity;


import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "item_carrito")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ItemCarritoEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "carrito_id", nullable = false)
    private CarritoEntity carrito;

    @Column(name = "articulo_id", nullable = false)
    private Long articuloId; // ID del artículo

    @Column(name = "cantidad", nullable = false)
    private int cantidad;

    @Column(name = "categoria", nullable = false)
    private String categoria;

    @Column(name = "fecha_abastecimiento")
    private LocalDate fechaAbastecimiento; // Fecha estimada de reabastecimiento si no hay stock
}

