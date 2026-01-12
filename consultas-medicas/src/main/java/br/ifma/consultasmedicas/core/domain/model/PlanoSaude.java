package br.ifma.consultasmedicas.core.domain.model;

import java.util.Objects;

public class PlanoSaude {
    private final Integer id;
    private final String nomePlano;

    public PlanoSaude(Integer id, String nomePlano) {
        this.id = Objects.requireNonNull(id);
        this.nomePlano = Objects.requireNonNull(nomePlano);
    }

    public Integer getId() { return id; }
    public String getNomePlano() { return nomePlano; }
}
