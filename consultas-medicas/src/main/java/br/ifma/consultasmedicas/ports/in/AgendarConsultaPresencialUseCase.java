package br.ifma.consultasmedicas.ports.in;

/**
 * Caso de uso: Agendar consulta presencial.
 * 
 * Responsabilidade: Orquestrar o agendamento de uma consulta presencial,
 * validando disponibilidade do médico.
 */
public interface AgendarConsultaPresencialUseCase {
    Integer agendar(AgendarConsultaPresencialCommand command);
}
