package com.emazon.api_cart_emazon.adapters.driven.jpa.mysql.adapter;

import com.emazon.api_cart_emazon.adapters.driven.jpa.mysql.entity.CarritoEntity;
import com.emazon.api_cart_emazon.adapters.driven.jpa.mysql.exception.CarritoExceptionAdapter;
import com.emazon.api_cart_emazon.adapters.driven.jpa.mysql.mapper.ICarritoMapper;
import com.emazon.api_cart_emazon.adapters.driven.jpa.mysql.repository.ICarritoRepository;
import com.emazon.api_cart_emazon.domain.model.Carrito;
import com.emazon.api_cart_emazon.domain.spi.ICarritoPersistencePort;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@RequiredArgsConstructor
public class CarritoPersistenceAdapter implements ICarritoPersistencePort {
    private final ICarritoRepository carritoRepository;
    private final ICarritoMapper carritoMapper;

    @Override
    public Carrito saveCarrito(Carrito carrito) {
        CarritoEntity carritoEntity = carritoMapper.toEntity(carrito);
        return carritoMapper.toModel(carritoRepository.save(carritoEntity));
    }

    @Override
    public Carrito getCarritoByUsuarioId(Long usuarioId) {
        return carritoRepository.findByUsuarioId(usuarioId)
                .map(carritoMapper::toModel)
                .orElse(null);
    }

    @Override
    public void vaciarCarrito(Long usuarioId) {
        CarritoEntity carritoEntity = carritoRepository.findByUsuarioId(usuarioId)
                .orElseThrow(() -> new CarritoExceptionAdapter("Carrito no encontrado para el usuario con ID: " + usuarioId));

        carritoEntity.getItems().clear(); // Limpiar los ítems del carrito
        carritoEntity.setFechaModificacion(LocalDateTime.now()); // Actualizar fecha de modificación
        carritoRepository.save(carritoEntity); // Guardar los cambios
    }
}
