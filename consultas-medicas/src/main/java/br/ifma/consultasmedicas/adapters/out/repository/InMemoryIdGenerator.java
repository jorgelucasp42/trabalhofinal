package br.ifma.consultasmedicas.adapters.out.repository;

import br.ifma.consultasmedicas.ports.out.IdGenerator;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Implementação in-memory de geração de IDs com incremento sequencial.
 * Adequada para testes e desenvolvimento.
 */
public class InMemoryIdGenerator implements IdGenerator {
    private final AtomicInteger counter;

    public InMemoryIdGenerator(int initialValue) {
        this.counter = new AtomicInteger(initialValue);
    }

    public InMemoryIdGenerator() {
        this(1);
    }

    @Override
    public Integer gerarId() {
        return counter.getAndIncrement();
    }
}
