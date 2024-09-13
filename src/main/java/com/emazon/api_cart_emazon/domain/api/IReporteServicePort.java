package com.emazon.api_cart_emazon.domain.api;

import com.emazon.api_cart_emazon.domain.model.ItemCarrito;

import java.time.LocalDate;
import java.util.List;

public interface IReporteServicePort {
    void registrarCompra(Long usuarioId, List<ItemCarrito> items, double total, LocalDate fechaCompra);
}
