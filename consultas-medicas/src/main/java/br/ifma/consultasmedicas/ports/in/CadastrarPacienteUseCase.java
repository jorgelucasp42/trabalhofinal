package br.ifma.consultasmedicas.ports.in;

/**
 * Porta de entrada para cadastro de novo paciente.
 * Retorna o ID do paciente criado.
 */
public interface CadastrarPacienteUseCase {
    Integer cadastrar(CadastrarPacienteCommand command);
}
