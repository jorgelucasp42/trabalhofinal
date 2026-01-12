package br.ifma.consultasmedicas.core.domain.model;

import java.util.Objects;

public class Endereco {
    private final String logradouro;
    private final String numero;
    private final String complemento;
    private final String bairro;
    private final String cidade;
    private final String estadoUf;
    private final String cep;

    public Endereco(String logradouro, String numero, String complemento, String bairro, String cidade, String estadoUf, String cep) {
        this.logradouro = Objects.requireNonNull(logradouro);
        this.numero = Objects.requireNonNull(numero);
        this.complemento = complemento;
        this.bairro = Objects.requireNonNull(bairro);
        this.cidade = Objects.requireNonNull(cidade);
        this.estadoUf = Objects.requireNonNull(estadoUf);
        this.cep = Objects.requireNonNull(cep);
    }

    public String getLogradouro() { return logradouro; }
    public String getNumero() { return numero; }
    public String getComplemento() { return complemento; }
    public String getBairro() { return bairro; }
    public String getCidade() { return cidade; }
    public String getEstadoUf() { return estadoUf; }
    public String getCep() { return cep; }
}
