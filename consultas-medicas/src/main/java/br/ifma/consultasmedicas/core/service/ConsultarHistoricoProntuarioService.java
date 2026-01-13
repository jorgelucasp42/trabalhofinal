package br.ifma.consultasmedicas.core.service;

import br.ifma.consultasmedicas.core.domain.model.Prontuario;
import br.ifma.consultasmedicas.ports.in.ConsultarHistoricoProntuarioUseCase;
import br.ifma.consultasmedicas.ports.out.ProntuarioRepository;

import java.util.List;
import java.util.Objects;

public class ConsultarHistoricoProntuarioService implements ConsultarHistoricoProntuarioUseCase {
    private final ProntuarioRepository prontuarioRepository;
    private final SimpleLogger logger = new SimpleLogger(ConsultarHistoricoProntuarioService.class);

    public ConsultarHistoricoProntuarioService(ProntuarioRepository prontuarioRepository) {
        this.prontuarioRepository = Objects.requireNonNull(prontuarioRepository);
    }

    @Override
    public List<Prontuario> consultarPorPaciente(Integer pacienteId) {
        logger.info("Consultando histórico de prontuários para paciente: %d", pacienteId);
        List<Prontuario> prontuarios = prontuarioRepository.buscarPorPaciente(pacienteId);
        logger.info("Encontrados %d prontuários para paciente %d", prontuarios.size(), pacienteId);
        return prontuarios;
    }
}
