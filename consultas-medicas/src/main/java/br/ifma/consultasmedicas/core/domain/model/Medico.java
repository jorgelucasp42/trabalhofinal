package br.ifma.consultasmedicas.core.domain.model;

import java.util.Objects;

public class Medico {
    private final Integer id;
    private final String nome;
    private final String crm;

    public Medico(Integer id, String nome, String crm) {
        this.id = Objects.requireNonNull(id);
        this.nome = Objects.requireNonNull(nome);
        this.crm = Objects.requireNonNull(crm);
    }

    public Integer getId() { return id; }
    public String getNome() { return nome; }
    public String getCrm() { return crm; }
}
