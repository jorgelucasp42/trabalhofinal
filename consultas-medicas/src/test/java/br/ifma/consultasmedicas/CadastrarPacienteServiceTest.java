package br.ifma.consultasmedicas;

import br.ifma.consultasmedicas.adapters.out.repository.*;
import br.ifma.consultasmedicas.core.domain.exception.DomainException;
import br.ifma.consultasmedicas.core.domain.model.*;
import br.ifma.consultasmedicas.core.service.CadastrarPacienteService;
import br.ifma.consultasmedicas.ports.in.CadastrarPacienteCommand;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class CadastrarPacienteServiceTest {

    private CadastrarPacienteService service;
    private InMemoryPacienteRepository pacienteRepo;
    private InMemoryPlanoSaudeRepository planoRepo;
    private InMemoryIdGenerator idGenerator;

    @BeforeEach
    void setup() {
        pacienteRepo = new InMemoryPacienteRepository();
        planoRepo = new InMemoryPlanoSaudeRepository();
        idGenerator = new InMemoryIdGenerator(1000);
        service = new CadastrarPacienteService(pacienteRepo, planoRepo, idGenerator);

        // Registra plano de saúde para testes
        planoRepo.salvar(new PlanoSaude(1, "Unimed"));
        planoRepo.salvar(new PlanoSaude(2, "Amil"));
    }

    @Test
    void deveRegistrarPacienteComSucesso() {
        // given
        var enderecoCmd = new CadastrarPacienteCommand.EnderecoCommand(
                "Rua Principal", "123", "Apt 456", "Centro", "São Luís", "MA", "65000-000");
        var telefoneCmd = new CadastrarPacienteCommand.TelefoneCommand(
                "98987654321", TelefoneTipo.CELULAR, "Pessoal");
        var cmd = new CadastrarPacienteCommand(
                "Maria Silva", "Mãe", LocalDate.of(1990, 5, 15),
                "F", enderecoCmd, List.of(telefoneCmd), 1);

        // when
        Integer pacienteId = service.cadastrar(cmd);

        // then
        assertNotNull(pacienteId);
        assertTrue(pacienteRepo.existePorId(pacienteId));
        Paciente paciente = pacienteRepo.buscarPorId(pacienteId).get();
        assertEquals("Maria Silva", paciente.getNomeCrianca());
        assertEquals("F", paciente.getSexo());
        assertEquals(LocalDate.of(1990, 5, 15), paciente.getDataNascimento());
    }

    @Test
    void deveValidarNomePaciente() {
        // given - nome vazio
        var enderecoCmd = new CadastrarPacienteCommand.EnderecoCommand(
                "Rua", "123", null, "Centro", "São Luís", "MA", "65000-000");
        var cmd = new CadastrarPacienteCommand(
                "", "Mãe", LocalDate.of(1990, 5, 15),
                "F", enderecoCmd, List.of(), 1);

        // when/then
        assertThrows(IllegalArgumentException.class, () -> service.cadastrar(cmd));
    }

    @Test
    void deveValidarDataNascimento() {
        // given - data nascimento no futuro
        var enderecoCmd = new CadastrarPacienteCommand.EnderecoCommand(
                "Rua", "123", null, "Centro", "São Luís", "MA", "65000-000");
        var cmd = new CadastrarPacienteCommand(
                "João Silva", "Pai", LocalDate.now().plusYears(1),
                "M", enderecoCmd, List.of(), 1);

        // when/then
        assertThrows(IllegalArgumentException.class, () -> service.cadastrar(cmd));
    }

    @Test
    void deveValidarSexo() {
        // given - sexo inválido
        var enderecoCmd = new CadastrarPacienteCommand.EnderecoCommand(
                "Rua", "123", null, "Centro", "São Luís", "MA", "65000-000");
        var cmd = new CadastrarPacienteCommand(
                "João Silva", "Pai", LocalDate.of(1990, 5, 15),
                "X", enderecoCmd, List.of(), 1);

        // when/then
        assertThrows(IllegalArgumentException.class, () -> service.cadastrar(cmd));
    }

    @Test
    void deveValidarPlanoSaudeExistente() {
        // given - plano de saúde inexistente
        var enderecoCmd = new CadastrarPacienteCommand.EnderecoCommand(
                "Rua", "123", null, "Centro", "São Luís", "MA", "65000-000");
        var cmd = new CadastrarPacienteCommand(
                "João Silva", "Pai", LocalDate.of(1990, 5, 15),
                "M", enderecoCmd, List.of(), 999 // ID inválido
        );

        // when/then
        assertThrows(DomainException.class, () -> service.cadastrar(cmd));
    }

    @Test
    void deveRegistrarPacienteComMultiplosTelefones() {
        // given
        var enderecoCmd = new CadastrarPacienteCommand.EnderecoCommand(
                "Rua", "123", null, "Centro", "São Luís", "MA", "65000-000");
        var telefoneCmd1 = new CadastrarPacienteCommand.TelefoneCommand(
                "98987654321", TelefoneTipo.CELULAR, "Pessoal");
        var telefoneCmd2 = new CadastrarPacienteCommand.TelefoneCommand(
                "3221234567", TelefoneTipo.CELULAR, "Trabalho");
        var cmd = new CadastrarPacienteCommand(
                "João Silva", "Pai", LocalDate.of(1990, 5, 15),
                "M", enderecoCmd, List.of(telefoneCmd1, telefoneCmd2), 1);

        // when
        Integer pacienteId = service.cadastrar(cmd);

        // then
        Paciente paciente = pacienteRepo.buscarPorId(pacienteId).get();
        assertEquals(2, paciente.getTelefones().size());
    }

    @Test
    void deveRegistrarPacienteSemTelefone() {
        // given
        var enderecoCmd = new CadastrarPacienteCommand.EnderecoCommand(
                "Rua", "123", null, "Centro", "São Luís", "MA", "65000-000");
        var cmd = new CadastrarPacienteCommand(
                "João Silva", "Pai", LocalDate.of(1990, 5, 15),
                "M", enderecoCmd, List.of(), 1);

        // when
        Integer pacienteId = service.cadastrar(cmd);

        // then
        Paciente paciente = pacienteRepo.buscarPorId(pacienteId).get();
        assertNotNull(paciente.getTelefones());
        assertEquals(0, paciente.getTelefones().size());
    }

    @Test
    void deveAssociarPlanoSaude() {
        // given
        var enderecoCmd = new CadastrarPacienteCommand.EnderecoCommand(
                "Rua", "123", null, "Centro", "São Luís", "MA", "65000-000");
        var cmd = new CadastrarPacienteCommand(
                "João Silva", "Pai", LocalDate.of(1990, 5, 15),
                "M", enderecoCmd, List.of(), 2 // Amil
        );

        // when
        Integer pacienteId = service.cadastrar(cmd);

        // then
        Paciente paciente = pacienteRepo.buscarPorId(pacienteId).get();
        assertNotNull(paciente.getPlanoSaude());
        assertEquals("Amil", paciente.getPlanoSaude().getNomePlano());
    }

    @Test
    void deveRetornarIdGeradoSequencialmente() {
        // given - primeira registro
        var enderecoCmd = new CadastrarPacienteCommand.EnderecoCommand(
                "Rua", "123", null, "Centro", "São Luís", "MA", "65000-000");
        var cmd1 = new CadastrarPacienteCommand(
                "João Silva", "Pai", LocalDate.of(1990, 5, 15),
                "M", enderecoCmd, List.of(), 1);
        var cmd2 = new CadastrarPacienteCommand(
                "Maria Silva", "Mãe", LocalDate.of(1992, 3, 10),
                "F", enderecoCmd, List.of(), 1);

        // when
        Integer id1 = service.cadastrar(cmd1);
        Integer id2 = service.cadastrar(cmd2);

        // then
        assertEquals(1000, id1);
        assertEquals(1001, id2);
        assertTrue(id2 > id1);
    }
}
