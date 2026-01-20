package br.ifma.consultasmedicas.adapters.in.controller;

import br.ifma.consultasmedicas.adapters.in.dto.AgendarConsultaOnlineRequest;
import br.ifma.consultasmedicas.adapters.in.dto.AgendarConsultaOnlineResponse;
import br.ifma.consultasmedicas.core.domain.exception.DomainException;
import br.ifma.consultasmedicas.ports.in.AgendarConsultaOnlineCommand;
import br.ifma.consultasmedicas.ports.in.AgendarConsultaOnlineUseCase;
import org.springframework.stereotype.Service;

import java.util.Objects;

/**
 * Controller para agendamento de consultas online.
 * 
 * Responsabilidade: Mapear requisições HTTP para comandos de domínio
 * e respostas de domínio para DTOs.
 */
@Service
public class ConsultaOnlineController {
    private final AgendarConsultaOnlineUseCase agendarConsultaOnlineUseCase;

    public ConsultaOnlineController(AgendarConsultaOnlineUseCase agendarConsultaOnlineUseCase) {
        this.agendarConsultaOnlineUseCase = Objects.requireNonNull(agendarConsultaOnlineUseCase);
    }

    /**
     * Agenda uma nova consulta online.
     * 
     * @param request Dados da consulta a agendar
     * @return Resposta com ID da consulta criada
     */
    public AgendarConsultaOnlineResponse agendar(AgendarConsultaOnlineRequest request) {
        try {
            // Valida entrada
            if (request.getPacienteId() == null || request.getMedicoId() == null
                    || request.getDataHora() == null || request.getTipoVideoconferencia() == null) {
                throw new IllegalArgumentException("Todos os campos são obrigatórios");
            }

            // Cria comando de domínio
            AgendarConsultaOnlineCommand command = new AgendarConsultaOnlineCommand(
                    request.getPacienteId(),
                    request.getMedicoId(),
                    request.getDataHora(),
                    request.getTipoVideoconferencia());

            // Executa caso de uso
            Integer consultaId = agendarConsultaOnlineUseCase.agendar(command);

            // Mapeia resposta
            return new AgendarConsultaOnlineResponse(
                    consultaId,
                    "Consulta agendada com sucesso",
                    "link-sera-adicionado-aqui" // TODO: Adicionar link real
            );

        } catch (DomainException e) {
            // Erro de regra de negócio (400 Bad Request)
            throw new IllegalArgumentException("Erro ao agendar consulta: " + e.getMessage());
        } catch (Exception e) {
            // Erro inesperado (500 Internal Server Error)
            throw new RuntimeException("Erro ao agendar consulta: " + e.getMessage());
        }
    }
}
