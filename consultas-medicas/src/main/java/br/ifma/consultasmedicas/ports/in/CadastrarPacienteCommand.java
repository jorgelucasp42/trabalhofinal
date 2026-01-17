package br.ifma.consultasmedicas.ports.in;

import br.ifma.consultasmedicas.core.domain.model.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

/**
 * Comando para Cadastro de Paciente.
 * Encapsula os dados necessários para criar um novo paciente.
 */
public class CadastrarPacienteCommand {
    private final String nomeCrianca;
    private final String nomeResponsavel;
    private final LocalDate dataNascimento;
    private final String sexo;
    private final EnderecoCommand endereco;
    private final List<TelefoneCommand> telefones;
    private final Integer planoSaudeId; // null = particular

    public CadastrarPacienteCommand(
            String nomeCrianca,
            String nomeResponsavel,
            LocalDate dataNascimento,
            String sexo,
            EnderecoCommand endereco,
            List<TelefoneCommand> telefones,
            Integer planoSaudeId) {
        this.nomeCrianca = Objects.requireNonNull(nomeCrianca);
        this.nomeResponsavel = Objects.requireNonNull(nomeResponsavel);
        this.dataNascimento = Objects.requireNonNull(dataNascimento);
        this.sexo = Objects.requireNonNull(sexo);
        this.endereco = Objects.requireNonNull(endereco);
        this.telefones = telefones;
        this.planoSaudeId = planoSaudeId;
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

    public EnderecoCommand getEndereco() {
        return endereco;
    }

    public List<TelefoneCommand> getTelefones() {
        return telefones;
    }

    public Integer getPlanoSaudeId() {
        return planoSaudeId;
    }

    // Value objects para sub-dados
    public static class EnderecoCommand {
        private final String logradouro;
        private final String numero;
        private final String complemento;
        private final String bairro;
        private final String cidade;
        private final String estadoUf;
        private final String cep;

        public EnderecoCommand(String logradouro, String numero, String complemento,
                String bairro, String cidade, String estadoUf, String cep) {
            this.logradouro = Objects.requireNonNull(logradouro);
            this.numero = Objects.requireNonNull(numero);
            this.complemento = complemento;
            this.bairro = Objects.requireNonNull(bairro);
            this.cidade = Objects.requireNonNull(cidade);
            this.estadoUf = Objects.requireNonNull(estadoUf);
            this.cep = Objects.requireNonNull(cep);
        }

        public String getLogradouro() {
            return logradouro;
        }

        public String getNumero() {
            return numero;
        }

        public String getComplemento() {
            return complemento;
        }

        public String getBairro() {
            return bairro;
        }

        public String getCidade() {
            return cidade;
        }

        public String getEstadoUf() {
            return estadoUf;
        }

        public String getCep() {
            return cep;
        }
    }

    public static class TelefoneCommand {
        private final String numero;
        private final TelefoneTipo tipo;
        private final String responsavel;

        public TelefoneCommand(String numero, TelefoneTipo tipo, String responsavel) {
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

}
