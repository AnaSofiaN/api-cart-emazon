package com.emazon.api_cart_emazon.domain.util;

public class ErrorConstants {

    private ErrorConstants() {}

    public static final String NOT_EXIST_CART = "El carrito del usuario no existe.";
    public static final String NOT_EXIST_USER_BY_ROL = "El usuario no tiene el rol adecuado para agregar artículos al carrito.";
    public static final String NOT_STOCK = "No hay suficiente stock para el artículo ";
    public static final String LIMIT_ARTICLES = "No se puede agregar más de 3 artículos por categoría.";
}
