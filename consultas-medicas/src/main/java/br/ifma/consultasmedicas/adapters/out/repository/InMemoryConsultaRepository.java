package br.ifma.consultasmedicas.adapters.out.repository;

import br.ifma.consultasmedicas.core.domain.model.Consulta;
import br.ifma.consultasmedicas.ports.out.ConsultaRepository;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

public class InMemoryConsultaRepository implements ConsultaRepository {
    private final Map<Integer, Consulta> store = new HashMap<>();

    @Override
    public Optional<Consulta> buscarPorId(Integer id) {
        return Optional.ofNullable(store.get(id));
    }

    @Override
    public List<Consulta> buscarPorData(LocalDate data) {
        return store.values().stream()
            .filter(c -> c.getDataHora().toLocalDate().equals(data))
            .sorted(Comparator.comparing(Consulta::getDataHora))
            .collect(Collectors.toList());
    }

    @Override
    public void salvar(Consulta consulta) {
        store.put(consulta.getId(), consulta);
    }
}
