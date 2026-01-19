package br.ifma.consultasmedicas.ports.in;

import java.math.BigDecimal;
import java.util.Objects;

/**
 * Command para processar pagamento.
 * 
 * Contém dados necessários para realizar a transação.
 */
public class ProcessarPagamentoCommand {
    private final Integer consultaId;
    private final BigDecimal valor;
    private final String numeroCartao;
    private final String nomeTitular;
    private final String validade; // MM/AA
    private final String cvv;

    public ProcessarPagamentoCommand(Integer consultaId, BigDecimal valor,
            String numeroCartao, String nomeTitular, String validade, String cvv) {
        this.consultaId = Objects.requireNonNull(consultaId, "ID da consulta não pode ser nulo");
        this.valor = Objects.requireNonNull(valor, "Valor não pode ser nulo");
        this.numeroCartao = Objects.requireNonNull(numeroCartao, "Número do cartão não pode ser nulo");
        this.nomeTitular = Objects.requireNonNull(nomeTitular, "Nome do titular não pode ser nulo");
        this.validade = Objects.requireNonNull(validade, "Validade não pode ser nula");
        this.cvv = Objects.requireNonNull(cvv, "CVV não pode ser nulo");
    }

    public Integer getConsultaId() {
        return consultaId;
    }

    public BigDecimal getValor() {
        return valor;
    }

    public String getNumeroCartao() {
        return numeroCartao;
    }

    public String getNomeTitular() {
        return nomeTitular;
    }

    public String getValidade() {
        return validade;
    }

    public String getCvv() {
        return cvv;
    }
}
