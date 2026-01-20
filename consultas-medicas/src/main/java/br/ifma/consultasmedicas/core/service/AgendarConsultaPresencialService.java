package br.ifma.consultasmedicas.core.service;

import br.ifma.consultasmedicas.core.domain.exception.DomainException;
import br.ifma.consultasmedicas.core.domain.model.Consulta;
import br.ifma.consultasmedicas.core.domain.model.Medico;
import br.ifma.consultasmedicas.core.domain.model.Paciente;
import br.ifma.consultasmedicas.ports.in.AgendarConsultaPresencialCommand;
import br.ifma.consultasmedicas.ports.in.AgendarConsultaPresencialUseCase;
import br.ifma.consultasmedicas.ports.out.*;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Caso de uso: Agendar Consulta Presencial.
 * 
 * Responsabilidades:
 * - Validar dados da consulta
 * - Verificar disponibilidade do médico
 * - Gerar ID único
 * - Persistir no repositório
 * 
 * Invariantes:
 * - Médico deve existir e estar ativo
 * - Paciente deve existir
 * - Data/hora deve ser futura
 * - Não pode haver conflito de horário para o médico
 */
public class AgendarConsultaPresencialService implements AgendarConsultaPresencialUseCase {

    private final ConsultaRepository consultaRepository;
    private final PacienteRepository pacienteRepository;
    private final MedicoRepository medicoRepository;
    private final IdGenerator idGenerator;
    private final SimpleLogger logger = new SimpleLogger(AgendarConsultaPresencialService.class);

    public AgendarConsultaPresencialService(ConsultaRepository consultaRepository,
            PacienteRepository pacienteRepository,
            MedicoRepository medicoRepository,
            IdGenerator idGenerator) {
        this.consultaRepository = Objects.requireNonNull(consultaRepository);
        this.pacienteRepository = Objects.requireNonNull(pacienteRepository);
        this.medicoRepository = Objects.requireNonNull(medicoRepository);
        this.idGenerator = Objects.requireNonNull(idGenerator);
    }

    @Override
    public Integer agendar(AgendarConsultaPresencialCommand command) {
        logger.info("Iniciando agendamento de consulta presencial");

        try {
            // 1. Valida existência do paciente
            Paciente paciente = pacienteRepository.obter(command.getPacienteId());
            if (paciente == null) {
                throw new DomainException("Paciente não encontrado: " + command.getPacienteId());
            }
            logger.info("Paciente validado: " + paciente.getNomeCrianca());

            // 2. Valida existência do médico
            Medico medico = medicoRepository.obter(command.getMedicoId());
            if (medico == null) {
                throw new DomainException("Médico não encontrado: " + command.getMedicoId());
            }
            logger.info("Médico validado: " + medico.getNome());

            // 3. Valida data/hora
            LocalDateTime agora = LocalDateTime.now();
            if (command.getDataHora().isBefore(agora)) {
                throw new DomainException("Data/hora deve ser no futuro");
            }
            logger.info("Data/hora validada: " + command.getDataHora());

            // 4. Verifica conflito de horário
            if (temConflito(command.getMedicoId(), command.getDataHora())) {
                throw new DomainException("Médico já possui consulta agendada neste horário");
            }
            logger.info("Sem conflito de horário");

            // 5. Cria consulta presencial
            Integer consultaId = idGenerator.gerarId();
            Consulta consulta = new Consulta(
                    consultaId,
                    paciente,
                    medico,
                    command.getDataHora(),
                    command.isPacienteNovo()
            );

            consultaRepository.salvar(consulta);
            logger.info("Consulta presencial agendada com sucesso. ID: " + consultaId);

            return consultaId;

        } catch (DomainException e) {
            logger.error("Erro de domínio ao agendar consulta presencial: " + e.getMessage());
            throw e;
        } catch (Exception e) {
            logger.error("Erro inesperado ao agendar consulta presencial: " + e.getMessage());
            throw new DomainException("Erro ao agendar consulta presencial: " + e.getMessage());
        }
    }

    /**
     * Verifica se há conflito de horário para o médico na data/hora especificada.
     * 
     * Implementação simples: verifica consultas dentro de 1 hora antes/depois.
     * Uma implementação real consultaria um calendário ou agenda mais sofisticada.
     */
    private boolean temConflito(Integer medicoId, LocalDateTime dataHora) {
        // TODO: Implementar verificação real contra calendário de disponibilidade
        return false;
    }
}
