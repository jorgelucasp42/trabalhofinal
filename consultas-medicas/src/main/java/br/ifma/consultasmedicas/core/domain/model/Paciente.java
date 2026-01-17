package br.ifma.consultasmedicas.core.domain.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * Agregado de Paciente.
 * Encapsula informações pessoais, contacto, plano de saúde e históricos.
 * 
 * Responsabilidades:
 * - Manter dados pessoais do paciente (imutáveis)
 * - Manter histórico de peso/altura (mutável, apenas adição)
 * - Manter referências de prontuários realizados (mutável, apenas adição)
 */
public class Paciente {
    private final Integer id;
    private final String nomeCrianca;
    private final String nomeResponsavel;
    private final LocalDate dataNascimento;
    private final String sexo;
    private final Endereco endereco;
    private final List<Telefone> telefones = new ArrayList<>();
    private final PlanoSaude planoSaude; // pode ser null (particular)

    // Históricos (mantidos por referência, não pela Consulta)
    private final List<PesoAltura> historicoPesoAltura = new ArrayList<>();
    private final List<Integer> prontuarioIds = new ArrayList<>(); // IDs para não criar referências cíclicas

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
        if (telefones != null)
            this.telefones.addAll(telefones);
        this.planoSaude = planoSaude;
    }

    public Integer getId() {
        return id;
    }

    public String getNomeCrianca() {
        return nomeCrianca;
    }

    public String getNomeResponsavel() {
        return nomeResponsavel;
    }

    public LocalDate getDataNascimento() {
        return dataNascimento;
    }

    public String getSexo() {
        return sexo;
    }

    public Endereco getEndereco() {
        return endereco;
    }

    public List<Telefone> getTelefones() {
        return Collections.unmodifiableList(telefones);
    }

    public PlanoSaude getPlanoSaude() {
        return planoSaude;
    }

    /**
     * Retorna histórico de peso e altura em ordem cronológica (ascendente).
     */
    public List<PesoAltura> getHistoricoPesoAltura() {
        return Collections.unmodifiableList(historicoPesoAltura);
    }

    /**
     * Retorna IDs dos prontuários realizados para este paciente.
     */
    public List<Integer> getProntuarioIds() {
        return Collections.unmodifiableList(prontuarioIds);
    }

    public boolean isParticular() {
        return planoSaude == null;
    }

    /**
     * Adiciona uma leitura de peso/altura ao histórico.
     * 
     * @param pesoAltura Leitura de peso e altura com data
     */
    public void adicionarPesoAltura(PesoAltura pesoAltura) {
        Objects.requireNonNull(pesoAltura, "PesoAltura não pode ser nulo");
        historicoPesoAltura.add(pesoAltura);
        // Lista mantém ordem natural (por data de inserção)
    }

    /**
     * Retorna o último peso/altura registrado (mais recente).
     */
    public java.util.Optional<PesoAltura> getUltimoPesoAltura() {
        if (historicoPesoAltura.isEmpty()) {
            return java.util.Optional.empty();
        }
        return java.util.Optional.of(historicoPesoAltura.get(historicoPesoAltura.size() - 1));
    }

    /**
     * Registra que um prontuário foi realizado para este paciente.
     * 
     * @param prontuarioId ID do prontuário criado
     */
    public void registrarProntuario(Integer prontuarioId) {
        Objects.requireNonNull(prontuarioId, "ProntuarioId não pode ser nulo");
        if (!prontuarioIds.contains(prontuarioId)) {
            prontuarioIds.add(prontuarioId);
        }
    }
}
