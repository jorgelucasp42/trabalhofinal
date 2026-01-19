package br.ifma.consultasmedicas.ports.out;

/**
 * Response do processamento de transação no gateway.
 */
public class TransacaoResponse {
    private final String idTransacao;
    private final boolean sucesso;
    private final String mensagem;
    private final StatusTransacao status;

    public TransacaoResponse(String idTransacao, boolean sucesso, String mensagem, StatusTransacao status) {
        this.idTransacao = idTransacao;
        this.sucesso = sucesso;
        this.mensagem = mensagem;
        this.status = status;
    }

    public String getIdTransacao() {
        return idTransacao;
    }

    public boolean isSucesso() {
        return sucesso;
    }

    public String getMensagem() {
        return mensagem;
    }

    public StatusTransacao getStatus() {
        return status;
    }
}
