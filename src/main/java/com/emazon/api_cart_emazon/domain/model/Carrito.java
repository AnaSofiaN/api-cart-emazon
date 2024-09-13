package com.emazon.api_cart_emazon.domain.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Carrito {

    private Long id;
    private Long usuarioId; // ID del usuario
    private List<ItemCarrito> items;
    private LocalDateTime fechaModificacion;
}
