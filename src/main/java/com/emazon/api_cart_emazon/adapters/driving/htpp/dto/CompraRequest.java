package com.emazon.api_cart_emazon.adapters.driving.htpp.dto;

import com.emazon.api_cart_emazon.domain.model.ItemCarrito;

import java.time.LocalDate;
import java.util.List;

public class CompraRequest {
    private Long usuarioId;
    private List<ItemCarrito> items;
    private double total;
    private LocalDate fechaCompra;

    public CompraRequest(Long usuarioId, List<ItemCarrito> items, double total, LocalDate fechaCompra) {
        this.usuarioId = usuarioId;
        this.items = items;
        this.total = total;
        this.fechaCompra = fechaCompra;
    }
}
