package br.ifma.consultasmedicas.adapters.out.repository.jpa.crud;

import br.ifma.consultasmedicas.adapters.out.repository.jpa.entity.PagamentoJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PagamentoCrudRepository extends JpaRepository<PagamentoJpaEntity, Integer> {
    Optional<PagamentoJpaEntity> findByConsultaId(Integer consultaId);
}