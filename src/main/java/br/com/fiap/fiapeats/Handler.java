package br.com.fiap.fiapeats;

import br.com.fiap.fiapeats.adapter.integration.ValidaCliente;
import br.com.fiap.fiapeats.adapter.request.ValidaClienteRequest;
import br.com.fiap.fiapeats.adapter.response.ValidaClienteResponse;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.UncheckedIOException;

public class Handler implements RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {

    private static final ObjectMapper objectMapper;
    private static final ValidaCliente validaCliente;

    static {
        objectMapper = new ObjectMapper();
        validaCliente = new ValidaCliente();
    }

    @Override
    public APIGatewayProxyResponseEvent handleRequest(APIGatewayProxyRequestEvent request,
                                Context context) {

        try {
            var validaClienteRequest = objectMapper.readValue(request.getBody(), ValidaClienteRequest.class);

            System.out.println("Request: " + validaClienteRequest.documento());

            var mensagem = "Cliente não cadastrado";
            if(validaCliente.consulta(validaClienteRequest.documento())){
                mensagem = "Cliente cadastrado";
            }

            System.out.println("Mensagem: " + mensagem);

            var response = new ValidaClienteResponse(mensagem);

            return new APIGatewayProxyResponseEvent()
                    .withStatusCode(200)
                    .withBody(objectMapper.writeValueAsString(response))
                    .withIsBase64Encoded(false);
        } catch (IOException e){
            throw new UncheckedIOException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
