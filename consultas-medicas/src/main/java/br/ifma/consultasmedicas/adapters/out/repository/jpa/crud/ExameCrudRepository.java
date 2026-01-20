package br.ifma.consultasmedicas.adapters.out.repository.jpa.crud;


import br.ifma.consultasmedicas.adapters.out.repository.jpa.entity.ExameJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ExameCrudRepository
        extends JpaRepository<ExameJpaEntity, Integer> {
}
