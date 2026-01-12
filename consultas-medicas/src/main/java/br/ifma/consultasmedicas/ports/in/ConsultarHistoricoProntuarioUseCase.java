package br.ifma.consultasmedicas.ports.in;

import br.ifma.consultasmedicas.core.domain.model.Prontuario;

import java.util.List;

public interface ConsultarHistoricoProntuarioUseCase {
    List<Prontuario> consultarPorPaciente(Integer pacienteId);
}
