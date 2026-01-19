package br.ifma.consultasmedicas.adapters.in.dto;

import java.math.BigDecimal;

/**
 * DTO para requisição de processamento de pagamento.
 */
public class ProcessarPagamentoRequest {
    private Integer consultaId;
    private BigDecimal valor;
    private String numeroCartao;
    private String nomeTitular;
    private String validade; // MM/AA
    private String cvv;

    public ProcessarPagamentoRequest() {
    }

    public ProcessarPagamentoRequest(Integer consultaId, BigDecimal valor,
            String numeroCartao, String nomeTitular, String validade, String cvv) {
        this.consultaId = consultaId;
        this.valor = valor;
        this.numeroCartao = numeroCartao;
        this.nomeTitular = nomeTitular;
        this.validade = validade;
        this.cvv = cvv;
    }

    public Integer getConsultaId() {
        return consultaId;
    }

    public void setConsultaId(Integer consultaId) {
        this.consultaId = consultaId;
    }

    public BigDecimal getValor() {
        return valor;
    }

    public void setValor(BigDecimal valor) {
        this.valor = valor;
    }

    public String getNumeroCartao() {
        return numeroCartao;
    }

    public void setNumeroCartao(String numeroCartao) {
        this.numeroCartao = numeroCartao;
    }

    public String getNomeTitular() {
        return nomeTitular;
    }

    public void setNomeTitular(String nomeTitular) {
        this.nomeTitular = nomeTitular;
    }

    public String getValidade() {
        return validade;
    }

    public void setValidade(String validade) {
        this.validade = validade;
    }

    public String getCvv() {
        return cvv;
    }

    public void setCvv(String cvv) {
        this.cvv = cvv;
    }
}
