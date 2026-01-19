package br.ifma.consultasmedicas.core.domain.model;

/**
 * Enum que representa os possíveis status de um pagamento.
 * 
 * Transições:
 * PENDENTE → PROCESSANDO → (CONCLUÍDO | FALHOU)
 */
public enum StatusPagamento {
    PENDENTE("Aguardando processamento"),
    PROCESSANDO("Em processamento"),
    CONCLUIDO("Pagamento confirmado"),
    FALHOU("Pagamento falhou");

    private final String descricao;

    StatusPagamento(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }
}
