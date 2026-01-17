package br.ifma.consultasmedicas;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Aplicação Spring Boot para Consultas Médicas.
 * Inicia o servidor web e carrega todas as dependências do Spring.
 */
@SpringBootApplication
public class ConsultasMedicasApplication {

    public static void main(String[] args) {
        SpringApplication.run(ConsultasMedicasApplication.class, args);
    }
}
