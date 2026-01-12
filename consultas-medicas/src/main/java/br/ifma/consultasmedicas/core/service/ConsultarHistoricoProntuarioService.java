package br.ifma.consultasmedicas.core.service;

import br.ifma.consultasmedicas.core.domain.model.Prontuario;
import br.ifma.consultasmedicas.ports.in.ConsultarHistoricoProntuarioUseCase;
import br.ifma.consultasmedicas.ports.out.ProntuarioRepository;

import java.util.List;
import java.util.Objects;

public class ConsultarHistoricoProntuarioService implements ConsultarHistoricoProntuarioUseCase {
    private final ProntuarioRepository prontuarioRepository;

    public ConsultarHistoricoProntuarioService(ProntuarioRepository prontuarioRepository) {
        this.prontuarioRepository = Objects.requireNonNull(prontuarioRepository);
    }

    @Override
    public List<Prontuario> consultarPorPaciente(Integer pacienteId) {
        return prontuarioRepository.buscarPorPaciente(pacienteId);
    }
}
