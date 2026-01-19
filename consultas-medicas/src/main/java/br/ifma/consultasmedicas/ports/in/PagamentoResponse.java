package br.ifma.consultasmedicas.ports.in;

/**
 * Response para pagamento processado.
 * Contém informações sobre o resultado da transação.
 */
public class PagamentoResponse {
    private final Integer pagamentoId;
    private final String status;
    private final String mensagem;
    private final String idTransacao; // Retornado pelo gateway (pode ser null em caso de falha)

    public PagamentoResponse(Integer pagamentoId, String status, String mensagem, String idTransacao) {
        this.pagamentoId = pagamentoId;
        this.status = status;
        this.mensagem = mensagem;
        this.idTransacao = idTransacao;
    }

    public Integer getPagamentoId() {
        return pagamentoId;
    }

    public String getStatus() {
        return status;
    }

    public String getMensagem() {
        return mensagem;
    }

    public String getIdTransacao() {
        return idTransacao;
    }

    public boolean foiBemSucedido() {
        return "CONCLUÍDO".equalsIgnoreCase(status);
    }
}
