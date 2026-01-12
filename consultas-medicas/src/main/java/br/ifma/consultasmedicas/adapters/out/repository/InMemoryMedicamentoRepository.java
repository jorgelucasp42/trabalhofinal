package br.ifma.consultasmedicas.adapters.out.repository;

import br.ifma.consultasmedicas.core.domain.model.Medicamento;
import br.ifma.consultasmedicas.ports.out.MedicamentoRepository;

import java.util.*;

public class InMemoryMedicamentoRepository implements MedicamentoRepository {
    private final Map<Integer, Medicamento> store = new HashMap<>();

    @Override
    public Optional<Medicamento> buscarPorId(Integer id) {
        return Optional.ofNullable(store.get(id));
    }

    @Override
    public List<Medicamento> listarTodos() {
        return new ArrayList<>(store.values());
    }

    @Override
    public void salvar(Medicamento medicamento) {
        store.put(medicamento.getId(), medicamento);
    }
}
