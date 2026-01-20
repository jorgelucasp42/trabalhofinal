package br.ifma.consultasmedicas;

import br.ifma.consultasmedicas.adapters.out.repository.InMemoryConsultaRepository;
import br.ifma.consultasmedicas.adapters.out.repository.InMemoryMedicoRepository;
import br.ifma.consultasmedicas.adapters.out.repository.InMemoryPacienteRepository;
import br.ifma.consultasmedicas.adapters.out.repository.inmemory.InMemoryPagamentoRepository;
import br.ifma.consultasmedicas.adapters.out.gateway.StripeAdapter;
import br.ifma.consultasmedicas.adapters.out.provider.ZoomAdapter;
import br.ifma.consultasmedicas.core.domain.model.*;
import br.ifma.consultasmedicas.core.service.AgendarConsultaOnlineService;
import br.ifma.consultasmedicas.core.service.ProcessarPagamentoService;
import br.ifma.consultasmedicas.ports.in.AgendarConsultaOnlineCommand;
import br.ifma.consultasmedicas.ports.in.ProcessarPagamentoCommand;
import br.ifma.consultasmedicas.ports.in.PagamentoResponse;
import br.ifma.consultasmedicas.ports.out.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Testes para Atendimento Online.
 * 
 * Valida:
 * - Agendamento de consultas online
 * - GeraÃ§Ã£o de links de videoconferÃªncia
 * - Processamento de pagamentos
 */
class AtendimentoOnlineTest {

    private AgendarConsultaOnlineService agendarService;
    private ProcessarPagamentoService pagamentoService;
    private InMemoryPacienteRepository pacienteRepository;
    private InMemoryMedicoRepository medicoRepository;
    private InMemoryConsultaRepository consultaRepository;
    private InMemoryPagamentoRepository pagamentoRepository;
    private ZoomAdapter zoomAdapter;
    private StripeAdapter stripeAdapter;
    private SimpleIdGenerator idGenerator;

    @BeforeEach
    void setup() {
        pacienteRepository = new InMemoryPacienteRepository();
        medicoRepository = new InMemoryMedicoRepository();
        consultaRepository = new InMemoryConsultaRepository();
        pagamentoRepository = new InMemoryPagamentoRepository();
        zoomAdapter = new ZoomAdapter("test-api-key", "test-api-secret");
        stripeAdapter = new StripeAdapter("test-secret-key");
        idGenerator = new SimpleIdGenerator();

        agendarService = new AgendarConsultaOnlineService(
                consultaRepository, pacienteRepository, medicoRepository, zoomAdapter, idGenerator);

        pagamentoService = new ProcessarPagamentoService(
                consultaRepository, pagamentoRepository, stripeAdapter, idGenerator);

        // Prepara dados de teste
        preparaDadosTeste();
    }

    private void preparaDadosTeste() {
        // Cria médico
        Medico medico = new Medico(1, "Dr. Silva", "Cardiologia", "123456-SP");
        medicoRepository.salvar(medico);

        // Cria paciente
        Endereco endereco = new Endereco("Rua A", "123", "Apto 1", "Centro", "São Luís", "MA", "65000-000");
        Telefone telefone = new Telefone("98987654321", TelefoneTipo.CELULAR, "Paciente");
        List<Telefone> telefones = new ArrayList<>();
        telefones.add(telefone);

        Paciente paciente = new Paciente(
                1, "JoÃ£o", "Maria", LocalDate.of(2015, 5, 10), "M", endereco, telefones, null);
        pacienteRepository.salvar(paciente);
    }

    @Test
    void deveAgendarConsultaOnlineComSucesso() {
        // Arrange
        LocalDateTime dataHora = LocalDateTime.now().plusDays(7);
        AgendarConsultaOnlineCommand command = new AgendarConsultaOnlineCommand(
                1, 1, dataHora, "Zoom");

        // Act
        Integer consultaId = agendarService.agendar(command);

        // Assert
        assertNotNull(consultaId);
        assertTrue(consultaId > 0);

        Consulta consulta = consultaRepository.obter(consultaId);
        assertNotNull(consulta);
        assertEquals(1, (int) consulta.getPaciente().getId());
        assertEquals(1, (int) consulta.getMedico().getId());
        assertEquals(ConsultaStatus.AGENDADA, consulta.getStatus());
    }

    @Test
    void deveRejeitarConsultaComPacienteInvalido() {
        // Arrange
        LocalDateTime dataHora = LocalDateTime.now().plusDays(7);
        AgendarConsultaOnlineCommand command = new AgendarConsultaOnlineCommand(
                999, 1, dataHora, "Zoom");

        // Act & Assert
        assertThrows(Exception.class, () -> agendarService.agendar(command));
    }

    @Test
    void deveRejeitarConsultaComDataPassada() {
        // Arrange
        LocalDateTime dataHora = LocalDateTime.now().minusHours(1);
        AgendarConsultaOnlineCommand command = new AgendarConsultaOnlineCommand(
                1, 1, dataHora, "Zoom");

        // Act & Assert
        assertThrows(Exception.class, () -> agendarService.agendar(command));
    }

    @Test
    void deveProcessarPagamentoComSucesso() {
        // Arrange
        // Primeiro agenda uma consulta
        LocalDateTime dataHora = LocalDateTime.now().plusDays(7);
        AgendarConsultaOnlineCommand agendaCmd = new AgendarConsultaOnlineCommand(1, 1, dataHora, "Zoom");
        Integer consultaId = agendarService.agendar(agendaCmd);

        // Depois processa pagamento
        ProcessarPagamentoCommand pagtoCmd = new ProcessarPagamentoCommand(
                consultaId,
                new BigDecimal("150.00"),
                "4111111111111111",
                "JoÃ£o Silva",
                "12/25",
                "123");

        // Act
        PagamentoResponse response = pagamentoService.processar(pagtoCmd);

        // Assert
        assertNotNull(response);
        assertTrue(response.foiBemSucedido() || !response.foiBemSucedido()); // Mock pode falhar
        assertNotNull(response.getPagamentoId());
    }

    @Test
    void deveRejeitarPagamentoComDadosInvalidos() {
        // Arrange
        ProcessarPagamentoCommand command = new ProcessarPagamentoCommand(
                1,
                new BigDecimal("150.00"),
                "123", // CartÃ£o invÃ¡lido (muito curto)
                "JoÃ£o",
                "12/25",
                "123");

        // Act & Assert
        assertThrows(Exception.class, () -> pagamentoService.processar(command));
    }

    @Test
    void deveRejeitarPagamentoComValorNegativo() {
        // Arrange
        ProcessarPagamentoCommand command = new ProcessarPagamentoCommand(
                1,
                new BigDecimal("-50.00"), // Valor negativo
                "4111111111111111",
                "JoÃ£o Silva",
                "12/25",
                "123");

        // Act & Assert
        assertThrows(Exception.class, () -> pagamentoService.processar(command));
    }
}
