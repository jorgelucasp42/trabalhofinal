package br.ifma.consultasmedicas.adapters.out.repository.jpa.entity;

import jakarta.persistence.Embeddable;

@Embeddable
public class EnderecoEmbeddable {

    private String logradouro;
    private String numero;
    private String complemento;
    private String bairro;
    private String cidade;
    private String estadoUf;
    private String cep;

    protected EnderecoEmbeddable() {}

    public EnderecoEmbeddable(
            String logradouro,
            String numero,
            String complemento,
            String bairro,
            String cidade,
            String estadoUf,
            String cep
    ) {
        this.logradouro = logradouro;
        this.numero = numero;
        this.complemento = complemento;
        this.bairro = bairro;
        this.cidade = cidade;
        this.estadoUf = estadoUf;
        this.cep = cep;
    }

    public String getLogradouro() { return logradouro; }
    public String getNumero() { return numero; }
    public String getComplemento() { return complemento; }
    public String getBairro() { return bairro; }
    public String getCidade() { return cidade; }
    public String getEstadoUf() { return estadoUf; }
    public String getCep() { return cep; }
}

