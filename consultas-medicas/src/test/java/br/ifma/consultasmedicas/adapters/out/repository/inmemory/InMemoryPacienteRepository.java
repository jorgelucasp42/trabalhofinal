package br.ifma.consultasmedicas.adapters.out.repository.inmemory;

import br.ifma.consultasmedicas.core.domain.model.Paciente;
import br.ifma.consultasmedicas.ports.out.PacienteRepository;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

/**
 * ImplementaÃ§Ã£o em memÃ³ria de PacienteRepository para testes.
 */
public class InMemoryPacienteRepository implements PacienteRepository {
    private final Map<Integer, Paciente> pacientes = new HashMap<>();

    @Override
    public Optional<Paciente> buscarPorId(Integer id) {
        Objects.requireNonNull(id, "ID nÃ£o pode ser nulo");
        return Optional.ofNullable(pacientes.get(id));
    }

    @Override
    public void salvar(Paciente paciente) {
        Objects.requireNonNull(paciente, "Paciente nÃ£o pode ser nulo");
        pacientes.put(paciente.getId(), paciente);
    }

    @Override
    public Paciente obter(Integer id) {
        Objects.requireNonNull(id, "ID nÃ£o pode ser nulo");
        return pacientes.get(id);
    }

    public void limpar() {
        pacientes.clear();
    }
}

