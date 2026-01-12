package br.ifma.consultasmedicas.core.domain.model;

import java.util.Objects;

public class Telefone {
    private final String numero;
    private final TelefoneTipo tipo;
    private final String responsavel; // identifica o responsável, quando aplicável

    public Telefone(String numero, TelefoneTipo tipo, String responsavel) {
        this.numero = Objects.requireNonNull(numero);
        this.tipo = Objects.requireNonNull(tipo);
        this.responsavel = responsavel;
    }

    public String getNumero() {
        return numero;
    }

    public TelefoneTipo getTipo() {
        return tipo;
    }

    public String getResponsavel() {
        return responsavel;
    }
}
