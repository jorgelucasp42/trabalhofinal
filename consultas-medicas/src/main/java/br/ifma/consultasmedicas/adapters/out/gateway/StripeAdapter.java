package br.ifma.consultasmedicas.adapters.out.gateway;

import br.ifma.consultasmedicas.core.service.SimpleLogger;
import br.ifma.consultasmedicas.ports.out.PagamentoGateway;
import br.ifma.consultasmedicas.ports.out.ProcessarTransacaoRequest;
import br.ifma.consultasmedicas.ports.out.StatusTransacao;
import br.ifma.consultasmedicas.ports.out.TransacaoResponse;

/**
 * Adapter para Stripe (implementação da interface PagamentoGateway).
 * 
 * Responsabilidade: Integrar com API do Stripe para processar pagamentos.
 * 
 * Nota: Esta é uma implementação mock. Em produção, faria chamadas HTTP reais
 * para a API do Stripe.
 */
public class StripeAdapter implements PagamentoGateway {

    private final String stripeSecretKey;
    private final SimpleLogger logger = new SimpleLogger(StripeAdapter.class);

    public StripeAdapter(String stripeSecretKey) {
        this.stripeSecretKey = stripeSecretKey;
    }

    @Override
    public TransacaoResponse processar(ProcessarTransacaoRequest request) {
        logger.info("Processando pagamento via Stripe");

        try {
            // Em produção: Chamar API Stripe
            // POST https://api.stripe.com/v1/payment_intents
            // Com dados: { "amount": valor, "currency": "BRL", "source": token, ... }

            // Mock: Simular resposta bem-sucedida (80% das vezes)
            boolean simulouSucesso = Math.random() > 0.2;

            if (simulouSucesso) {
                String idTransacao = "stripe-pi-" + System.currentTimeMillis();
                logger.info("Transação Stripe bem-sucedida. ID: " + idTransacao);

                return new TransacaoResponse(
                        idTransacao,
                        true,
                        "Pagamento autorizado e capturado",
                        StatusTransacao.CAPTURADA);
            } else {
                logger.info("Transação Stripe negada");

                return new TransacaoResponse(
                        null,
                        false,
                        "Cartão negado pela operadora",
                        StatusTransacao.NEGADA);
            }

        } catch (Exception e) {
            logger.error("Erro ao processar pagamento Stripe: " + e.getMessage());

            return new TransacaoResponse(
                    null,
                    false,
                    "Erro ao processar pagamento: " + e.getMessage(),
                    StatusTransacao.ERRO);
        }
    }

    @Override
    public StatusTransacao consultar(String idTransacao) {
        logger.info("Consultando status da transação Stripe: " + idTransacao);

        try {
            // Em produção: Chamar API Stripe
            // GET https://api.stripe.com/v1/payment_intents/{idTransacao}

            return StatusTransacao.CAPTURADA; // Mock

        } catch (Exception e) {
            logger.error("Erro ao consultar transação: " + e.getMessage());
            return StatusTransacao.ERRO;
        }
    }

    @Override
    public String getNomeGateway() {
        return "Stripe";
    }
}
