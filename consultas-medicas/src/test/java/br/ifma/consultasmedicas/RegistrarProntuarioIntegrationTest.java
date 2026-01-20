package br.ifma.consultasmedicas;

import br.ifma.consultasmedicas.adapters.out.repository.InMemoryConsultaRepository;
import br.ifma.consultasmedicas.adapters.out.repository.InMemoryExameRepository;
import br.ifma.consultasmedicas.adapters.out.repository.InMemoryIdGenerator;
import br.ifma.consultasmedicas.adapters.out.repository.InMemoryMedicamentoRepository;
import br.ifma.consultasmedicas.adapters.out.repository.InMemoryProntuarioRepository;
import br.ifma.consultasmedicas.core.domain.model.*;
import br.ifma.consultasmedicas.core.service.ListarConsultasDoDiaService;
import br.ifma.consultasmedicas.core.service.RegistrarProntuarioService;
import br.ifma.consultasmedicas.ports.in.RegistrarProntuarioCommand;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Teste de integraÃ§Ã£o: fluxo completo da aplicaÃ§Ã£o.
 * Testa: Entrada (Controller) â†’ DomÃ­nio (Service) â†’ SaÃ­da (Repository)
 */
public class RegistrarProntuarioIntegrationTest {

    private RegistrarProntuarioService registrarService;
    private ListarConsultasDoDiaService listarService;
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

        registrarService = new RegistrarProntuarioService(consultaRepo, prontuarioRepo,
                medicamentoRepo, exameRepo, idGenerator);
        listarService = new ListarConsultasDoDiaService(consultaRepo);

        // Seed de dados
        medicamentoRepo.salvar(new Medicamento(1, "Paracetamol"));
        exameRepo.salvar(new Exame(1, "Hemograma completo"));
    }

    @Test
    void deveExecutarFluxoCompletoDeRegistroProntuario() {
        // Arrange: Setup de dados iniciais
        Endereco endereco = new Endereco("Rua A", "123", "Apto 10", "Centro",
                "SÃ£o LuÃ­s", "MA", "65000-000");
        Paciente paciente = new Paciente(1, "Ana Silva", "Maria (mÃ£e)",
                LocalDate.of(2020, 5, 3), "F", endereco,
                List.of(new Telefone("98999990000", TelefoneTipo.CELULAR, "Maria")), null);

        Medico medico = new Medico(1, "Dr. Vilegas", "Pediatria", "CRM-MA 12345");
        Consulta consulta = new Consulta(1, paciente, medico,
                LocalDateTime.of(2024, 1, 15, 14, 30), true);

        consultaRepo.salvar(consulta);

        // Act: Listar consultas do dia
        List<Consulta> consultasDia = listarService.listar(LocalDate.of(2024, 1, 15));

        // Assert: Verificar que consulta foi listada
        assertEquals(1, consultasDia.size());
        assertEquals(1, consultasDia.get(0).getId());
        assertEquals(ConsultaStatus.AGENDADA, consultasDia.get(0).getStatus());

        // Act: Registrar prontuÃ¡rio
        var command = new RegistrarProntuarioCommand(
                1,
                15.2,
                0.95,
                "Febre e tosse",
                "Hidratar e observar sinais de alarme",
                List.of(new RegistrarProntuarioCommand.PrescricaoItemCommand(
                        1, "10mg/kg", "VO a cada 6h", "3 dias")),
                List.of(1));

        Integer prontuarioId = registrarService.registrar(command);

        // Assert: Verificar que prontuÃ¡rio foi criado
        assertNotNull(prontuarioId);
        assertTrue(prontuarioId > 0);

        // Assert: Verificar que prontuÃ¡rio estÃ¡ no repositÃ³rio
        assertTrue(prontuarioRepo.buscarPorId(prontuarioId).isPresent());
        Prontuario prontuario = prontuarioRepo.buscarPorId(prontuarioId).get();
        assertEquals(15.2, prontuario.getPeso());
        assertEquals(0.95, prontuario.getAltura());
        assertEquals("Febre e tosse", prontuario.getSintomas());

        // Assert: Verificar que status da consulta foi atualizado
        Consulta consultaAtualizada = consultaRepo.buscarPorId(1).get();
        assertEquals(ConsultaStatus.REALIZADA, consultaAtualizada.getStatus());

        // Assert: Verificar que nÃ£o pode registrar prontuÃ¡rio novamente
        assertThrows(Exception.class, () -> registrarService.registrar(command),
                "Deve impedir registro duplicado de prontuÃ¡rio para mesma consulta");
    }

    @Test
    void deveHandleRegistroProntuarioComMultiplosMedicamentosExames() {
        // Setup adicional de medicamentos
        medicamentoRepo.salvar(new Medicamento(2, "Ibuprofeno"));
        medicamentoRepo.salvar(new Medicamento(3, "Amoxicilina"));
        exameRepo.salvar(new Exame(2, "Raio-X de tÃ³rax"));

        Endereco endereco = new Endereco("Rua B", "456", null, "Bairro",
                "SÃ£o LuÃ­s", "MA", "65000-001");
        Paciente paciente = new Paciente(2, "JoÃ£o Silva", "Pedro (pai)",
                LocalDate.of(2019, 3, 15), "M", endereco,
                List.of(new Telefone("98988880000", TelefoneTipo.CELULAR, "Pedro")), null);

        Medico medico = new Medico(1, "Dr. Vilegas", "Pediatria", "CRM-MA 12345");
        Consulta consulta = new Consulta(2, paciente, medico,
                LocalDateTime.now(), false);

        consultaRepo.salvar(consulta);

        // Act: Registrar com mÃºltiplos medicamentos e exames
        var command = new RegistrarProntuarioCommand(
                2,
                20.0,
                1.0,
                "Tosse persistente",
                "Suspeita de pneumonia",
                List.of(
                        new RegistrarProntuarioCommand.PrescricaoItemCommand(1, "10mg", "VO", "5 dias"),
                        new RegistrarProntuarioCommand.PrescricaoItemCommand(2, "5mg", "VO", "3 dias"),
                        new RegistrarProntuarioCommand.PrescricaoItemCommand(3, "25mg/kg", "VO", "7 dias")),
                List.of(1, 2));

        Integer prontuarioId = registrarService.registrar(command);

        // Assert
        assertNotNull(prontuarioId);
        Prontuario prontuario = prontuarioRepo.buscarPorId(prontuarioId).get();
        assertEquals(3, prontuario.getPrescricoes().size(), "Deve ter 3 prescriÃ§Ãµes");
        assertEquals(2, prontuario.getExames().size(), "Deve ter 2 exames");
    }

    @Test
    void deveValidarRegistroProntuarioComDadosInvalidos() {
        Endereco endereco = new Endereco("Rua C", "789", null, "Centro",
                "SÃ£o LuÃ­s", "MA", "65000-002");
        Paciente paciente = new Paciente(3, "Maria Silva", "Ana (mÃ£e)",
                LocalDate.of(2021, 7, 20), "F", endereco,
                List.of(new Telefone("98977770000", TelefoneTipo.CELULAR, "Ana")), null);

        Medico medico = new Medico(1, "Dr. Vilegas", "Pediatria", "CRM-MA 12345");
        Consulta consulta = new Consulta(3, paciente, medico, LocalDateTime.now(), true);

        consultaRepo.salvar(consulta);

        // Act & Assert: Medicamento nÃ£o existe
        var commandComMedicamentoInvalido = new RegistrarProntuarioCommand(
                3, 15.0, 0.85, "Febre", "Obs",
                List.of(new RegistrarProntuarioCommand.PrescricaoItemCommand(999, "x", "y", "z")),
                List.of());

        assertThrows(Exception.class, () -> registrarService.registrar(commandComMedicamentoInvalido),
                "Deve rejeitar medicamento inexistente");

        // Act & Assert: Exame nÃ£o existe
        var commandComExameInvalido = new RegistrarProntuarioCommand(
                3, 15.0, 0.85, "Febre", "Obs",
                List.of(),
                List.of(999));

        assertThrows(Exception.class, () -> registrarService.registrar(commandComExameInvalido),
                "Deve rejeitar exame inexistente");
    }
}

