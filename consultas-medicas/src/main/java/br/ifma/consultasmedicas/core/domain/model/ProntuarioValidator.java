package br.ifma.consultasmedicas.core.domain.model;

/**
 * Validador de dados médicos para Prontuário.
 * Encapsula regras de validação de saúde (altura, peso, IMC).
 */
public class ProntuarioValidator {

    private static final double ALTURA_MINIMA = 0.3; // 30 cm (bebê prematuro)
    private static final double ALTURA_MAXIMA = 2.5; // 250 cm (adulto muito alto)
    private static final double PESO_MINIMO = 0.5; // 500g (bebê prematuro extremo)
    private static final double PESO_MAXIMO = 300.0; // 300 kg (limite razoável)

    /**
     * Valida se a altura está dentro de um intervalo plausível.
     * 
     * @param altura altura em metros
     * @throws IllegalArgumentException se altura for inválida
     */
    public static void validarAltura(double altura) {
        if (altura < ALTURA_MINIMA) {
            throw new IllegalArgumentException(
                    String.format("Altura muito baixa: %.2f m (mínimo: %.2f m)", altura, ALTURA_MINIMA));
        }
        if (altura > ALTURA_MAXIMA) {
            throw new IllegalArgumentException(
                    String.format("Altura muito alta: %.2f m (máximo: %.2f m)", altura, ALTURA_MAXIMA));
        }
    }

    /**
     * Valida se o peso está dentro de um intervalo plausível.
     * 
     * @param peso peso em quilogramas
     * @throws IllegalArgumentException se peso for inválido
     */
    public static void validarPeso(double peso) {
        if (peso < PESO_MINIMO) {
            throw new IllegalArgumentException(
                    String.format("Peso muito baixo: %.2f kg (mínimo: %.2f kg)", peso, PESO_MINIMO));
        }
        if (peso > PESO_MAXIMO) {
            throw new IllegalArgumentException(
                    String.format("Peso muito alto: %.2f kg (máximo: %.2f kg)", peso, PESO_MAXIMO));
        }
    }

    /**
     * Calcula e valida o IMC (Índice de Massa Corporal).
     * 
     * @param peso   peso em quilogramas
     * @param altura altura em metros
     * @return o IMC calculado
     * @throws IllegalArgumentException se o IMC estiver fora do intervalo plausível
     *                                  (5 a 100)
     */
    public static double calcularEValidarIMC(double peso, double altura) {
        double imc = peso / (altura * altura);

        if (imc < 5.0) {
            throw new IllegalArgumentException(
                    String.format("IMC muito baixo: %.2f (esperado >= 5)", imc));
        }
        if (imc > 100.0) {
            throw new IllegalArgumentException(
                    String.format("IMC muito alto: %.2f (esperado <= 100)", imc));
        }

        return imc;
    }

    /**
     * Classifica o IMC de acordo com padrões de saúde.
     * 
     * @param imc Índice de Massa Corporal
     * @return classificação do IMC
     */
    public static String classificarIMC(double imc) {
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
        return "Obesidade grau III (mórbida)";
    }
}
