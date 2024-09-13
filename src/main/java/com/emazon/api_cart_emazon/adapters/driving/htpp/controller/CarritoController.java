package com.emazon.api_cart_emazon.adapters.driving.htpp.controller;


import com.emazon.api_cart_emazon.domain.api.usecase.CarritoUseCase;
import com.emazon.api_cart_emazon.domain.model.ItemCarrito;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/carrito")
@RequiredArgsConstructor
public class CarritoController {

    private final CarritoUseCase carritoUseCase;

    // Consultar artículos en el carrito
    @GetMapping("/{usuarioId}")
    public ResponseEntity<List<ItemCarrito>> consultarCarrito(
            @PathVariable Long usuarioId,
            @RequestParam(required = false) String orden,
            @RequestParam(required = false) String categoria,
            @RequestParam(required = false) String marca,
            @RequestParam(defaultValue = "0") int pagina,
            @RequestParam(defaultValue = "10") int tamano
    ) {
        List<ItemCarrito> items = carritoUseCase.consultarCarrito(usuarioId, orden, categoria, marca, pagina, tamano);
        return new ResponseEntity<>(items, HttpStatus.OK);
    }

    // Agregar un artículo al carrito
    @PostMapping("/{usuarioId}/agregar")
    public ResponseEntity<Void> agregarArticulo(
            @PathVariable Long usuarioId,
            @RequestParam Long articuloId,
            @RequestParam int cantidad
    ) {
        carritoUseCase.addArticulo(usuarioId, articuloId, cantidad);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    // Eliminar un artículo del carrito
    @DeleteMapping("/{usuarioId}/eliminar/{articuloId}")
    public ResponseEntity<Void> eliminarArticulo(
            @PathVariable Long usuarioId,
            @PathVariable Long articuloId
    ) {
        carritoUseCase.removeArticulo(usuarioId, articuloId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    // Comprar los artículos en el carrito
    @PostMapping("/{usuarioId}/comprar")
    public ResponseEntity<Void> comprarCarrito(@PathVariable Long usuarioId) {
        carritoUseCase.comprar(usuarioId);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
