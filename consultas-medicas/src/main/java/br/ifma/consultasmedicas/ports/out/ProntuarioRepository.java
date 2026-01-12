package br.ifma.consultasmedicas.ports.out;

import br.ifma.consultasmedicas.core.domain.model.Prontuario;

import java.util.List;

public interface ProntuarioRepository {
    void salvar(Prontuario prontuario);
    List<Prontuario> buscarPorPaciente(Integer pacienteId);
    boolean existeParaConsulta(Integer consultaId);
}
