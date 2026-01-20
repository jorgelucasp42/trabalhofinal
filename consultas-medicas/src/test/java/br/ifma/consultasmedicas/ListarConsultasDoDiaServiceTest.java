package br.ifma.consultasmedicas;

import br.ifma.consultasmedicas.adapters.out.repository.InMemoryConsultaRepository;
import br.ifma.consultasmedicas.core.domain.model.*;
import br.ifma.consultasmedicas.core.service.ListarConsultasDoDiaService;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class ListarConsultasDoDiaServiceTest {

    @Test
    void deveListarVazioDiaComSemConsultas() {
        var consultaRepo = new InMemoryConsultaRepository();
        var service = new ListarConsultasDoDiaService(consultaRepo);

        List<Consulta> resultado = service.listar(LocalDate.of(2024, 1, 15));

        assertNotNull(resultado);
        assertTrue(resultado.isEmpty(), "Deve retornar vazio quando nÃ£o hÃ¡ consultas");
    }

    @Test
    void deveListarConsultasFiltradasPorData() {
        var consultaRepo = new InMemoryConsultaRepository();

        Endereco endereco = new Endereco("Rua A", "1", null, "Centro", "SÃ£o LuÃ­s", "MA", "65000-000");
        Paciente paciente = new Paciente(1, "JoÃ£o", "Pai", LocalDate.of(2019, 1, 1), "M",
                endereco, List.of(new Telefone("9800000000", TelefoneTipo.CELULAR, "Pai")), null);
        Medico medico = new Medico(1, "Dr. Vilegas", "Pediatria", "CRM-MA 12345");

        // Adicionar consultas em datas diferentes
        LocalDate data1 = LocalDate.of(2024, 1, 15);
        LocalDate data2 = LocalDate.of(2024, 1, 16);

        Consulta c1 = new Consulta(10, paciente, medico, LocalDateTime.of(2024, 1, 15, 9, 0), false);
        Consulta c2 = new Consulta(11, paciente, medico, LocalDateTime.of(2024, 1, 15, 10, 0), false);
        Consulta c3 = new Consulta(12, paciente, medico, LocalDateTime.of(2024, 1, 16, 9, 0), false);

        consultaRepo.salvar(c1);
        consultaRepo.salvar(c2);
        consultaRepo.salvar(c3);

        var service = new ListarConsultasDoDiaService(consultaRepo);

        // Act
        List<Consulta> resultado = service.listar(data1);

        // Assert
        assertEquals(2, resultado.size(), "Deve retornar 2 consultas da data 15");
        assertTrue(resultado.stream().allMatch(c -> c.getDataHora().toLocalDate().equals(data1)),
                "Todas as consultas devem ser da data especificada");
    }

    @Test
    void deveIgnorarOutrosDias() {
        var consultaRepo = new InMemoryConsultaRepository();

        Endereco endereco = new Endereco("Rua A", "1", null, "Centro", "SÃ£o LuÃ­s", "MA", "65000-000");
        Paciente paciente = new Paciente(1, "JoÃ£o", "Pai", LocalDate.of(2019, 1, 1), "M",
                endereco, List.of(new Telefone("9800000000", TelefoneTipo.CELULAR, "Pai")), null);
        Medico medico = new Medico(1, "Dr. Vilegas", "Pediatria", "CRM-MA 12345");

        // Adicionar consultas em vÃ¡rias datas
        for (int dia = 1; dia <= 31; dia++) {
            try {
                Consulta c = new Consulta(100 + dia, paciente, medico,
                        LocalDateTime.of(2024, 1, dia, 9, 0), false);
                consultaRepo.salvar(c);
            } catch (Exception e) {
                // Dia invÃ¡lido, pular
            }
        }

        var service = new ListarConsultasDoDiaService(consultaRepo);

        // Act
        List<Consulta> resultado = service.listar(LocalDate.of(2024, 1, 15));

        // Assert
        assertEquals(1, resultado.size(), "Deve retornar apenas 1 consulta do dia 15");
        assertEquals(LocalDate.of(2024, 1, 15), resultado.get(0).getDataHora().toLocalDate());
    }

    @Test
    void deveRetornarOrdenado() {
        var consultaRepo = new InMemoryConsultaRepository();

        Endereco endereco = new Endereco("Rua A", "1", null, "Centro", "SÃ£o LuÃ­s", "MA", "65000-000");
        Paciente paciente = new Paciente(1, "JoÃ£o", "Pai", LocalDate.of(2019, 1, 1), "M",
                endereco, List.of(new Telefone("9800000000", TelefoneTipo.CELULAR, "Pai")), null);
        Medico medico = new Medico(1, "Dr. Vilegas", "Pediatria", "CRM-MA 12345");

        // Adicionar consultas em ordem reversa de horÃ¡rio
        Consulta c1 = new Consulta(10, paciente, medico, LocalDateTime.of(2024, 1, 15, 15, 0), false);
        Consulta c2 = new Consulta(11, paciente, medico, LocalDateTime.of(2024, 1, 15, 9, 0), false);
        Consulta c3 = new Consulta(12, paciente, medico, LocalDateTime.of(2024, 1, 15, 12, 0), false);

        consultaRepo.salvar(c1);
        consultaRepo.salvar(c2);
        consultaRepo.salvar(c3);

        var service = new ListarConsultasDoDiaService(consultaRepo);

        // Act
        List<Consulta> resultado = service.listar(LocalDate.of(2024, 1, 15));

        // Assert
        assertEquals(3, resultado.size());
        // Deve estar ordenado por horÃ¡rio
        assertEquals(9, resultado.get(0).getDataHora().getHour(), "Primeira deve ser 9h");
        assertEquals(12, resultado.get(1).getDataHora().getHour(), "Segunda deve ser 12h");
        assertEquals(15, resultado.get(2).getDataHora().getHour(), "Terceira deve ser 15h");
    }

    @Test
    void deveFiltrarPorDataExata() {
        var consultaRepo = new InMemoryConsultaRepository();

        Endereco endereco = new Endereco("Rua A", "1", null, "Centro", "SÃ£o LuÃ­s", "MA", "65000-000");
        Paciente paciente = new Paciente(1, "JoÃ£o", "Pai", LocalDate.of(2019, 1, 1), "M",
                endereco, List.of(new Telefone("9800000000", TelefoneTipo.CELULAR, "Pai")), null);
        Medico medico = new Medico(1, "Dr. Vilegas", "Pediatria", "CRM-MA 12345");

        // Adicionar consultas
        Consulta c1 = new Consulta(10, paciente, medico, LocalDateTime.of(2024, 1, 14, 23, 59), false);
        Consulta c2 = new Consulta(11, paciente, medico, LocalDateTime.of(2024, 1, 15, 0, 0), false);
        Consulta c3 = new Consulta(12, paciente, medico, LocalDateTime.of(2024, 1, 15, 23, 59), false);
        Consulta c4 = new Consulta(13, paciente, medico, LocalDateTime.of(2024, 1, 16, 0, 0), false);

        consultaRepo.salvar(c1);
        consultaRepo.salvar(c2);
        consultaRepo.salvar(c3);
        consultaRepo.salvar(c4);

        var service = new ListarConsultasDoDiaService(consultaRepo);

        // Act
        List<Consulta> resultado = service.listar(LocalDate.of(2024, 1, 15));

        // Assert
        assertEquals(2, resultado.size(), "Deve retornar apenas consultas do dia 15");
        assertTrue(resultado.stream().allMatch(c -> c.getDataHora().toLocalDate().equals(LocalDate.of(2024, 1, 15))),
                "Todas as consultas devem ser do dia 15");
    }
}

