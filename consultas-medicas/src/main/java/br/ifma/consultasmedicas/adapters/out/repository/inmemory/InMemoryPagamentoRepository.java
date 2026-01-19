package br.ifma.consultasmedicas.adapters.out.repository.inmemory;

import br.ifma.consultasmedicas.core.domain.model.Pagamento;
import br.ifma.consultasmedicas.ports.out.PagamentoRepository;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Implementação em memória de PagamentoRepository.
 * 
 * Útil para testes. Em produção, usar JPA ou implementação similar.
 */
public class InMemoryPagamentoRepository implements PagamentoRepository {
    private final Map<Integer, Pagamento> pagamentos = new HashMap<>();

    @Override
    public void salvar(Pagamento pagamento) {
        Objects.requireNonNull(pagamento, "Pagamento não pode ser nulo");
        pagamentos.put(pagamento.getId(), pagamento);
    }

    @Override
    public Pagamento obter(Integer id) {
        Objects.requireNonNull(id, "ID não pode ser nulo");
        return pagamentos.get(id);
    }

    @Override
    public Pagamento obterPorConsultaId(Integer consultaId) {
        Objects.requireNonNull(consultaId, "ID da consulta não pode ser nulo");
        return pagamentos.values().stream()
                .filter(p -> p.getConsultaId().equals(consultaId))
                .findFirst()
                .orElse(null);
    }

    public void limpar() {
        pagamentos.clear();
    }
}
