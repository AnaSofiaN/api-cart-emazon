package com.emazon.api_cart_emazon.domain.spi;

import com.emazon.api_cart_emazon.domain.model.Carrito;

public interface ICarritoPersistencePort {
    Carrito saveCarrito(Carrito carrito);

    Carrito getCarritoByUsuarioId(Long usuarioId);
    void vaciarCarrito(Long usuarioId);
}
