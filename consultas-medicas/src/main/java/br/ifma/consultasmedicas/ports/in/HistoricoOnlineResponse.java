package br.ifma.consultasmedicas.ports.in;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Response para visualização de histórico online.
 * Contém todas as consultas e prontuários do paciente.
 */
public class HistoricoOnlineResponse {
    private final Integer pacienteId;
    private final String nomePaciente;
    private final List<ConsultaOnlineInfo> consultas;

    public HistoricoOnlineResponse(Integer pacienteId, String nomePaciente,
            List<ConsultaOnlineInfo> consultas) {
        this.pacienteId = pacienteId;
        this.nomePaciente = nomePaciente;
        this.consultas = consultas != null ? List.copyOf(consultas) : List.of();
    }

    public Integer getPacienteId() {
        return pacienteId;
    }

    public String getNomePaciente() {
        return nomePaciente;
    }

    public List<ConsultaOnlineInfo> getConsultas() {
        return consultas;
    }

    /**
     * Informação resumida de uma consulta para exibição no histórico.
     */
    public static class ConsultaOnlineInfo {
        private final Integer consultaId;
        private final LocalDateTime dataHora;
        private final String nomeMedico;
        private final String especialidade;
        private final String status;
        private final Integer prontuarioId; // null se consulta não foi realizada

        public ConsultaOnlineInfo(Integer consultaId, LocalDateTime dataHora,
                String nomeMedico, String especialidade, String status, Integer prontuarioId) {
            this.consultaId = consultaId;
            this.dataHora = dataHora;
            this.nomeMedico = nomeMedico;
            this.especialidade = especialidade;
            this.status = status;
            this.prontuarioId = prontuarioId;
        }

        public Integer getConsultaId() {
            return consultaId;
        }

        public LocalDateTime getDataHora() {
            return dataHora;
        }

        public String getNomeMedico() {
            return nomeMedico;
        }

        public String getEspecialidade() {
            return especialidade;
        }

        public String getStatus() {
            return status;
        }

        public Integer getProntuarioId() {
            return prontuarioId;
        }
    }
}
