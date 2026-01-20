package br.ifma.consultasmedicas.adapters.in.controller;

import br.ifma.consultasmedicas.core.domain.exception.DomainException;
import br.ifma.consultasmedicas.ports.in.CadastrarPacienteCommand;
import br.ifma.consultasmedicas.ports.in.CadastrarPacienteUseCase;
import org.springframework.stereotype.Service;

import java.util.Objects;

/**
 * Adaptador de entrada (Controller) para Pacientes.
 * Responsabilidade: orquestrar chamadas aos casos de uso e mapear respostas
 * para DTO.
 * 
 * Tratamento de erros:
 * - DomainException: erro de regra de negócio (400 Bad Request)
 * - IllegalArgumentException: validação de input (422 Unprocessable Entity)
 * - Exception genérica: erro inesperado (500 Internal Server Error)
 */
@Service
public class PacienteController {
    private final CadastrarPacienteUseCase cadastrarPacienteUseCase;

    public PacienteController(CadastrarPacienteUseCase cadastrarPacienteUseCase) {
        this.cadastrarPacienteUseCase = Objects.requireNonNull(cadastrarPacienteUseCase);
    }

    /**
     * Cadastra um novo paciente.
     * 
     * @param command dados do paciente a cadastrar
     * @return resposta com ID do paciente criado
     * @throws DomainException          se houver erro de regra de negócio
     * @throws IllegalArgumentException se houver erro de validação
     */
    public PacienteCadastroResponse cadastrarPaciente(CadastrarPacienteCommand command) {
        try {
            validarCommand(command);
            Integer pacienteId = cadastrarPacienteUseCase.cadastrar(command);
            return new PacienteCadastroResponse(pacienteId, "Paciente cadastrado com sucesso");
        } catch (DomainException e) {
            throw e; // Relança para tratamento em nível superior
        } catch (IllegalArgumentException e) {
            throw e; // Relança para tratamento em nível superior
        }
    }

    /**
     * Valida os dados de entrada antes de processar.
     */
    private void validarCommand(CadastrarPacienteCommand command) {
        if (command == null) {
            throw new IllegalArgumentException("Comando não pode ser nulo");
        }
        // Validações adicionais podem ser feitas aqui se necessário
        // O UseCase já faz validações completas
    }

    /**
     * DTO de resposta para cadastro de paciente.
     */
    public static class PacienteCadastroResponse {
        private final Integer pacienteId;
        private final String mensagem;

        public PacienteCadastroResponse(Integer pacienteId, String mensagem) {
            this.pacienteId = pacienteId;
            this.mensagem = mensagem;
        }

        public Integer getPacienteId() {
            return pacienteId;
        }

        public String getMensagem() {
            return mensagem;
        }
    }
}
