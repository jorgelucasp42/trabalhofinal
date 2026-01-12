package br.ifma.consultasmedicas.adapters.in.controller;

import br.ifma.consultasmedicas.ports.in.RegistrarProntuarioCommand;
import br.ifma.consultasmedicas.ports.in.RegistrarProntuarioUseCase;

import java.util.Objects;

/**
 * Adaptador de entrada (ex.: Controller REST/CLI).
 * Responsabilidade: orquestrar chamadas ao caso de uso.
 */
public class ProntuarioController {
    private final RegistrarProntuarioUseCase registrarProntuarioUseCase;

    public ProntuarioController(RegistrarProntuarioUseCase registrarProntuarioUseCase) {
        this.registrarProntuarioUseCase = Objects.requireNonNull(registrarProntuarioUseCase);
    }

    public Integer registrarProntuario(RegistrarProntuarioCommand command) {
        return registrarProntuarioUseCase.registrar(command);
    }
}
