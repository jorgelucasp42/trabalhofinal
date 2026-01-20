package br.ifma.consultasmedicas.adapters.out.gateway;

import br.ifma.consultasmedicas.ports.out.*;

import java.util.UUID;

public class FakePagamentoGateway implements PagamentoGateway {

    @Override
    public TransacaoResponse processar(ProcessarTransacaoRequest request) {

        // Regra fake:
        // cartão terminando com 0 → falha
        boolean sucesso = !request.getNumeroCartao().endsWith("0");

        if (sucesso) {
            return new TransacaoResponse(
                    UUID.randomUUID().toString(),   // idTransacao
                    true,                           // sucesso
                    "Pagamento aprovado (FAKE)",   // mensagem
                    StatusTransacao.CAPTURADA       // status
            );
        }

        return new TransacaoResponse(
                UUID.randomUUID().toString(),       // idTransacao
                false,                              // sucesso
                "Cartão recusado (FAKE)",           // mensagem
                StatusTransacao.CANCELADA           // status
        );
    }

    @Override
    public StatusTransacao consultar(String idTransacao) {
        // Fake simples: sempre retorna capturada
        return StatusTransacao.CAPTURADA;
    }

    @Override
    public String getNomeGateway() {
        return "FakeGateway";
    }
}