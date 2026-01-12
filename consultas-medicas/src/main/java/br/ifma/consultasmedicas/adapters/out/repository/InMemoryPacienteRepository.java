package br.ifma.consultasmedicas.adapters.out.repository;

import br.ifma.consultasmedicas.core.domain.model.Paciente;
import br.ifma.consultasmedicas.ports.out.PacienteRepository;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class InMemoryPacienteRepository implements PacienteRepository {
    private final Map<Integer, Paciente> store = new HashMap<>();

    @Override
    public Optional<Paciente> buscarPorId(Integer id) {
        return Optional.ofNullable(store.get(id));
    }

    @Override
    public void salvar(Paciente paciente) {
        store.put(paciente.getId(), paciente);
    }
}
