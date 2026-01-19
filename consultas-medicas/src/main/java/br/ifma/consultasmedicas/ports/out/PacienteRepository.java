package br.ifma.consultasmedicas.ports.out;

import br.ifma.consultasmedicas.core.domain.model.Paciente;

import java.util.Optional;

public interface PacienteRepository {
    Optional<Paciente> buscarPorId(Integer id);

    Paciente obter(Integer id);

    void salvar(Paciente paciente);
}
