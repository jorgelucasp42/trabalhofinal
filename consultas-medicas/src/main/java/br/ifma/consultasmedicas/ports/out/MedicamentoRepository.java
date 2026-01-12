package br.ifma.consultasmedicas.ports.out;

import br.ifma.consultasmedicas.core.domain.model.Medicamento;

import java.util.List;
import java.util.Optional;

public interface MedicamentoRepository {
    Optional<Medicamento> buscarPorId(Integer id);
    List<Medicamento> listarTodos();
    void salvar(Medicamento medicamento);
}
