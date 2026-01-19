package br.ifma.consultasmedicas.adapters.in.dto;

/**
 * DTO para resposta de agendamento online.
 */
public class AgendarConsultaOnlineResponse {
    private Integer consultaId;
    private String mensagem;
    private String linkVideoconferencia;

    public AgendarConsultaOnlineResponse() {
    }

    public AgendarConsultaOnlineResponse(Integer consultaId, String mensagem, String linkVideoconferencia) {
        this.consultaId = consultaId;
        this.mensagem = mensagem;
        this.linkVideoconferencia = linkVideoconferencia;
    }

    public Integer getConsultaId() {
        return consultaId;
    }

    public void setConsultaId(Integer consultaId) {
        this.consultaId = consultaId;
    }

    public String getMensagem() {
        return mensagem;
    }

    public void setMensagem(String mensagem) {
        this.mensagem = mensagem;
    }

    public String getLinkVideoconferencia() {
        return linkVideoconferencia;
    }

    public void setLinkVideoconferencia(String linkVideoconferencia) {
        this.linkVideoconferencia = linkVideoconferencia;
    }
}
