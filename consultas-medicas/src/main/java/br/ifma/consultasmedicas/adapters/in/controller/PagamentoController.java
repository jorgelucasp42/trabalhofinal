package br.ifma.consultasmedicas.adapters.in.controller;

import br.ifma.consultasmedicas.adapters.in.dto.ProcessarPagamentoRequest;
import br.ifma.consultasmedicas.core.domain.exception.DomainException;
import br.ifma.consultasmedicas.ports.in.PagamentoResponse;
import br.ifma.consultasmedicas.ports.in.ProcessarPagamentoCommand;
import br.ifma.consultasmedicas.ports.in.ProcessarPagamentoUseCase;

import java.util.Objects;

/**
 * Controller para pagamentos de consultas online.
 * 
 * Responsabilidade: Mapear requisições HTTP para comandos de domínio
 * e respostas de domínio para DTOs.
 */
public class PagamentoController {
    private final ProcessarPagamentoUseCase processarPagamentoUseCase;

    public PagamentoController(ProcessarPagamentoUseCase processarPagamentoUseCase) {
        this.processarPagamentoUseCase = Objects.requireNonNull(processarPagamentoUseCase);
    }

    /**
     * Processa um pagamento de consulta.
     * 
     * @param request Dados do pagamento
     * @return Resposta com resultado da transação
     */
    public PagamentoResponse processar(ProcessarPagamentoRequest request) {
        try {
            // Valida entrada
            if (request.getConsultaId() == null || request.getValor() == null
                    || request.getNumeroCartao() == null || request.getNomeTitular() == null) {
                throw new IllegalArgumentException("Campos obrigatórios ausentes");
            }

            // Cria comando de domínio
            ProcessarPagamentoCommand command = new ProcessarPagamentoCommand(
                    request.getConsultaId(),
                    request.getValor(),
                    request.getNumeroCartao(),
                    request.getNomeTitular(),
                    request.getValidade(),
                    request.getCvv());

            // Executa caso de uso
            return processarPagamentoUseCase.processar(command);

        } catch (DomainException e) {
            // Erro de regra de negócio (400 Bad Request)
            return new PagamentoResponse(
                    null,
                    "ERRO",
                    e.getMessage(),
                    null);
        } catch (Exception e) {
            // Erro inesperado (500 Internal Server Error)
            return new PagamentoResponse(
                    null,
                    "ERRO",
                    "Erro ao processar pagamento: " + e.getMessage(),
                    null);
        }
    }
}
