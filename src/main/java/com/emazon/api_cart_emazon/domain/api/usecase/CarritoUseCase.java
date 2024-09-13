package com.emazon.api_cart_emazon.domain.api.usecase;

import com.emazon.api_cart_emazon.domain.api.IUsuarioServicePort;
import com.emazon.api_cart_emazon.domain.exception.CarritoException;
import com.emazon.api_cart_emazon.domain.model.Carrito;
import com.emazon.api_cart_emazon.domain.model.ItemCarrito;
import com.emazon.api_cart_emazon.domain.spi.ICarritoPersistencePort;
import com.emazon.api_cart_emazon.domain.api.IReporteServicePort;
import com.emazon.api_cart_emazon.domain.api.IStockServicePort;
import com.emazon.api_cart_emazon.domain.util.ErrorConstants;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class CarritoUseCase {

    private final ICarritoPersistencePort carritoPersistencePort;
    private final IStockServicePort stockServicePort;
    private final IUsuarioServicePort usuarioServicePort;
    private final IReporteServicePort reporteServicePort;

    public List<ItemCarrito> consultarCarrito(Long usuarioId, String orden, String categoria, String marca, int pagina, int tamano) {
        Carrito carrito = carritoPersistencePort.getCarritoByUsuarioId(usuarioId);
        if (carrito == null) {
            throw new CarritoException(ErrorConstants.NOT_EXIST_CART);
        }

        List<ItemCarrito> items = carrito.getItems();

        // Filtrar por categoría
        if (categoria != null && !categoria.isEmpty()) {
            items = items.stream()
                    .filter(item -> item.getCategoria().equals(categoria))
                    .collect(Collectors.toList());
        }

        // Filtrar por marca (si tienes un atributo de marca en ItemCarrito, implementa aquí)
        if (marca != null && !marca.isEmpty()) {
            // Suponiendo que ItemCarrito tiene un campo de marca
            // items = items.stream()
            //        .filter(item -> item.getMarca().equals(marca))
            //        .collect(Collectors.toList());
        }

        // Ordenar
        if (orden != null) {
            if (orden.equalsIgnoreCase("asc")) {
                items = items.stream()
                        .sorted(Comparator.comparing(ItemCarrito::getArticuloId)) // Reemplazar por el campo adecuado
                        .collect(Collectors.toList());
            } else if (orden.equalsIgnoreCase("desc")) {
                items = items.stream()
                        .sorted(Comparator.comparing(ItemCarrito::getArticuloId).reversed()) // Reemplazar por el campo adecuado
                        .collect(Collectors.toList());
            }
        }

        // Paginación
        int fromIndex = Math.min(pagina * tamano, items.size());
        int toIndex = Math.min(fromIndex + tamano, items.size());
        return items.subList(fromIndex, toIndex);
    }

    @Transactional
    public void addArticulo(Long usuarioId, Long articuloId, int cantidad) {
        Carrito carrito = carritoPersistencePort.getCarritoByUsuarioId(usuarioId);
        if (carrito == null) {
            throw new CarritoException(ErrorConstants.NOT_EXIST_CART);
        }

        // Validar si el usuario tiene el rol correcto
        if (!isRolCliente(usuarioId)) {
            throw new CarritoException(ErrorConstants.NOT_EXIST_USER_BY_ROL);
        }

        // Validar stock
        int stockDisponible = stockServicePort.obtenerStock(articuloId);
        if (stockDisponible < cantidad) {
            throw new CarritoException(ErrorConstants.NOT_STOCK + articuloId);
        }

        // Verificar si el artículo ya está en el carrito
        ItemCarrito itemExistente = carrito.getItems().stream()
                .filter(item -> item.getArticuloId().equals(articuloId))
                .findFirst()
                .orElse(null);

        if (itemExistente != null) {
            // Actualizar cantidad si ya existe
            itemExistente.setCantidad(itemExistente.getCantidad() + cantidad);
        } else {
            // Añadir nuevo artículo
            ItemCarrito nuevoItem = new ItemCarrito();
            nuevoItem.setArticuloId(articuloId);
            nuevoItem.setCantidad(cantidad);
            nuevoItem.setCategoria(getCategoriaByArticulo(articuloId)); // Añadir categoría
            carrito.getItems().add(nuevoItem);
        }

        // Validar cantidad máxima por categoría
        long countByCategoria = carrito.getItems().stream()
                .filter(item -> item.getCategoria().equals(getCategoriaByArticulo(articuloId)))
                .count();

        if (countByCategoria > 3) {
            throw new CarritoException(ErrorConstants.LIMIT_ARTICLES);
        }

        carrito.setFechaModificacion(LocalDateTime.now());
        carritoPersistencePort.saveCarrito(carrito);
    }

    @Transactional
    public void removeArticulo(Long usuarioId, Long articuloId) {
        Carrito carrito = carritoPersistencePort.getCarritoByUsuarioId(usuarioId);
        if (carrito == null) {
            throw new CarritoException(ErrorConstants.NOT_EXIST_CART);
        }

        // Eliminar el artículo del carrito
        carrito.getItems().removeIf(item -> item.getArticuloId().equals(articuloId));

        carrito.setFechaModificacion(LocalDateTime.now());
        carritoPersistencePort.saveCarrito(carrito);
    }

    @Transactional
    public void comprar(Long usuarioId) {
        Carrito carrito = carritoPersistencePort.getCarritoByUsuarioId(usuarioId);

        if (carrito == null || carrito.getItems().isEmpty()) {
            throw new CarritoException(ErrorConstants.NOT_EXIST_CART);
        }

        double total = 0.0;
        for (ItemCarrito item : carrito.getItems()) {
            // Verificar stock
            int stockDisponible = stockServicePort.obtenerStock(item.getArticuloId());
            if (stockDisponible < item.getCantidad()) {
                throw new CarritoException("No hay suficiente stock para el artículo " + item.getArticuloId());
            }

            // Descontar stock
            stockServicePort.descontarStock(item.getArticuloId(), item.getCantidad());

            // Calcular precio total
            total += calcularPrecio(item.getArticuloId(), item.getCantidad());
        }

        // Registrar compra
        LocalDate fechaCompra = LocalDate.now();
        reporteServicePort.registrarCompra(usuarioId, carrito.getItems(), total, fechaCompra);

        // Vaciar el carrito
        carritoPersistencePort.vaciarCarrito(usuarioId);
    }

    private boolean isRolCliente(Long usuarioId) {
        String rol = usuarioServicePort.getRolByUsuarioId(usuarioId);
        return "cliente".equalsIgnoreCase(rol);
    }

    private String getCategoriaByArticulo(Long articuloId) {
        // Supongamos que obtienes la categoría del stockServicePort
        return stockServicePort.obtenerCategoriaPorArticulo(articuloId);
    }

    private double calcularPrecio(Long articuloId, int cantidad) {
        // Implementar lógica para obtener el precio del artículo y calcular el total
        double precio = stockServicePort.obtenerPrecio(articuloId);
        return precio * cantidad;
    }
}
