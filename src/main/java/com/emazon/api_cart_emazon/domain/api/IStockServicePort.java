package com.emazon.api_cart_emazon.domain.api;

public interface IStockServicePort {
    void descontarStock(Long articuloId, int cantidad);

    int obtenerStock(Long articuloId);

    double obtenerPrecio(Long articuloId);

    String obtenerCategoriaPorArticulo(Long articuloId);
}
