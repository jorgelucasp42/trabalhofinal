package br.ifma.consultasmedicas.ports.out;

/**
 * Enum para status de transação no gateway de pagamento.
 */
public enum StatusTransacao {
    PENDENTE("Aguardando processamento"),
    AUTORIZADA("Autorizada pela operadora"),
    CAPTURADA("Capturada com sucesso"),
    NEGADA("Negada pela operadora"),
    CANCELADA("Cancelada"),
    ERRO("Erro no processamento");

    private final String descricao;

    StatusTransacao(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }
}
