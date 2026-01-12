package br.ifma.consultasmedicas.core.domain.model;

import java.util.Objects;

public class Medicamento {
    private final Integer id;
    private final String nome;

    public Medicamento(Integer id, String nome) {
        this.id = Objects.requireNonNull(id);
        this.nome = Objects.requireNonNull(nome);
    }

    public Integer getId() { return id; }
    public String getNome() { return nome; }
}
