package br.ifma.consultasmedicas.core.service;

import br.ifma.consultasmedicas.core.domain.exception.DomainException;
import br.ifma.consultasmedicas.core.domain.model.*;
import br.ifma.consultasmedicas.ports.in.RegistrarProntuarioCommand;
import br.ifma.consultasmedicas.ports.in.RegistrarProntuarioUseCase;
import br.ifma.consultasmedicas.ports.out.ConsultaRepository;
import br.ifma.consultasmedicas.ports.out.ExameRepository;
import br.ifma.consultasmedicas.ports.out.MedicamentoRepository;
import br.ifma.consultasmedicas.ports.out.ProntuarioRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;

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

    private final AtomicInteger prontuarioIdSeq = new AtomicInteger(1);
    private final AtomicInteger prescricaoIdSeq = new AtomicInteger(1);

    public RegistrarProntuarioService(ConsultaRepository consultaRepository,
                                     ProntuarioRepository prontuarioRepository,
                                     MedicamentoRepository medicamentoRepository,
                                     ExameRepository exameRepository) {
        this.consultaRepository = Objects.requireNonNull(consultaRepository);
        this.prontuarioRepository = Objects.requireNonNull(prontuarioRepository);
        this.medicamentoRepository = Objects.requireNonNull(medicamentoRepository);
        this.exameRepository = Objects.requireNonNull(exameRepository);
    }

    @Override
    public Integer registrar(RegistrarProntuarioCommand command) {
        Consulta consulta = consultaRepository.buscarPorId(command.getConsultaId())
            .orElseThrow(() -> new DomainException("Consulta não encontrada: " + command.getConsultaId()));

        if (consulta.getStatus() != ConsultaStatus.AGENDADA) {
            throw new DomainException("A consulta não está em estado AGENDADA.");
        }

        if (prontuarioRepository.existeParaConsulta(consulta.getId())) {
            throw new DomainException("Já existe prontuário registrado para a consulta " + consulta.getId());
        }

        List<Prescricao> prescricoes = new ArrayList<>();
        if (command.getPrescricoes() != null) {
            for (RegistrarProntuarioCommand.PrescricaoItemCommand item : command.getPrescricoes()) {
                Medicamento medicamento = medicamentoRepository.buscarPorId(item.getMedicamentoId())
                    .orElseThrow(() -> new DomainException("Medicamento não encontrado: " + item.getMedicamentoId()));

                Prescricao prescricao = new Prescricao(
                    prescricaoIdSeq.getAndIncrement(),
                    medicamento,
                    item.getDosagem(),
                    item.getAdministracao(),
                    item.getTempoUso()
                );
                prescricoes.add(prescricao);
            }
        }

        List<Exame> exames = new ArrayList<>();
        if (command.getExamesIds() != null) {
            for (Integer exameId : command.getExamesIds()) {
                Exame exame = exameRepository.buscarPorId(exameId)
                    .orElseThrow(() -> new DomainException("Exame não encontrado: " + exameId));
                exames.add(exame);
            }
        }

        Prontuario prontuario = ProntuarioBuilder.builder()
            .id(prontuarioIdSeq.getAndIncrement())
            .consulta(consulta)
            .peso(command.getPeso())
            .altura(command.getAltura())
            .sintomas(command.getSintomas())
            .observacao(command.getObservacaoClinica())
            .build();

        // Recria prontuário incluindo listas (mantendo builder simples)
        prontuario = new Prontuario(
            prontuario.getId(),
            prontuario.getConsulta(),
            prontuario.getPeso(),
            prontuario.getAltura(),
            prontuario.getSintomas(),
            prontuario.getObservacaoClinica(),
            prescricoes,
            exames
        );

        prontuarioRepository.salvar(prontuario);

        // Atualiza status da consulta
        consulta.marcarRealizada();
        consultaRepository.salvar(consulta);

        return prontuario.getId();
    }
}
