package com.emazon.api_cart_emazon.adapters.driving.htpp;

import com.emazon.api_cart_emazon.adapters.driving.htpp.dto.CompraRequest;
import com.emazon.api_cart_emazon.domain.api.IReporteServicePort;
import com.emazon.api_cart_emazon.domain.model.ItemCarrito;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.util.List;

@Component
public class ReporteServiceAdapter implements IReporteServicePort {

    private final RestTemplate restTemplate;
    private final String reporteServiceUrl = "http://localhost:8088/api/reportes";

    public ReporteServiceAdapter(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    public void registrarCompra(Long usuarioId, List<ItemCarrito> items, double total, LocalDate fechaCompra) {
        String url = reporteServiceUrl + "/compras";
        CompraRequest request = new CompraRequest(usuarioId, items, total, fechaCompra);
        restTemplate.postForObject(url, request, Void.class);
    }
}
