package br.ifma.consultasmedicas.core.domain.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class Paciente {
    private final Integer id;
    private final String nomeCrianca;
    private final String nomeResponsavel;
    private final LocalDate dataNascimento;
    private final String sexo;
    private final Endereco endereco;
    private final List<Telefone> telefones = new ArrayList<>();
    private final PlanoSaude planoSaude; // pode ser null (particular)

    public Paciente(Integer id,
                    String nomeCrianca,
                    String nomeResponsavel,
                    LocalDate dataNascimento,
                    String sexo,
                    Endereco endereco,
                    List<Telefone> telefones,
                    PlanoSaude planoSaude) {
        this.id = Objects.requireNonNull(id);
        this.nomeCrianca = Objects.requireNonNull(nomeCrianca);
        this.nomeResponsavel = Objects.requireNonNull(nomeResponsavel);
        this.dataNascimento = Objects.requireNonNull(dataNascimento);
        this.sexo = Objects.requireNonNull(sexo);
        this.endereco = Objects.requireNonNull(endereco);
        if (telefones != null) this.telefones.addAll(telefones);
        this.planoSaude = planoSaude;
    }

    public Integer getId() { return id; }
    public String getNomeCrianca() { return nomeCrianca; }
    public String getNomeResponsavel() { return nomeResponsavel; }
    public LocalDate getDataNascimento() { return dataNascimento; }
    public String getSexo() { return sexo; }
    public Endereco getEndereco() { return endereco; }
    public List<Telefone> getTelefones() { return Collections.unmodifiableList(telefones); }
    public PlanoSaude getPlanoSaude() { return planoSaude; }

    public boolean isParticular() {
        return planoSaude == null;
    }
}
