package br.ifma.consultasmedicas;

import br.ifma.consultasmedicas.core.domain.model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Testes para ProntuarioBuilder com validaÃ§Ãµes de negÃ³cio.
 */
public class ProntuarioBuilderTest {

        private Consulta consulta;

        @BeforeEach
        void setup() {
                Endereco endereco = new Endereco("Rua A", "1", null, "Centro", "São Luís", "MA", "65000-000");
                Paciente paciente = new Paciente(1, "João", "Pai", LocalDate.of(2019, 1, 1), "M",
                                endereco, List.of(new Telefone("9800000000", TelefoneTipo.CELULAR, "Pai")), null);
                Medico medico = new Medico(1, "Dr. Silva", "Pediatria", "CRM-MA 12345");
                consulta = new Consulta(10, paciente, medico, LocalDateTime.now(), false);
        }

        @Test
        void deveConstroirProntuarioValido() {
                Prontuario prontuario = ProntuarioBuilder.builder()
                                .id(1)
                                .consulta(consulta)
                                .peso(15.2)
                                .altura(0.95)
                                .sintomas("Febre")
                                .observacao("Observar")
                                .build();

                assertNotNull(prontuario);
                assertEquals(1, prontuario.getId());
                assertEquals(15.2, prontuario.getPeso());
                assertEquals(0.95, prontuario.getAltura());
        }

        @Test
        void deveLancarExcecaoQuandoIDNuloNoBuild() {
                assertThrows(IllegalStateException.class, () -> ProntuarioBuilder.builder()
                                .consulta(consulta)
                                .peso(15.2)
                                .altura(0.95)
                                .build());
        }

        @Test
        void deveLancarExcecaoQuandoConsultaNulaNoBuild() {
                assertThrows(IllegalStateException.class, () -> ProntuarioBuilder.builder()
                                .id(1)
                                .peso(15.2)
                                .altura(0.95)
                                .build());
        }

        @Test
        void deveLancarExcecaoParaPesoNegativoOuZero() {
                assertThrows(IllegalArgumentException.class, () -> ProntuarioBuilder.builder()
                                .id(1)
                                .consulta(consulta)
                                .peso(0)
                                .altura(0.95)
                                .build());

                assertThrows(IllegalArgumentException.class, () -> ProntuarioBuilder.builder()
                                .id(1)
                                .consulta(consulta)
                                .peso(-5.0)
                                .altura(0.95)
                                .build());
        }

        @Test
        void deveLancarExcecaoParaAlturaNegativaOuZero() {
                assertThrows(IllegalArgumentException.class, () -> ProntuarioBuilder.builder()
                                .id(1)
                                .consulta(consulta)
                                .peso(15.2)
                                .altura(0)
                                .build());

                assertThrows(IllegalArgumentException.class, () -> ProntuarioBuilder.builder()
                                .id(1)
                                .consulta(consulta)
                                .peso(15.2)
                                .altura(-0.5)
                                .build());
        }

        @Test
        void deveLancarExcecaoParaPesoMuitoAlto() {
                assertThrows(IllegalArgumentException.class,
                                () -> ProntuarioBuilder.builder()
                                                .id(1)
                                                .consulta(consulta)
                                                .peso(350.0)
                                                .altura(1.70)
                                                .build());
        }

        @Test
        void deveLancarExcecaoParaAlturaMuitoAlta() {
                assertThrows(IllegalArgumentException.class,
                                () -> ProntuarioBuilder.builder()
                                                .id(1)
                                                .consulta(consulta)
                                                .peso(70.0)
                                                .altura(3.0)
                                                .build());
        }

        @Test
        void deveLancarExcecaoParaIMCInvalido() {
                assertThrows(IllegalArgumentException.class,
                                () -> ProntuarioBuilder.builder()
                                                .id(1)
                                                .consulta(consulta)
                                                .peso(1.0)
                                                .altura(0.45)
                                                .build());

                assertThrows(IllegalArgumentException.class,
                                () -> ProntuarioBuilder.builder()
                                                .id(1)
                                                .consulta(consulta)
                                                .peso(300.0)
                                                .altura(1.50)
                                                .build());
        }

        @Test
        void deveAdicionarPrescricoes() {
                Medicamento medicamento = new Medicamento(1, "Paracetamol");
                Prescricao prescricao = new Prescricao(100, medicamento, "10mg", "VO", "3 dias");

                Prontuario prontuario = ProntuarioBuilder.builder()
                                .id(1)
                                .consulta(consulta)
                                .peso(15.2)
                                .altura(0.95)
                                .addPrescricao(prescricao)
                                .build();

                assertEquals(1, prontuario.getPrescricoes().size());
                assertEquals("Paracetamol", prontuario.getPrescricoes().get(0).getMedicamento().getNome());
        }

        @Test
        void deveAdicionarExames() {
                Exame exame = new Exame(1, "Hemograma");

                Prontuario prontuario = ProntuarioBuilder.builder()
                                .id(1)
                                .consulta(consulta)
                                .peso(15.2)
                                .altura(0.95)
                                .addExame(exame)
                                .build();

                assertEquals(1, prontuario.getExames().size());
                assertEquals("Hemograma", prontuario.getExames().get(0).getNome());
        }

        @Test
        void deveIgnorarPrescricaoNula() {
                Medicamento medicamento = new Medicamento(1, "Paracetamol");
                Prescricao prescricao = new Prescricao(100, medicamento, "10mg", "VO", "3 dias");

                Prontuario prontuario = ProntuarioBuilder.builder()
                                .id(1)
                                .consulta(consulta)
                                .peso(15.2)
                                .altura(0.95)
                                .addPrescricao(prescricao)
                                .addPrescricao(null)
                                .build();

                assertEquals(1, prontuario.getPrescricoes().size());
        }

        @Test
        void deveRetornarSintomasVazioSeNuloPassado() {
                Prontuario prontuario = ProntuarioBuilder.builder()
                                .id(1)
                                .consulta(consulta)
                                .peso(15.2)
                                .altura(0.95)
                                .sintomas(null)
                                .build();

                assertEquals("", prontuario.getSintomas());
        }

        @Test
        void deveRetornarObservacaoVaziaSeNulaPassada() {
                Prontuario prontuario = ProntuarioBuilder.builder()
                                .id(1)
                                .consulta(consulta)
                                .peso(15.2)
                                .altura(0.95)
                                .observacao(null)
                                .build();

                assertEquals("", prontuario.getObservacaoClinica());
        }
}
