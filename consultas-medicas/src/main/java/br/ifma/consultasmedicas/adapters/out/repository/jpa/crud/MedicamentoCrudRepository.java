package br.ifma.consultasmedicas.adapters.out.repository.jpa.crud;

import br.ifma.consultasmedicas.adapters.out.repository.jpa.entity.MedicamentoJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MedicamentoCrudRepository
        extends JpaRepository<MedicamentoJpaEntity, Integer> {
}
