package br.ifma.consultasmedicas;

import br.ifma.consultasmedicas.adapters.out.repository.*;
import br.ifma.consultasmedicas.core.domain.exception.DomainException;
import br.ifma.consultasmedicas.core.domain.model.*;
import br.ifma.consultasmedicas.core.service.RegistrarProntuarioService;
import br.ifma.consultasmedicas.ports.in.RegistrarProntuarioCommand;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class RegistrarProntuarioServiceTest {

        @Test
        void deveRegistrarProntuarioUmaUnicaVezPorConsulta() {
                var consultaRepo = new InMemoryConsultaRepository();
                var prontuarioRepo = new InMemoryProntuarioRepository();
                var medicamentoRepo = new InMemoryMedicamentoRepository();
                var exameRepo = new InMemoryExameRepository();
                var idGenerator = new InMemoryIdGenerator(100);

                medicamentoRepo.salvar(new Medicamento(1, "Paracetamol"));
                exameRepo.salvar(new Exame(1, "Hemograma"));

                Endereco endereco = new Endereco("Rua A", "1", null, "Centro", "SÃ£o LuÃ­s", "MA", "65000-000");
                Paciente paciente = new Paciente(1, "JoÃ£o", "Pai", LocalDate.of(2019, 1, 1), "M",
                                endereco, List.of(new Telefone("9800000000", TelefoneTipo.CELULAR, "Pai")), null);

                Medico medico = new Medico(1, "Dr. Vilegas", "Pediatria", "CRM-MA 12345");
                Consulta consulta = new Consulta(10, paciente, medico, LocalDateTime.now(), false);
                consultaRepo.salvar(consulta);

                var service = new RegistrarProntuarioService(consultaRepo, prontuarioRepo, medicamentoRepo, exameRepo,
                                idGenerator);

                var cmd = new RegistrarProntuarioCommand(
                                10, 12.0, 0.80, "Sintomas", "Obs",
                                List.of(new RegistrarProntuarioCommand.PrescricaoItemCommand(1, "x", "y", "z")),
                                List.of(1));

                Integer id1 = service.registrar(cmd);
                assertNotNull(id1);
                assertTrue(prontuarioRepo.existeParaConsulta(10));
                assertEquals(ConsultaStatus.REALIZADA, consultaRepo.buscarPorId(10).get().getStatus());

                assertThrows(DomainException.class, () -> service.registrar(cmd));
        }
}

