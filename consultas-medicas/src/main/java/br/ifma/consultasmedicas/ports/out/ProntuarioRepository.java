package br.ifma.consultasmedicas.ports.out;

import br.ifma.consultasmedicas.core.domain.model.Prontuario;

import java.util.List;
import java.util.Optional;

public interface ProntuarioRepository {
    void salvar(Prontuario prontuario);

    Optional<Prontuario> buscarPorId(Integer id);

    List<Prontuario> buscarPorPaciente(Integer pacienteId);

    boolean existeParaConsulta(Integer consultaId);
}
