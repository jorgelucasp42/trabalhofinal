package br.ifma.consultasmedicas.adapters.out.repository.jpa.crud;

import br.ifma.consultasmedicas.adapters.out.repository.jpa.entity.ConsultaJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * CRUD Repository para Consulta usando Spring Data JPA.
 */
@Repository
public interface ConsultaCrudRepository extends JpaRepository<ConsultaJpaEntity, Integer> {

    @Query("SELECT c FROM ConsultaJpaEntity c WHERE CAST(c.dataHora AS date) = :data ORDER BY c.dataHora ASC")
    List<ConsultaJpaEntity> findByData(@Param("data") LocalDate data);

    Optional<ConsultaJpaEntity> findById(Integer id);
}
