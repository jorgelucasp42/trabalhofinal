package br.ifma.consultasmedicas.adapters.in.controller;

import br.ifma.consultasmedicas.core.domain.exception.DomainException;
import br.ifma.consultasmedicas.ports.in.RegistrarProntuarioCommand;
import br.ifma.consultasmedicas.ports.in.RegistrarProntuarioUseCase;
import org.springframework.stereotype.Service;

import java.util.Objects;

/**
 * Adaptador de entrada (Controller).
 * Responsabilidade: orquestrar chamadas aos casos de uso e mapear respostas
 * para DTO.
 * 
 * Tratamento de erros:
 * - DomainException: erro de regra de negócio (400 Bad Request)
 * - IllegalArgumentException: validação de input (422 Unprocessable Entity)
 * - Exception genérica: erro inesperado (500 Internal Server Error)
 */
@Service
public class ProntuarioController {
    private final RegistrarProntuarioUseCase registrarProntuarioUseCase;

    public ProntuarioController(RegistrarProntuarioUseCase registrarProntuarioUseCase) {
        this.registrarProntuarioUseCase = Objects.requireNonNull(registrarProntuarioUseCase);
    }

    /**
     * Registra um novo prontuário para uma consulta.
     * 
     * @param command dados do prontuário a registrar
     * @return resposta com ID do prontuário criado
     * @throws DomainException          se houver erro de regra de negócio
     * @throws IllegalArgumentException se houver erro de validação
     */
    public ProntuarioRegistroResponse registrarProntuario(RegistrarProntuarioCommand command) {
        try {
            validarCommand(command);
            Integer prontuarioId = registrarProntuarioUseCase.registrar(command);
            return new ProntuarioRegistroResponse(prontuarioId, "Prontuário registrado com sucesso");
        } catch (DomainException e) {
            throw e; // Relança para tratamento em nível superior
        } catch (IllegalArgumentException e) {
            throw e; // Relança para tratamento em nível superior
        }
    }

    /**
     * Valida os dados de entrada antes de processar.
     */
    private void validarCommand(RegistrarProntuarioCommand command) {
        if (command == null) {
            throw new IllegalArgumentException("Comando não pode ser nulo");
        }
        if (command.getConsultaId() == null || command.getConsultaId() <= 0) {
            throw new IllegalArgumentException("ID da consulta é obrigatório e deve ser positivo");
        }
        if (command.getPeso() <= 0) {
            throw new IllegalArgumentException("Peso deve ser maior que zero");
        }
        if (command.getAltura() <= 0) {
            throw new IllegalArgumentException("Altura deve ser maior que zero");
        }
    }

    /**
     * DTO de resposta para registro de prontuário.
     */
    public static class ProntuarioRegistroResponse {
        private final Integer prontuarioId;
        private final String mensagem;

        public ProntuarioRegistroResponse(Integer prontuarioId, String mensagem) {
            this.prontuarioId = prontuarioId;
            this.mensagem = mensagem;
        }

        public Integer getProntuarioId() {
            return prontuarioId;
        }

        public String getMensagem() {
            return mensagem;
        }
    }
}
