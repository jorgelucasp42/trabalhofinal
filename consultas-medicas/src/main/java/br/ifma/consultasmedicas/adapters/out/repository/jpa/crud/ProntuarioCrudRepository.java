package br.ifma.consultasmedicas.adapters.out.repository.jpa.crud;

import br.ifma.consultasmedicas.adapters.out.repository.jpa.entity.ProntuarioJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * CRUD Repository para Prontuário usando Spring Data JPA.
 */
@Repository
public interface ProntuarioCrudRepository extends JpaRepository<ProntuarioJpaEntity, Integer> {
    List<ProntuarioJpaEntity> findByConsultaId(Integer consultaId);
}
