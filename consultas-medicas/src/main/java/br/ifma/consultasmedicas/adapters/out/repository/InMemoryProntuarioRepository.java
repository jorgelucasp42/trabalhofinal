package br.ifma.consultasmedicas.adapters.out.repository;

import br.ifma.consultasmedicas.core.domain.model.Prontuario;
import br.ifma.consultasmedicas.ports.out.ProntuarioRepository;

import java.util.*;
import java.util.stream.Collectors;

public class InMemoryProntuarioRepository implements ProntuarioRepository {
    private final Map<Integer, Prontuario> store = new HashMap<>();

    @Override
    public void salvar(Prontuario prontuario) {
        store.put(prontuario.getId(), prontuario);
    }

    @Override
    public Optional<Prontuario> buscarPorId(Integer id) {
        return Optional.ofNullable(store.get(id));
    }

    @Override
    public List<Prontuario> buscarPorPaciente(Integer pacienteId) {
        return store.values().stream()
                .filter(p -> p.getConsulta().getPaciente().getId().equals(pacienteId))
                .sorted(Comparator.comparing(p -> p.getConsulta().getDataHora()))
                .collect(Collectors.toList());
    }

    @Override
    public boolean existeParaConsulta(Integer consultaId) {
        return store.values().stream()
                .anyMatch(p -> p.getConsulta().getId().equals(consultaId));
    }
}
