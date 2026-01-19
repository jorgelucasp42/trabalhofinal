package br.ifma.consultasmedicas.core.service;

import br.ifma.consultasmedicas.core.domain.exception.DomainException;
import br.ifma.consultasmedicas.core.domain.model.*;
import br.ifma.consultasmedicas.ports.in.HistoricoOnlineResponse;
import br.ifma.consultasmedicas.ports.in.VisualizarHistoricoOnlineUseCase;
import br.ifma.consultasmedicas.ports.out.ConsultaRepository;
import br.ifma.consultasmedicas.ports.out.MedicoRepository;
import br.ifma.consultasmedicas.ports.out.PacienteRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Caso de uso: Visualizar Histórico Online.
 * 
 * Responsabilidades:
 * - Recuperar todas as consultas do paciente
 * - Enriquecer com dados do médico (nome, especialidade)
 * - Incluir status da consulta
 * - Montar resposta para visualização
 */
public class VisualizarHistoricoOnlineService implements VisualizarHistoricoOnlineUseCase {

    private final ConsultaRepository consultaRepository;
    private final PacienteRepository pacienteRepository;
    private final MedicoRepository medicoRepository;
    private final SimpleLogger logger = new SimpleLogger(VisualizarHistoricoOnlineService.class);

    public VisualizarHistoricoOnlineService(ConsultaRepository consultaRepository,
            PacienteRepository pacienteRepository,
            MedicoRepository medicoRepository) {
        this.consultaRepository = Objects.requireNonNull(consultaRepository);
        this.pacienteRepository = Objects.requireNonNull(pacienteRepository);
        this.medicoRepository = Objects.requireNonNull(medicoRepository);
    }

    @Override
    public HistoricoOnlineResponse obterHistorico(Integer pacienteId) {
        logger.info("Recuperando histórico online para paciente: " + pacienteId);

        try {
            // 1. Valida paciente
            Paciente paciente = pacienteRepository.obter(pacienteId);
            if (paciente == null) {
                throw new DomainException("Paciente não encontrado: " + pacienteId);
            }
            logger.info("Paciente encontrado: " + paciente.getNomeCrianca());

            // 2. Recupera todas as consultas do paciente
            List<Consulta> consultas = consultaRepository.obterPorPaciente(pacienteId);
            logger.info("Total de consultas encontradas: " + consultas.size());

            // 3. Monta resposta com informações enriquecidas
            List<HistoricoOnlineResponse.ConsultaOnlineInfo> consultasInfo = new ArrayList<>();

            for (Consulta consulta : consultas) {
                Medico medico = medicoRepository.obter(consulta.getMedico().getId());

                HistoricoOnlineResponse.ConsultaOnlineInfo info = new HistoricoOnlineResponse.ConsultaOnlineInfo(
                        consulta.getId(),
                        consulta.getDataHora(),
                        medico != null ? medico.getNome() : "Médico desconhecido",
                        medico != null ? medico.getEspecialidade() : "N/A",
                        consulta.getStatus().toString(),
                        consulta.getProntuarioId().orElse(null));

                consultasInfo.add(info);
            }

            logger.info("Histórico montado com sucesso");
            return new HistoricoOnlineResponse(pacienteId, paciente.getNomeCrianca(), consultasInfo);

        } catch (DomainException e) {
            logger.error("Erro de domínio ao recuperar histórico: " + e.getMessage());
            throw e;
        } catch (Exception e) {
            logger.error("Erro inesperado ao recuperar histórico: " + e.getMessage());
            throw new DomainException("Erro ao recuperar histórico: " + e.getMessage());
        }
    }
}
