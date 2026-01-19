package br.ifma.consultasmedicas.ports.in;

/**
 * Caso de uso: Visualizar histórico de consultas e prontuários.
 * 
 * Responsabilidade: Recuperar histórico clínico completo do paciente,
 * incluindo todas as consultas e prontuários associados.
 */
public interface VisualizarHistoricoOnlineUseCase {
    HistoricoOnlineResponse obterHistorico(Integer pacienteId);
}
