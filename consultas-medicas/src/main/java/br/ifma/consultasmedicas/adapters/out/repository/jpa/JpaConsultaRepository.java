package br.ifma.consultasmedicas.adapters.out.repository.jpa;

import br.ifma.consultasmedicas.adapters.out.repository.jpa.crud.ConsultaCrudRepository;
import br.ifma.consultasmedicas.adapters.out.repository.jpa.entity.ConsultaJpaEntity;
import br.ifma.consultasmedicas.core.domain.model.Consulta;
import br.ifma.consultasmedicas.core.domain.model.ConsultaStatus;
import br.ifma.consultasmedicas.ports.out.ConsultaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Implementação JPA de ConsultaRepository.
 * Responsável por mapear domínio ↔ banco de dados.
 */
@Repository
public class JpaConsultaRepository implements ConsultaRepository {

    private final ConsultaCrudRepository crudRepository;

    public JpaConsultaRepository(ConsultaCrudRepository crudRepository) {
        this.crudRepository = crudRepository;
    }

    @Override
    public Optional<Consulta> buscarPorId(Integer id) {
        return crudRepository.findById(id)
                .map(this::mapToDomain);
    }

    @Override
    public List<Consulta> buscarPorData(LocalDate data) {
        return crudRepository.findByData(data)
                .stream()
                .map(this::mapToDomain)
                .collect(Collectors.toList());
    }

    @Override
    public void salvar(Consulta consulta) {
        ConsultaJpaEntity entity = new ConsultaJpaEntity(
                consulta.getId(),
                consulta.getPaciente().getId(),
                consulta.getMedico().getId(),
                consulta.getDataHora().toString(),
                consulta.isPacienteNovo(),
                consulta.getStatus().toString());
        crudRepository.save(entity);
    }

    private Consulta mapToDomain(ConsultaJpaEntity entity) {
        // Nota: Esta implementação simplificada
        // Em produção, seria necessário recuperar Paciente e Medico completos
        Consulta consulta = new Consulta(
                entity.getId(),
                null, // Seria necessário buscar Paciente do repository
                null, // Seria necessário buscar Medico do repository
                LocalDateTime.parse(entity.getDataHora()),
                entity.isPacienteNovo());

        // Restaurar status
        if (entity.getStatus().equals(ConsultaStatus.REALIZADA.toString())) {
            consulta.marcarRealizada();
        } else if (entity.getStatus().equals(ConsultaStatus.CANCELADA.toString())) {
            consulta.cancelar();
        }

        return consulta;
    }
}
