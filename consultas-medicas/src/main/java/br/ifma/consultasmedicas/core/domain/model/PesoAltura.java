package br.ifma.consultasmedicas.core.domain.model;

import java.time.LocalDate;
import java.util.Objects;

/**
 * Value Object que encapsula uma leitura de peso e altura com data.
 * Permite manter histórico completo de peso/altura para um paciente.
 * 
 * Invariantes:
 * - Peso entre 0.5kg e 300kg
 * - Altura entre 0.3m e 2.5m
 * - Data é obrigatória
 */
public class PesoAltura {
    private final double peso;
    private final double altura;
    private final LocalDate data;

    public PesoAltura(double peso, double altura, LocalDate data) {
        this.data = Objects.requireNonNull(data, "Data não pode ser nula");

        // Validar peso
        ProntuarioValidator.validarPeso(peso);
        this.peso = peso;

        // Validar altura
        ProntuarioValidator.validarAltura(altura);
        this.altura = altura;
    }

    public double getPeso() {
        return peso;
    }

    public double getAltura() {
        return altura;
    }

    public LocalDate getData() {
        return data;
    }

    /**
     * Calcula o IMC baseado em peso e altura.
     */
    public double calcularImc() {
        return peso / (altura * altura);
    }

    /**
     * Classifica o IMC em categorias.
     */
    public String classificarImc() {
        double imc = calcularImc();
        if (imc < 18.5)
            return "Abaixo do peso";
        if (imc < 25.0)
            return "Peso normal";
        if (imc < 30.0)
            return "Sobrepeso";
        if (imc < 35.0)
            return "Obesidade grau I";
        if (imc < 40.0)
            return "Obesidade grau II";
        return "Obesidade grau III";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        PesoAltura that = (PesoAltura) o;
        return Double.compare(that.peso, peso) == 0 &&
                Double.compare(that.altura, altura) == 0 &&
                Objects.equals(data, that.data);
    }

    @Override
    public int hashCode() {
        return Objects.hash(peso, altura, data);
    }

    @Override
    public String toString() {
        return String.format("PesoAltura{%.2fkg, %.2fm, %s, IMC: %.2f}",
                peso, altura, data, calcularImc());
    }
}
