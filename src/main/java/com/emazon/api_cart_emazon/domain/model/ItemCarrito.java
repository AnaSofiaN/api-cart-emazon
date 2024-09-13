package com.emazon.api_cart_emazon.domain.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ItemCarrito {

    private Long id;
    private Long articuloId;
    private int cantidad;
    private String categoria;
    private LocalDate fechaAbastecimiento; // Fecha estimada de reabastecimiento
}
