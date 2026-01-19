package br.ifma.consultasmedicas.ports.out;

/**
 * Classe para encapsular dados da transação enviados ao gateway.
 */
public class ProcessarTransacaoRequest {
    private final String numeroCartao;
    private final String nomeTitular;
    private final String validade;
    private final String cvv;
    private final java.math.BigDecimal valor;
    private final String descricao;

    public ProcessarTransacaoRequest(String numeroCartao, String nomeTitular,
            String validade, String cvv, java.math.BigDecimal valor, String descricao) {
        this.numeroCartao = numeroCartao;
        this.nomeTitular = nomeTitular;
        this.validade = validade;
        this.cvv = cvv;
        this.valor = valor;
        this.descricao = descricao;
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

    public java.math.BigDecimal getValor() {
        return valor;
    }

    public String getDescricao() {
        return descricao;
    }
}
