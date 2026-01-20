package br.ifma.consultasmedicas.adapters.config;

import br.ifma.consultasmedicas.adapters.out.gateway.FakePagamentoGateway;
import br.ifma.consultasmedicas.ports.out.PagamentoGateway;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GatewayConfig {

    @Bean
    public PagamentoGateway pagamentoGateway() {
        return new FakePagamentoGateway();
    }
}
