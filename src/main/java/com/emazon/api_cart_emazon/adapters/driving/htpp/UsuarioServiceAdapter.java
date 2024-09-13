package com.emazon.api_cart_emazon.adapters.driving.htpp;

import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class UsuarioServiceAdapter {

    private final RestTemplate restTemplate;
    private final String usuarioServiceUrl = "http://localhost:8070/usuarios";

    public UsuarioServiceAdapter(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public String getRolByUsuarioId(Long usuarioId) {
        String url = usuarioServiceUrl + "/rol/" + usuarioId;
        return restTemplate.getForObject(url, String.class);
    }
}
