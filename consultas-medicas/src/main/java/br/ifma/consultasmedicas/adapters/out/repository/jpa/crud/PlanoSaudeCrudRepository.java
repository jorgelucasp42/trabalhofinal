package br.ifma.consultasmedicas.adapters.out.repository.jpa.crud;

import br.ifma.consultasmedicas.adapters.out.repository.jpa.entity.PlanoSaudeJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PlanoSaudeCrudRepository
        extends JpaRepository<PlanoSaudeJpaEntity, Integer> {
}
