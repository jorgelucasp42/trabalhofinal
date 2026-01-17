package br.ifma.consultasmedicas;

import br.ifma.consultasmedicas.adapters.in.controller.ProntuarioController;
import br.ifma.consultasmedicas.adapters.out.repository.*;
import br.ifma.consultasmedicas.core.domain.exception.DomainException;
import br.ifma.consultasmedicas.core.domain.model.*;
import br.ifma.consultasmedicas.core.service.RegistrarProntuarioService;
import br.ifma.consultasmedicas.ports.in.RegistrarProntuarioCommand;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class ProntuarioControllerTest {

    private ProntuarioController controller;
    private RegistrarProntuarioService service;
    private InMemoryConsultaRepository consultaRepo;
    private InMemoryProntuarioRepository prontuarioRepo;
    private InMemoryMedicamentoRepository medicamentoRepo;
    private InMemoryExameRepository exameRepo;
    private InMemoryIdGenerator idGenerator;

    @BeforeEach
    void setup() {
        consultaRepo = new InMemoryConsultaRepository();
        prontuarioRepo = new InMemoryProntuarioRepository();
        medicamentoRepo = new InMemoryMedicamentoRepository();
        exameRepo = new InMemoryExameRepository();
        idGenerator = new InMemoryIdGenerator(100);

        // Seed
        medicamentoRepo.salvar(new Medicamento(1, "Paracetamol"));
        exameRepo.salvar(new Exame(1, "Hemograma"));

        // Setup service e controller
        service = new RegistrarProntuarioService(consultaRepo, prontuarioRepo,
                medicamentoRepo, exameRepo, idGenerator);
        controller = new ProntuarioController(service);
    }

    @Test
    void deveValidarConsultaIdObrigatorio() {
        var cmd = new RegistrarProntuarioCommand(
                null, 12.0, 0.80, "Febre", "Obs",
                List.of(new RegistrarProntuarioCommand.PrescricaoItemCommand(1, "x", "y", "z")),
                List.of());

        assertThrows(IllegalArgumentException.class,
                () -> controller.registrarProntuario(cmd),
                "Deve validar consultaId obrigatório");
    }

    @Test
    void deveValidarConsultaIdPositivo() {
        var cmd = new RegistrarProntuarioCommand(
                -1, 12.0, 0.80, "Febre", "Obs",
                List.of(new RegistrarProntuarioCommand.PrescricaoItemCommand(1, "x", "y", "z")),
                List.of());

        assertThrows(IllegalArgumentException.class,
                () -> controller.registrarProntuario(cmd),
                "Deve validar consultaId positivo");
    }

    @Test
    void deveValidarPesoPositivo() {
        setupConsulta();

        var cmd = new RegistrarProntuarioCommand(
                10, -1.0, 0.80, "Febre", "Obs",
                List.of(new RegistrarProntuarioCommand.PrescricaoItemCommand(1, "x", "y", "z")),
                List.of());

        assertThrows(IllegalArgumentException.class,
                () -> controller.registrarProntuario(cmd),
                "Deve validar peso positivo");
    }

    @Test
    void deveValidarAlturaPositiva() {
        setupConsulta();

        var cmd = new RegistrarProntuarioCommand(
                10, 12.0, 0.0, "Febre", "Obs",
                List.of(new RegistrarProntuarioCommand.PrescricaoItemCommand(1, "x", "y", "z")),
                List.of());

        assertThrows(IllegalArgumentException.class,
                () -> controller.registrarProntuario(cmd),
                "Deve validar altura positiva");
    }

    @Test
    void deveRetornarDTOCorretamente() {
        setupConsulta();

        var cmd = new RegistrarProntuarioCommand(
                10, 12.0, 0.80, "Febre", "Obs",
                List.of(new RegistrarProntuarioCommand.PrescricaoItemCommand(1, "10mg", "VO", "3 dias")),
                List.of(1));

        var response = controller.registrarProntuario(cmd);

        assertNotNull(response, "Response não pode ser nulo");
        assertNotNull(response.getProntuarioId(), "ProntuarioId não pode ser nulo");
        assertTrue(response.getProntuarioId() > 0, "ProntuarioId deve ser positivo");
        assertNotNull(response.getMensagem(), "Mensagem não pode ser nula");
        assertTrue(response.getMensagem().contains("sucesso"), "Mensagem deve indicar sucesso");
    }

    @Test
    void deveRelancarDomainException() {
        // Não setup a consulta, então vai falhar ao buscar
        var cmd = new RegistrarProntuarioCommand(
                999, 12.0, 0.80, "Febre", "Obs",
                List.of(new RegistrarProntuarioCommand.PrescricaoItemCommand(1, "x", "y", "z")),
                List.of());

        assertThrows(DomainException.class,
                () -> controller.registrarProntuario(cmd),
                "Deve relançar DomainException");
    }

    @Test
    void deveValidarCommandNulo() {
        assertThrows(IllegalArgumentException.class,
                () -> controller.registrarProntuario(null),
                "Deve validar comando nulo");
    }

    // Método auxiliar para setup de consulta
    private void setupConsulta() {
        Endereco endereco = new Endereco("Rua A", "1", null, "Centro", "São Luís", "MA", "65000-000");
        Paciente paciente = new Paciente(1, "João", "Pai", LocalDate.of(2019, 1, 1), "M",
                endereco, List.of(new Telefone("9800000000", TelefoneTipo.CELULAR, "Pai")), null);
        Medico medico = new Medico(1, "Dr. Vilegas", "CRM-MA 12345");
        Consulta consulta = new Consulta(10, paciente, medico, LocalDateTime.now(), false);
        consultaRepo.salvar(consulta);
    }
}
