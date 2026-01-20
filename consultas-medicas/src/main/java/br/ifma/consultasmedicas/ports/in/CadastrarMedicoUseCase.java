package br.ifma.consultasmedicas.ports.in;

/**
 * Porta de entrada para cadastro de novo médico.
 * Retorna o ID do médico criado.
 */
public interface CadastrarMedicoUseCase {
    Integer cadastrar(CadastrarMedicoCommand command);
}
