package br.ifma.consultasmedicas.adapters.out.repository;

import br.ifma.consultasmedicas.core.domain.model.Exame;
import br.ifma.consultasmedicas.ports.out.ExameRepository;

import java.util.*;

public class InMemoryExameRepository implements ExameRepository {
    private final Map<Integer, Exame> store = new HashMap<>();

    @Override
    public Optional<Exame> buscarPorId(Integer id) {
        return Optional.ofNullable(store.get(id));
    }

    @Override
    public List<Exame> listarTodos() {
        return new ArrayList<>(store.values());
    }

    @Override
    public void salvar(Exame exame) {
        store.put(exame.getId(), exame);
    }
}
