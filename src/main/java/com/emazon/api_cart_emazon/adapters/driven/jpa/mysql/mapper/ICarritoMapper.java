package com.emazon.api_cart_emazon.adapters.driven.jpa.mysql.mapper;

import com.emazon.api_cart_emazon.adapters.driven.jpa.mysql.entity.*;
import com.emazon.api_cart_emazon.domain.model.*;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ICarritoMapper {

    @Mappings({
            @Mapping(target = "items", source = "items")
    })
    Carrito toModel(CarritoEntity carritoEntity);

    @Mappings({
            @Mapping(target = "items", source = "items")
    })
    CarritoEntity toEntity(Carrito carrito);

    @Mapping(target = "categoria", source = "categoria")
    ItemCarrito toModel(ItemCarritoEntity itemCarritoEntity);

    @Mapping(target = "categoria", source = "categoria")
    ItemCarritoEntity toEntity(ItemCarrito itemCarrito);

    List<ItemCarrito> toModelList(List<ItemCarritoEntity> itemCarritoEntities);
    List<ItemCarritoEntity> toEntityList(List<ItemCarrito> itemCarritos);
}
