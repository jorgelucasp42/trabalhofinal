package br.ifma.consultasmedicas.core.service;

import br.ifma.consultasmedicas.core.domain.exception.DomainException;
import br.ifma.consultasmedicas.core.domain.model.*;
import br.ifma.consultasmedicas.ports.in.RegistrarProntuarioCommand;
import br.ifma.consultasmedicas.ports.in.RegistrarProntuarioUseCase;
import br.ifma.consultasmedicas.ports.out.ConsultaRepository;
import br.ifma.consultasmedicas.ports.out.ExameRepository;
import br.ifma.consultasmedicas.ports.out.IdGenerator;
import br.ifma.consultasmedicas.ports.out.MedicamentoRepository;
import br.ifma.consultasmedicas.ports.out.ProntuarioRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Caso de uso: Registro de Prontuário.
 * Regras obrigatórias do enunciado:
 * - Uma consulta gera exatamente um prontuário
 * - Um prontuário pode conter 0+ exames e 0+ prescrições
 */
public class RegistrarProntuarioService implements RegistrarProntuarioUseCase {

    private final ConsultaRepository consultaRepository;
    private final ProntuarioRepository prontuarioRepository;
    private final MedicamentoRepository medicamentoRepository;
    private final ExameRepository exameRepository;
    private final IdGenerator idGenerator;
    private final SimpleLogger logger = new SimpleLogger(RegistrarProntuarioService.class);

    public RegistrarProntuarioService(ConsultaRepository consultaRepository,
            ProntuarioRepository prontuarioRepository,
            MedicamentoRepository medicamentoRepository,
            ExameRepository exameRepository,
            IdGenerator idGenerator) {
        this.consultaRepository = Objects.requireNonNull(consultaRepository);
        this.prontuarioRepository = Objects.requireNonNull(prontuarioRepository);
        this.medicamentoRepository = Objects.requireNonNull(medicamentoRepository);
        this.exameRepository = Objects.requireNonNull(exameRepository);
        this.idGenerator = Objects.requireNonNull(idGenerator);
    }

    @Override
    public Integer registrar(RegistrarProntuarioCommand command) {
        logger.info("Iniciando registro de prontuário para consulta: %d", command.getConsultaId());

        Consulta consulta = consultaRepository.buscarPorId(command.getConsultaId())
                .orElseThrow(() -> {
                    logger.error("Consulta não encontrada: %d", command.getConsultaId());
                    return new DomainException("Consulta não encontrada: " + command.getConsultaId());
                });

        if (consulta.getStatus() != ConsultaStatus.AGENDADA) {
            logger.warn("Consulta %d não está em estado AGENDADA. Status: %s",
                    command.getConsultaId(), consulta.getStatus());
            throw new DomainException("A consulta não está em estado AGENDADA.");
        }

        if (prontuarioRepository.existeParaConsulta(consulta.getId())) {
            logger.warn("Prontuário já existe para consulta: %d", consulta.getId());
            throw new DomainException("Já existe prontuário registrado para a consulta " + consulta.getId());
        }

        List<Prescricao> prescricoes = new ArrayList<>();
        if (command.getPrescricoes() != null) {
            for (RegistrarProntuarioCommand.PrescricaoItemCommand item : command.getPrescricoes()) {
                Medicamento medicamento = medicamentoRepository.buscarPorId(item.getMedicamentoId())
                        .orElseThrow(() -> {
                            logger.error("Medicamento não encontrado: %d", item.getMedicamentoId());
                            return new DomainException("Medicamento não encontrado: " + item.getMedicamentoId());
                        });

                Prescricao prescricao = new Prescricao(
                        idGenerator.gerarId(),
                        medicamento,
                        item.getDosagem(),
                        item.getAdministracao(),
                        item.getTempoUso());
                prescricoes.add(prescricao);
                logger.debug("Prescrição adicionada: %s (dosagem: %s)", medicamento.getNome(), item.getDosagem());
            }
        }

        List<Exame> exames = new ArrayList<>();
        if (command.getExamesIds() != null) {
            for (Integer exameId : command.getExamesIds()) {
                Exame exame = exameRepository.buscarPorId(exameId)
                        .orElseThrow(() -> {
                            logger.error("Exame não encontrado: %d", exameId);
                            return new DomainException("Exame não encontrado: " + exameId);
                        });
                exames.add(exame);
                logger.debug("Exame adicionado: %s", exame.getNome());
            }
        }

        Prontuario prontuario = new Prontuario(
                idGenerator.gerarId(),
                consulta,
                command.getPeso(),
                command.getAltura(),
                command.getSintomas(),
                command.getObservacaoClinica(),
                prescricoes,
                exames);

        prontuarioRepository.salvar(prontuario);
        logger.info("Prontuário registrado com sucesso. ID: %d, Consulta: %d", prontuario.getId(), consulta.getId());

        // Atualiza status da consulta
        consulta.marcarRealizada();
        consultaRepository.salvar(consulta);
        logger.info("Status da consulta atualizado para REALIZADA");

        return prontuario.getId();
    }
}
