package br.ifma.consultasmedicas.adapters.out.repository.jpa.crud;

import br.ifma.consultasmedicas.adapters.out.repository.jpa.entity.MedicoJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MedicoCrudRepository
        extends JpaRepository<MedicoJpaEntity, Integer> {
}
