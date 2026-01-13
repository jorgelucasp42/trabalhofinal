package br.ifma.consultasmedicas.ports.out;

/**
 * Porta de saída para geração de IDs.
 * Responsabilidade: gerar identificadores únicos para entidades.
 * 
 * Implementações podem usar:
 * - Sequência de banco de dados
 * - UUID
 * - Snowflake ID
 * - Simples incrementador (para testes)
 */
public interface IdGenerator {
    Integer gerarId();
}
