package br.ifma.consultasmedicas;

import br.ifma.consultasmedicas.ports.out.IdGenerator;

/**
 * ImplementaÃ§Ã£o simples de IdGenerator para testes.
 * Usa um contador incrementado.
 */
public class SimpleIdGenerator implements IdGenerator {
    private int counter = 0;

    @Override
    public Integer gerarId() {
        return ++counter;
    }

    public void reset() {
        counter = 0;
    }
}

