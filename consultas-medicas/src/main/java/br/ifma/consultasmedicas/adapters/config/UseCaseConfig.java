package br.ifma.consultasmedicas.adapters.config;

import br.ifma.consultasmedicas.core.service.*;
import br.ifma.consultasmedicas.ports.in.*;
import br.ifma.consultasmedicas.ports.out.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UseCaseConfig {
    @Bean
    public RegistrarProntuarioUseCase registrarProntuarioUseCase(
            ConsultaRepository consultaRepository,
            ProntuarioRepository prontuarioRepository,
            MedicamentoRepository medicamentoRepository,
            ExameRepository exameRepository,
            IdGenerator idGenerator
    ) {
        return new RegistrarProntuarioService(
                consultaRepository,
                prontuarioRepository,
                medicamentoRepository,
                exameRepository,
                idGenerator
        );
    }

    @Bean
    public AgendarConsultaOnlineUseCase agendarConsultaOnlineUseCase(
            ConsultaRepository consultaRepository,
            PacienteRepository pacienteRepository,
            MedicoRepository medicoRepository,
            VideoConferenciaProvider videoConferenciaProvider,
            IdGenerator idGenerator
    ) {
        return new AgendarConsultaOnlineService(
                consultaRepository,
                pacienteRepository,
                medicoRepository,
                videoConferenciaProvider,
                idGenerator
        );
    }
    @Bean
    public ProcessarPagamentoUseCase processarPagamentoUseCase(
            ConsultaRepository consultaRepository,
            PagamentoRepository pagamentoRepository,
            PagamentoGateway pagamentoGateway,
            IdGenerator idGenerator
    ) {
        return new ProcessarPagamentoService(
                consultaRepository,
                pagamentoRepository,
                pagamentoGateway,
                idGenerator
        );
    }

    @Bean
    public CadastrarPacienteUseCase cadastrarPacienteUseCase(
            PacienteRepository pacienteRepository,
            PlanoSaudeRepository planoSaudeRepository,
            IdGenerator idGenerator
    ) {
        return new CadastrarPacienteService(
                pacienteRepository,
                planoSaudeRepository,
                idGenerator
        );
    }

    @Bean
    public ListarConsultasDoDiaUseCase listarConsultasDoDiaUseCase(
            ConsultaRepository consultaRepository
    ) {
        return new ListarConsultasDoDiaService(consultaRepository);
    }

    @Bean
    public ListarMedicosUseCase listarMedicosUseCase(
            MedicoRepository medicoRepository
    ) {
        return new ListarMedicosService(medicoRepository);
    }
}
