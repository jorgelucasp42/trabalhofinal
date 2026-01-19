package br.ifma.consultasmedicas.ports.in;

/**
 * Caso de uso: Processar pagamento de consulta online.
 * 
 * Responsabilidade: Orquestrar o pagamento através de um gateway,
 * validando dados e persistindo o registro.
 */
public interface ProcessarPagamentoUseCase {
    PagamentoResponse processar(ProcessarPagamentoCommand command);
}
