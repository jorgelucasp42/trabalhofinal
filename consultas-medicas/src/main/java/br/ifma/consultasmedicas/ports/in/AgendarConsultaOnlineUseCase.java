package br.ifma.consultasmedicas.ports.in;

/**
 * Caso de uso: Agendar consulta online.
 * 
 * Responsabilidade: Orquestrar o agendamento de uma consulta online,
 * validando disponibilidade do médico e gerando link de videoconferência.
 */
public interface AgendarConsultaOnlineUseCase {
    Integer agendar(AgendarConsultaOnlineCommand command);
}
