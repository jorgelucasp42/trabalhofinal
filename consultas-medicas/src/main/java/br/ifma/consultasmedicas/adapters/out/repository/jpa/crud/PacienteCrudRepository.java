package br.ifma.consultasmedicas.adapters.out.repository.jpa.crud;

import br.ifma.consultasmedicas.adapters.out.repository.jpa.entity.PacienteJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PacienteCrudRepository
        extends JpaRepository<PacienteJpaEntity, Integer> {
}

