package br.ifma.consultasmedicas.adapters.out.id;

import br.ifma.consultasmedicas.ports.out.IdGenerator;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class UuidIdGenerator implements IdGenerator {

    @Override
    public Integer gerarId() {
        // Converte UUID para Integer (simples, suficiente para o projeto)
        return Math.abs(UUID.randomUUID().hashCode());
    }
}
