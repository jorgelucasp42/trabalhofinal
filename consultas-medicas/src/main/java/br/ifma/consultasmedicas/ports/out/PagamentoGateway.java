package br.ifma.consultasmedicas.ports.out;

/**
 * Porta de saída para gateways de pagamento.
 * 
 * Responsabilidade: Abstrair a integração com diferentes plataformas
 * de pagamento (Stripe, PayPal, Mercado Pago, etc).
 * 
 * Strategy Pattern: Diferentes implementadores desta interface
 * permitem trocar de gateway sem alterar o domínio.
 */
public interface PagamentoGateway {
    /**
     * Processa uma transação de pagamento.
     * 
     * @param request Dados da transação
     * @return Resposta do processamento
     */
    TransacaoResponse processar(ProcessarTransacaoRequest request);

    /**
     * Consulta o status de uma transação.
     * 
     * @param idTransacao ID retornado por processar()
     * @return Status atual da transação
     */
    StatusTransacao consultar(String idTransacao);

    /**
     * Retorna o nome do gateway (Stripe, PayPal, etc).
     */
    String getNomeGateway();
}
