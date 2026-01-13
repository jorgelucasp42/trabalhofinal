package br.ifma.consultasmedicas.adapters.in.dto;

/**
 * DTO de resposta para erros de negócio e validação.
 */
public class ErrorResponse {
    private final String mensagem;
    private final String tipo;
    private final long timestamp;

    public ErrorResponse(String mensagem, String tipo) {
        this.mensagem = mensagem;
        this.tipo = tipo;
        this.timestamp = System.currentTimeMillis();
    }

    public String getMensagem() {
        return mensagem;
    }

    public String getTipo() {
        return tipo;
    }

    public long getTimestamp() {
        return timestamp;
    }
}
