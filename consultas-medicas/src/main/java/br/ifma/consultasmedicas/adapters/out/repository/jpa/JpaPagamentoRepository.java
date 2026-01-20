package br.ifma.consultasmedicas.adapters.out.repository.jpa;

import br.ifma.consultasmedicas.adapters.out.repository.jpa.crud.PagamentoCrudRepository;
import br.ifma.consultasmedicas.adapters.out.repository.jpa.entity.PagamentoJpaEntity;
import br.ifma.consultasmedicas.core.domain.model.Pagamento;
import br.ifma.consultasmedicas.ports.out.PagamentoRepository;
import org.springframework.stereotype.Repository;


@Repository
public class JpaPagamentoRepository implements PagamentoRepository {

    private final PagamentoCrudRepository crudRepository;

    public JpaPagamentoRepository(PagamentoCrudRepository crudRepository) {
        this.crudRepository = crudRepository;
    }

    @Override
    public void salvar(Pagamento pagamento) {
        PagamentoJpaEntity entity = mapToEntity(pagamento);
        crudRepository.save(entity);
    }

    @Override
    public Pagamento obter(Integer id) {
        return crudRepository.findById(id)
                .map(this::mapToDomain)
                .orElse(null);
    }

    @Override
    public Pagamento obterPorConsultaId(Integer consultaId) {
        return crudRepository.findByConsultaId(consultaId)
                .map(this::mapToDomain)
                .orElse(null);
    }

    private PagamentoJpaEntity mapToEntity(Pagamento pagamento) {
        return new PagamentoJpaEntity(
                pagamento.getId(),
                pagamento.getConsultaId(),
                pagamento.getValor(),
                pagamento.getStatus(),
                pagamento.getDataCriacao(),
                pagamento.getDataProcessamento(),
                pagamento.getIdTransacao(),
                pagamento.getMotivo()
        );
    }

    private Pagamento mapToDomain(PagamentoJpaEntity entity) {
        Pagamento pagamento = new Pagamento(
                entity.getId(),
                entity.getConsultaId(),
                entity.getValor()
        );

        // Ajusta status e dados de processamento
        switch (entity.getStatus()) {
            case PROCESSANDO -> pagamento.iniciarProcessamento(entity.getIdTransacao());
            case CONCLUIDO -> {
                pagamento.iniciarProcessamento(entity.getIdTransacao());
                pagamento.marcarComoConcluido();
            }
            case FALHOU -> {
                pagamento.iniciarProcessamento(entity.getIdTransacao());
                pagamento.marcarComoFalho(entity.getMotivo());
            }
            default -> {}
        }

        return pagamento;
    }
}