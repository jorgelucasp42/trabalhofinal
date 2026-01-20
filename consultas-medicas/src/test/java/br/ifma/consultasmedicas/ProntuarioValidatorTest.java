package br.ifma.consultasmedicas;

import br.ifma.consultasmedicas.core.domain.model.ProntuarioValidator;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Testes para validaÃ§Ãµes de dados mÃ©dicos.
 * Valida regras de altura, peso e IMC.
 */
public class ProntuarioValidatorTest {

    @Test
    void deveValidarAlturaCorreta() {
        assertDoesNotThrow(() -> ProntuarioValidator.validarAltura(0.95));
        assertDoesNotThrow(() -> ProntuarioValidator.validarAltura(1.80));
        assertDoesNotThrow(() -> ProntuarioValidator.validarAltura(0.3));
    }

    @Test
    void deveLancarExcecaoParaAlturaMuitoBaixa() {
        assertThrows(IllegalArgumentException.class,
                () -> ProntuarioValidator.validarAltura(0.25));
    }

    @Test
    void deveLancarExcecaoParaAlturaMuitoAlta() {
        assertThrows(IllegalArgumentException.class,
                () -> ProntuarioValidator.validarAltura(2.6));
    }

    @Test
    void deveValidarPesoCorreto() {
        assertDoesNotThrow(() -> ProntuarioValidator.validarPeso(15.2));
        assertDoesNotThrow(() -> ProntuarioValidator.validarPeso(70.0));
        assertDoesNotThrow(() -> ProntuarioValidator.validarPeso(0.5));
    }

    @Test
    void deveLancarExcecaoParaPesoMuitoBaixo() {
        assertThrows(IllegalArgumentException.class,
                () -> ProntuarioValidator.validarPeso(0.2));
    }

    @Test
    void deveLancarExcecaoParaPesoMuitoAlto() {
        assertThrows(IllegalArgumentException.class,
                () -> ProntuarioValidator.validarPeso(350.0));
    }

    @Test
    void deveCalcularIMCCorretamente() {
        double imc = ProntuarioValidator.calcularEValidarIMC(70.0, 1.70);
        assertTrue(imc > 24.0 && imc < 25.0);
    }

    @Test
    void deveValidarIMCValido() {
        assertDoesNotThrow(() -> ProntuarioValidator.calcularEValidarIMC(15.2, 0.95));
        assertDoesNotThrow(() -> ProntuarioValidator.calcularEValidarIMC(60.0, 1.60));
    }

    @Test
    void deveLancarExcecaoParaIMCMuitoBaixo() {
        assertThrows(IllegalArgumentException.class,
                () -> ProntuarioValidator.calcularEValidarIMC(1.0, 0.45));
    }

    @Test
    void deveLancarExcecaoParaIMCMuitoAlto() {
        assertThrows(IllegalArgumentException.class,
                () -> ProntuarioValidator.calcularEValidarIMC(300.0, 1.50));
    }

    @Test
    void deveClassificarIMCCorretamente() {
        assertEquals("Abaixo do peso", ProntuarioValidator.classificarIMC(17.0));
        assertEquals("Peso normal", ProntuarioValidator.classificarIMC(23.0));
        assertEquals("Sobrepeso", ProntuarioValidator.classificarIMC(27.0));
        assertEquals("Obesidade grau I", ProntuarioValidator.classificarIMC(32.0));
        assertEquals("Obesidade grau II", ProntuarioValidator.classificarIMC(37.0));
        assertEquals("Obesidade grau III (mórbida)", ProntuarioValidator.classificarIMC(45.0));
    }
}

