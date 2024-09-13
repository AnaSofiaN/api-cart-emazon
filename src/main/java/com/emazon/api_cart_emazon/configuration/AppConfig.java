package com.emazon.api_cart_emazon.configuration;
import com.emazon.api_cart_emazon.adapters.driven.jpa.mysql.adapter.CarritoPersistenceAdapter;
import com.emazon.api_cart_emazon.adapters.driven.jpa.mysql.mapper.ICarritoMapper;
import com.emazon.api_cart_emazon.adapters.driven.jpa.mysql.repository.ICarritoRepository;
import com.emazon.api_cart_emazon.adapters.driving.htpp.ReporteServiceAdapter;
import com.emazon.api_cart_emazon.adapters.driving.htpp.StockServiceAdapter;
import com.emazon.api_cart_emazon.adapters.driving.htpp.UsuarioServiceAdapter;
import com.emazon.api_cart_emazon.domain.api.IReporteServicePort;
import com.emazon.api_cart_emazon.domain.api.IStockServicePort;
import com.emazon.api_cart_emazon.domain.api.IUsuarioServicePort;
import com.emazon.api_cart_emazon.domain.api.usecase.CarritoUseCase;
import com.emazon.api_cart_emazon.domain.spi.ICarritoPersistencePort;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class AppConfig {

    // Definir un bean para RestTemplate para hacer llamadas HTTP
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    // Definir el adaptador de persistencia del carrito (CarritoPersistenceAdapter)
    @Bean
    public ICarritoPersistencePort carritoPersistencePort(ICarritoRepository carritoRepository, ICarritoMapper carritoMapper) {
        return new CarritoPersistenceAdapter(carritoRepository, carritoMapper);
    }

    // Definir el adaptador de servicio de stock
    @Bean
    public IStockServicePort stockServicePort(RestTemplate restTemplate) {
        return new StockServiceAdapter(restTemplate);
    }
}
