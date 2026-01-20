package br.ifma.consultasmedicas.ports.in;

import java.util.Objects;

/**
 * Comando para Cadastro de Médico.
 * Encapsula os dados necessários para criar um novo médico.
 */
public class CadastrarMedicoCommand {
    private final String nome;
    private final String especialidade;
    private final String crm;

    public CadastrarMedicoCommand(String nome, String especialidade, String crm) {
        this.nome = Objects.requireNonNull(nome, "Nome não pode ser nulo");
        this.especialidade = Objects.requireNonNull(especialidade, "Especialidade não pode ser nula");
        this.crm = Objects.requireNonNull(crm, "CRM não pode ser nulo");

        if (nome.isBlank()) {
            throw new IllegalArgumentException("Nome não pode estar vazio");
        }
        if (especialidade.isBlank()) {
            throw new IllegalArgumentException("Especialidade não pode estar vazia");
        }
        if (crm.isBlank()) {
            throw new IllegalArgumentException("CRM não pode estar vazio");
        }
    }

    public String getNome() {
        return nome;
    }

    public String getEspecialidade() {
        return especialidade;
    }

    public String getCrm() {
        return crm;
    }
}
