package com.emazon.api_cart_emazon.adapters.driven.jpa.mysql.repository;

import com.emazon.api_cart_emazon.adapters.driven.jpa.mysql.entity.CarritoEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ICarritoRepository extends JpaRepository<CarritoEntity, Long> {

    Optional<CarritoEntity> findByUsuarioId(Long usuarioId);
}
