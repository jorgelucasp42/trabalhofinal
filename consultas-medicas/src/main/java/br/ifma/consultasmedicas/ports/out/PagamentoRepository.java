package br.ifma.consultasmedicas.ports.out;

import br.ifma.consultasmedicas.core.domain.model.Pagamento;

/**
 * Porta de saída para persistência de pagamentos.
 * 
 * Responsabilidade: Abstrair a persistência de agregados Pagamento,
 * permitindo diferentes implementações (JPA, MongoDB, etc).
 */
public interface PagamentoRepository {
    void salvar(Pagamento pagamento);

    Pagamento obter(Integer id);

    Pagamento obterPorConsultaId(Integer consultaId);
}
