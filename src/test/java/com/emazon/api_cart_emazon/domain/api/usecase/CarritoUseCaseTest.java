package com.emazon.api_cart_emazon.domain.api.usecase;

import com.emazon.api_cart_emazon.domain.api.IReporteServicePort;
import com.emazon.api_cart_emazon.domain.api.IStockServicePort;
import com.emazon.api_cart_emazon.domain.api.IUsuarioServicePort;
import com.emazon.api_cart_emazon.domain.exception.CarritoException;
import com.emazon.api_cart_emazon.domain.model.Carrito;
import com.emazon.api_cart_emazon.domain.model.ItemCarrito;
import com.emazon.api_cart_emazon.domain.spi.ICarritoPersistencePort;
import com.emazon.api_cart_emazon.domain.util.ErrorConstants;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class CarritoUseCaseTest {

    @Mock
    private ICarritoPersistencePort carritoPersistencePort;
    @Mock
    private IStockServicePort stockServicePort;
    @Mock
    private IUsuarioServicePort usuarioServicePort;
    @Mock
    private IReporteServicePort reporteServicePort;

    @InjectMocks
    private CarritoUseCase carritoUseCase;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void consultarCarrito_CuandoCarritoNoExiste_LanzaExcepcion() {
        when(carritoPersistencePort.getCarritoByUsuarioId(anyLong())).thenReturn(null);

        CarritoException exception = assertThrows(CarritoException.class, () -> {
            carritoUseCase.consultarCarrito(1L, null, null, null, 0, 10);
        });

        assertEquals(ErrorConstants.NOT_EXIST_CART, exception.getMessage());
    }

    @Test
    void consultarCarrito_DevuelveItemsFiltradosYOrdenados() {
        Carrito carrito = new Carrito();
        List<ItemCarrito> items = new ArrayList<>();
        items.add(new ItemCarrito(1L, 2L, 3, "categoria1", null));
        items.add(new ItemCarrito(2L, 3L, 4, "categoria2", null));
        carrito.setItems(items);

        when(carritoPersistencePort.getCarritoByUsuarioId(anyLong())).thenReturn(carrito);

        List<ItemCarrito> result = carritoUseCase.consultarCarrito(1L, "asc", "categoria1", null, 0, 10);

        assertEquals(1, result.size());
        assertEquals(2L, result.get(0).getArticuloId());
    }

    @Test
    void addArticulo_CuandoNoExisteCarrito_LanzaExcepcion() {
        when(carritoPersistencePort.getCarritoByUsuarioId(anyLong())).thenReturn(null);

        CarritoException exception = assertThrows(CarritoException.class, () -> {
            carritoUseCase.addArticulo(1L, 1L, 5);
        });

        assertEquals(ErrorConstants.NOT_EXIST_CART, exception.getMessage());
    }

    @Test
    void addArticulo_ValidaStockYActualizaCarrito() {
        Carrito carrito = new Carrito();
        List<ItemCarrito> items = new ArrayList<>();
        carrito.setItems(items);

        when(carritoPersistencePort.getCarritoByUsuarioId(anyLong())).thenReturn(carrito);
        when(usuarioServicePort.getRolByUsuarioId(anyLong())).thenReturn("cliente");
        when(stockServicePort.obtenerStock(anyLong())).thenReturn(10);
        when(stockServicePort.obtenerCategoriaPorArticulo(anyLong())).thenReturn("categoria1");

        carritoUseCase.addArticulo(1L, 1L, 2);

        assertEquals(1, carrito.getItems().size());
        verify(carritoPersistencePort, times(1)).saveCarrito(any(Carrito.class));
    }

    @Test
    void removeArticulo_CuandoArticuloExiste_EliminaArticulo() {
        Carrito carrito = new Carrito();
        List<ItemCarrito> items = new ArrayList<>();
        items.add(new ItemCarrito(1L, 1L, 1, "categoria1", null));
        carrito.setItems(items);

        when(carritoPersistencePort.getCarritoByUsuarioId(anyLong())).thenReturn(carrito);

        carritoUseCase.removeArticulo(1L, 1L);

        assertTrue(carrito.getItems().isEmpty());
        verify(carritoPersistencePort, times(1)).saveCarrito(any(Carrito.class));
    }

    @Test
    void comprar_CuandoStockSuficiente_RegistraCompraYVaciaCarrito() {
        Carrito carrito = new Carrito();
        List<ItemCarrito> items = new ArrayList<>();
        items.add(new ItemCarrito(1L, 1L, 2, "categoria1", null));
        carrito.setItems(items);

        when(carritoPersistencePort.getCarritoByUsuarioId(anyLong())).thenReturn(carrito);
        when(stockServicePort.obtenerStock(anyLong())).thenReturn(10);
        when(stockServicePort.obtenerPrecio(anyLong())).thenReturn(100.0);

        carritoUseCase.comprar(1L);

        verify(stockServicePort, times(1)).descontarStock(1L, 2);
        verify(reporteServicePort, times(1)).registrarCompra(anyLong(), anyList(), anyDouble(), any());
        verify(carritoPersistencePort, times(1)).vaciarCarrito(anyLong());
    }

    @Test
    void comprar_CuandoNoHayStockSuficiente_LanzaExcepcion() {
        Carrito carrito = new Carrito();
        List<ItemCarrito> items = new ArrayList<>();
        items.add(new ItemCarrito(1L, 1L, 5, "categoria1", null));
        carrito.setItems(items);

        when(carritoPersistencePort.getCarritoByUsuarioId(anyLong())).thenReturn(carrito);
        when(stockServicePort.obtenerStock(anyLong())).thenReturn(2);

        CarritoException exception = assertThrows(CarritoException.class, () -> {
            carritoUseCase.comprar(1L);
        });

        assertEquals("No hay suficiente stock para el artículo 1", exception.getMessage());
    }
}
