package br.ifma.consultasmedicas.adapters.in.controller;

import br.ifma.consultasmedicas.core.domain.exception.DomainException;
import br.ifma.consultasmedicas.core.domain.model.Consulta;
import br.ifma.consultasmedicas.ports.in.AgendarConsultaOnlineCommand;
import br.ifma.consultasmedicas.ports.in.AgendarConsultaOnlineUseCase;
import br.ifma.consultasmedicas.ports.in.ListarConsultasDoDiaUseCase;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Adaptador de entrada (Controller) para Consultas.
 * Responsabilidade: orquestrar chamadas aos casos de uso e mapear respostas
 * para DTO.
 * 
 * Tratamento de erros:
 * - DomainException: erro de regra de negócio (400 Bad Request)
 * - IllegalArgumentException: validação de input (422 Unprocessable Entity)
 * - Exception genérica: erro inesperado (500 Internal Server Error)
 */
@Service
public class ConsultaController {
    private final ListarConsultasDoDiaUseCase listarConsultasDoDiaUseCase;
    private final AgendarConsultaOnlineUseCase agendarConsultaOnlineUseCase;

    public ConsultaController(
            ListarConsultasDoDiaUseCase listarConsultasDoDiaUseCase,
            AgendarConsultaOnlineUseCase agendarConsultaOnlineUseCase) {
        this.listarConsultasDoDiaUseCase = Objects.requireNonNull(listarConsultasDoDiaUseCase);
        this.agendarConsultaOnlineUseCase = Objects.requireNonNull(agendarConsultaOnlineUseCase);
    }

    /**
     * Lista todas as consultas agendadas para uma data específica.
     * 
     * @param data data para listar consultas
     * @return lista de consultas do dia
     */
    public List<ConsultaListagemResponse> listarConsultasDoDia(LocalDate data) {
        if (data == null) {
            throw new IllegalArgumentException("Data é obrigatória");
        }
        
        List<Consulta> consultas = listarConsultasDoDiaUseCase.listar(data);
        
        return consultas.stream()
                .map(ConsultaListagemResponse::fromDomain)
                .collect(Collectors.toList());
    }

    /**
     * Agenda uma nova consulta online.
     * 
     * @param command dados da consulta a agendar
     * @return resposta com ID da consulta criada
     * @throws DomainException          se houver erro de regra de negócio
     * @throws IllegalArgumentException se houver erro de validação
     */
    public ConsultaAgendamentoResponse agendarConsultaOnline(AgendarConsultaOnlineCommand command) {
        try {
            validarCommand(command);
            Integer consultaId = agendarConsultaOnlineUseCase.agendar(command);
            return new ConsultaAgendamentoResponse(consultaId, "Consulta online agendada com sucesso");
        } catch (DomainException e) {
            throw e; // Relança para tratamento em nível superior
        } catch (IllegalArgumentException e) {
            throw e; // Relança para tratamento em nível superior
        }
    }

    /**
     * Valida os dados de entrada antes de processar.
     */
    private void validarCommand(AgendarConsultaOnlineCommand command) {
        if (command == null) {
            throw new IllegalArgumentException("Comando não pode ser nulo");
        }
        if (command.getPacienteId() == null || command.getPacienteId() <= 0) {
            throw new IllegalArgumentException("ID do paciente é obrigatório e deve ser positivo");
        }
        if (command.getMedicoId() == null || command.getMedicoId() <= 0) {
            throw new IllegalArgumentException("ID do médico é obrigatório e deve ser positivo");
        }
        if (command.getDataHora() == null) {
            throw new IllegalArgumentException("Data/hora é obrigatória");
        }
    }

    /**
     * DTO de resposta para listagem de consultas.
     */
    public static class ConsultaListagemResponse {
        private final Integer id;
        private final String pacienteNome;
        private final String medicoNome;
        private final String dataHora;
        private final String status;

        public ConsultaListagemResponse(Integer id, String pacienteNome, String medicoNome, String dataHora, String status) {
            this.id = id;
            this.pacienteNome = pacienteNome;
            this.medicoNome = medicoNome;
            this.dataHora = dataHora;
            this.status = status;
        }

        public static ConsultaListagemResponse fromDomain(Consulta consulta) {
            return new ConsultaListagemResponse(
                    consulta.getId(),
                    consulta.getPaciente().getNomeCrianca(),
                    consulta.getMedico().getNome(),
                    consulta.getDataHora().toString(),
                    consulta.getStatus().toString());
        }

        public Integer getId() {
            return id;
        }

        public String getPacienteNome() {
            return pacienteNome;
        }

        public String getMedicoNome() {
            return medicoNome;
        }

        public String getDataHora() {
            return dataHora;
        }

        public String getStatus() {
            return status;
        }
    }

    /**
     * DTO de resposta para agendamento de consulta.
     */
    public static class ConsultaAgendamentoResponse {
        private final Integer consultaId;
        private final String mensagem;

        public ConsultaAgendamentoResponse(Integer consultaId, String mensagem) {
            this.consultaId = consultaId;
            this.mensagem = mensagem;
        }

        public Integer getConsultaId() {
            return consultaId;
        }

        public String getMensagem() {
            return mensagem;
        }
    }
}
