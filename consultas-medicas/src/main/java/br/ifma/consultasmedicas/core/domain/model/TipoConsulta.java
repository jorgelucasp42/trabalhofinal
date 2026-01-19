package br.ifma.consultasmedicas.core.domain.model;

/**
 * Enum para tipos de consulta suportados pelo sistema.
 * 
 * Valores:
 * - PRESENCIAL: Atendimento presencial na clínica
 * - ONLINE: Atendimento via videoconferência
 */
public enum TipoConsulta {
    PRESENCIAL("Presencial"),
    ONLINE("Online via Videoconferência");

    private final String descricao;

    TipoConsulta(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }
}
