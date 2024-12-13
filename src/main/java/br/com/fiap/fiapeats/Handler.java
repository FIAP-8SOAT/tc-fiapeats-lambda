package br.com.fiap.fiapeats;

import br.com.fiap.fiapeats.adapter.integration.ValidaCliente;
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
            var documento = request.getPathParameters().get("documento");
            System.out.println("Resquest - documento: " + documento);

            var response = validaCliente.consulta(documento);

            return new APIGatewayProxyResponseEvent()
                    .withStatusCode(response.codigo())
                    .withBody(objectMapper.writeValueAsString(response.mensagem()))
                    .withIsBase64Encoded(false);
        } catch (IOException e){
            throw new UncheckedIOException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
