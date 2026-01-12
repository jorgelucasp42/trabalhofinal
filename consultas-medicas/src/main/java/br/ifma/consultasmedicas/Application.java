package br.ifma.consultasmedicas;

import br.ifma.consultasmedicas.adapters.in.controller.ProntuarioController;
import br.ifma.consultasmedicas.adapters.out.repository.*;
import br.ifma.consultasmedicas.core.domain.model.*;
import br.ifma.consultasmedicas.core.service.ListarConsultasDoDiaService;
import br.ifma.consultasmedicas.core.service.RegistrarProntuarioService;
import br.ifma.consultasmedicas.ports.in.RegistrarProntuarioCommand;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public class Application {

    public static void main(String[] args) {
        // Adapters OUT (infra)
        var pacienteRepo = new InMemoryPacienteRepository();
        var consultaRepo = new InMemoryConsultaRepository();
        var prontuarioRepo = new InMemoryProntuarioRepository();
        var medicamentoRepo = new InMemoryMedicamentoRepository();
        var exameRepo = new InMemoryExameRepository();

        // Seed (cadastros prévios de medicamentos e exames - pré-condição do enunciado)
        medicamentoRepo.salvar(new Medicamento(1, "Paracetamol"));
        medicamentoRepo.salvar(new Medicamento(2, "Ibuprofeno"));
        exameRepo.salvar(new Exame(1, "Hemograma completo"));
        exameRepo.salvar(new Exame(2, "Raio-X"));

        // Domínio: paciente e consulta agendada (secretária agenda)
        Endereco endereco = new Endereco("Rua A", "123", "Apto 10", "Centro", "São Luís", "MA", "65000-000");
        Paciente paciente = new Paciente(1, "Ana", "Maria (mãe)", LocalDate.of(2020, 5, 3), "F", endereco,
            List.of(new Telefone("98999990000", TelefoneTipo.CELULAR, "Maria")), null);
        pacienteRepo.salvar(paciente);

        Medico medico = new Medico(1, "Dr. Vilegas", "CRM-MA 12345");

        Consulta consulta = new Consulta(1, paciente, medico, LocalDateTime.now().withHour(9).withMinute(0), true);
        consultaRepo.salvar(consulta);

        // Core services (casos de uso)
        var registrarProntuarioUC = new RegistrarProntuarioService(consultaRepo, prontuarioRepo, medicamentoRepo, exameRepo);
        var listarConsultasUC = new ListarConsultasDoDiaService(consultaRepo);

        // Adapter IN (controller)
        var prontuarioController = new ProntuarioController(registrarProntuarioUC);

        // Fluxo do caso de uso: listar consultas do dia
        System.out.println("Consultas do dia:");
        listarConsultasUC.listar(LocalDate.now()).forEach(c ->
            System.out.printf("- %s | %s | novo=%s%n", c.getDataHora().toLocalTime(), c.getPaciente().getNomeCrianca(), c.isPacienteNovo())
        );

        // Registrar prontuário
        var cmd = new RegistrarProntuarioCommand(
            1,
            15.2,
            0.95,
            "Febre e tosse",
            "Hidratar e observar sinais de alarme",
            List.of(new RegistrarProntuarioCommand.PrescricaoItemCommand(1, "10mg/kg", "VO a cada 6h", "3 dias")),
            List.of(1)
        );

        Integer prontuarioId = prontuarioController.registrarProntuario(cmd);
        System.out.println("Prontuário registrado com ID: " + prontuarioId);
        System.out.println("Status da consulta após registro: " + consultaRepo.buscarPorId(1).get().getStatus());
    }
}
