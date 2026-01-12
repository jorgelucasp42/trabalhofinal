package br.ifma.consultasmedicas.ports.out;

import br.ifma.consultasmedicas.core.domain.model.Exame;

import java.util.List;
import java.util.Optional;

public interface ExameRepository {
    Optional<Exame> buscarPorId(Integer id);
    List<Exame> listarTodos();
    void salvar(Exame exame);
}
