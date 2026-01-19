package br.ifma.consultasmedicas;

import br.ifma.consultasmedicas.adapters.out.repository.*;
import br.ifma.consultasmedicas.core.domain.model.*;
import br.ifma.consultasmedicas.core.service.ConsultarHistoricoProntuarioService;
import br.ifma.consultasmedicas.ports.in.RegistrarProntuarioCommand;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class ConsultarHistoricoProntuarioServiceTest {

    @Test
    void deveConsultarHistoricoVazio() {
        var prontuarioRepo = new InMemoryProntuarioRepository();
        var service = new ConsultarHistoricoProntuarioService(prontuarioRepo);

        List<Prontuario> resultado = service.consultarPorPaciente(999);

        assertNotNull(resultado);
        assertTrue(resultado.isEmpty(), "HistÃ³rico deve estar vazio para paciente inexistente");
    }

    @Test
    void deveConsultarHistoricoComProntuarios() {
        var prontuarioRepo = new InMemoryProntuarioRepository();
        var consultaRepo = new InMemoryConsultaRepository();
        var medicamentoRepo = new InMemoryMedicamentoRepository();
        var exameRepo = new InMemoryExameRepository();
        var idGenerator = new InMemoryIdGenerator(100);

        // Setup
        medicamentoRepo.salvar(new Medicamento(1, "Paracetamol"));
        exameRepo.salvar(new Exame(1, "Hemograma"));

        Endereco endereco = new Endereco("Rua A", "1", null, "Centro", "SÃ£o LuÃ­s", "MA", "65000-000");
        Paciente paciente = new Paciente(1, "JoÃ£o", "Pai", LocalDate.of(2019, 1, 1), "M",
                endereco, List.of(new Telefone("9800000000", TelefoneTipo.CELULAR, "Pai")), null);

        Medico medico = new Medico(1, "Dr. Vilegas", "Pediatria", "CRM-MA 12345");

        // Criar consulta 1
        Consulta consulta1 = new Consulta(10, paciente, medico, LocalDateTime.of(2024, 1, 15, 9, 0), false);
        consultaRepo.salvar(consulta1);

        // Criar prontuÃ¡rio 1
        Prontuario prontuario1 = new Prontuario(100, consulta1, 12.0, 0.80, "Febre", "Obs 1",
                List.of(), List.of(exameRepo.buscarPorId(1).get()));
        prontuarioRepo.salvar(prontuario1);

        // Criar consulta 2
        Consulta consulta2 = new Consulta(11, paciente, medico, LocalDateTime.of(2024, 2, 15, 10, 0), false);
        consultaRepo.salvar(consulta2);

        // Criar prontuÃ¡rio 2
        Prontuario prontuario2 = new Prontuario(101, consulta2, 12.5, 0.81, "Tosse", "Obs 2",
                List.of(), List.of(exameRepo.buscarPorId(1).get()));
        prontuarioRepo.salvar(prontuario2);

        var service = new ConsultarHistoricoProntuarioService(prontuarioRepo);

        // Act
        List<Prontuario> resultado = service.consultarPorPaciente(1);

        // Assert
        assertNotNull(resultado);
        assertEquals(2, resultado.size(), "Deve retornar 2 prontuÃ¡rios do paciente");
        assertTrue(resultado.contains(prontuario1), "Deve conter prontuÃ¡rio 1");
        assertTrue(resultado.contains(prontuario2), "Deve conter prontuÃ¡rio 2");
    }

    @Test
    void deveRetornarOrdenadoPorData() {
        var prontuarioRepo = new InMemoryProntuarioRepository();
        var consultaRepo = new InMemoryConsultaRepository();
        var idGenerator = new InMemoryIdGenerator(100);

        Endereco endereco = new Endereco("Rua A", "1", null, "Centro", "SÃ£o LuÃ­s", "MA", "65000-000");
        Paciente paciente = new Paciente(1, "JoÃ£o", "Pai", LocalDate.of(2019, 1, 1), "M",
                endereco, List.of(new Telefone("9800000000", TelefoneTipo.CELULAR, "Pai")), null);

        Medico medico = new Medico(1, "Dr. Vilegas", "Pediatria", "CRM-MA 12345");

        // Criar consultas em ordem reversa
        Consulta consulta1 = new Consulta(10, paciente, medico, LocalDateTime.of(2024, 3, 15, 9, 0), false);
        Consulta consulta2 = new Consulta(11, paciente, medico, LocalDateTime.of(2024, 1, 15, 10, 0), false);
        Consulta consulta3 = new Consulta(12, paciente, medico, LocalDateTime.of(2024, 2, 15, 11, 0), false);

        consultaRepo.salvar(consulta1);
        consultaRepo.salvar(consulta2);
        consultaRepo.salvar(consulta3);

        Prontuario p1 = new Prontuario(100, consulta1, 12.0, 0.80, "Febre", "Obs", List.of(), List.of());
        Prontuario p2 = new Prontuario(101, consulta2, 12.0, 0.80, "Febre", "Obs", List.of(), List.of());
        Prontuario p3 = new Prontuario(102, consulta3, 12.0, 0.80, "Febre", "Obs", List.of(), List.of());

        prontuarioRepo.salvar(p1);
        prontuarioRepo.salvar(p2);
        prontuarioRepo.salvar(p3);

        var service = new ConsultarHistoricoProntuarioService(prontuarioRepo);

        // Act
        List<Prontuario> resultado = service.consultarPorPaciente(1);

        // Assert
        assertEquals(3, resultado.size());
        assertEquals(p2.getId(), resultado.get(0).getId(), "Primeiro deve ser janeiro");
        assertEquals(p3.getId(), resultado.get(1).getId(), "Segundo deve ser fevereiro");
        assertEquals(p1.getId(), resultado.get(2).getId(), "Terceiro deve ser marÃ§o");
    }

    @Test
    void deveRetornarApenasDoMesmoPaciente() {
        var prontuarioRepo = new InMemoryProntuarioRepository();
        var consultaRepo = new InMemoryConsultaRepository();
        var idGenerator = new InMemoryIdGenerator(100);

        Endereco endereco = new Endereco("Rua A", "1", null, "Centro", "SÃ£o LuÃ­s", "MA", "65000-000");
        Paciente paciente1 = new Paciente(1, "JoÃ£o", "Pai", LocalDate.of(2019, 1, 1), "M",
                endereco, List.of(new Telefone("9800000000", TelefoneTipo.CELULAR, "Pai")), null);
        Paciente paciente2 = new Paciente(2, "Maria", "MÃ£e", LocalDate.of(2020, 5, 3), "F",
                endereco, List.of(new Telefone("9800000001", TelefoneTipo.CELULAR, "MÃ£e")), null);

        Medico medico = new Medico(1, "Dr. Vilegas", "Pediatria", "CRM-MA 12345");

        // Criar consultas de 2 pacientes
        Consulta c1 = new Consulta(10, paciente1, medico, LocalDateTime.of(2024, 1, 15, 9, 0), false);
        Consulta c2 = new Consulta(11, paciente2, medico, LocalDateTime.of(2024, 1, 15, 10, 0), false);

        consultaRepo.salvar(c1);
        consultaRepo.salvar(c2);

        Prontuario p1 = new Prontuario(100, c1, 12.0, 0.80, "Febre", "Obs", List.of(), List.of());
        Prontuario p2 = new Prontuario(101, c2, 13.0, 0.85, "Febre", "Obs", List.of(), List.of());

        prontuarioRepo.salvar(p1);
        prontuarioRepo.salvar(p2);

        var service = new ConsultarHistoricoProntuarioService(prontuarioRepo);

        // Act - consultar apenas paciente1
        List<Prontuario> resultado = service.consultarPorPaciente(1);

        // Assert
        assertEquals(1, resultado.size(), "Deve retornar apenas 1 prontuÃ¡rio do paciente 1");
        assertEquals(p1.getId(), resultado.get(0).getId());
    }

}

