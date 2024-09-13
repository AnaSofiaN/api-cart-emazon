package com.emazon.api_cart_emazon.adapters.driving.htpp;

import com.emazon.api_cart_emazon.domain.api.IStockServicePort;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class StockServiceAdapter implements IStockServicePort {

    private final RestTemplate restTemplate;
    private final String stockServiceUrl = "http://localhost:8090/api/v1/stock";

    public StockServiceAdapter(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    public int obtenerStock(Long articuloId) {
        String url = stockServiceUrl + "/cantidad/" + articuloId;
        return restTemplate.getForObject(url, Integer.class);
    }

    @Override
    public double obtenerPrecio(Long articuloId) {
        String url = stockServiceUrl + "/precio/" + articuloId;
        return restTemplate.getForObject(url, Double.class);
    }

    @Override
    public String obtenerCategoriaPorArticulo(Long articuloId) {
        String url = stockServiceUrl + "/categoria/" + articuloId;
        return restTemplate.getForObject(url, String.class);
    }

    @Override
    public void descontarStock(Long articuloId, int cantidad) {
        String url = stockServiceUrl + "/descontar/" + articuloId + "?cantidad=" + cantidad;
        restTemplate.postForObject(url, null, Void.class);
    }
}
