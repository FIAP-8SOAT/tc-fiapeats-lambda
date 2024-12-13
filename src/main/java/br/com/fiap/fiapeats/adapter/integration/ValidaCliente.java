package br.com.fiap.fiapeats.adapter.integration;

import br.com.fiap.fiapeats.adapter.response.ValidaClienteResponse;

import java.io.IOException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.URI;

public class ValidaCliente {

    public ValidaClienteResponse consulta(String documento) throws IOException, InterruptedException {

        String url = System.getenv("URL_API");

        HttpClient client = HttpClient.newHttpClient();

        System.out.println("Url API: " + url);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url+documento))
                .GET()
                .version(HttpClient.Version.HTTP_1_1)
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        System.out.println("Status Code: " + response.statusCode());
        System.out.println("Response Body: " + response.body());

        if (response.statusCode() == 200){
            return new ValidaClienteResponse(response.statusCode(), "Cliente cadastrado");
        }

        if (response.statusCode() == 404){
            return new ValidaClienteResponse(response.statusCode(), "Cliente não cadastrado");
        }

        return new ValidaClienteResponse(response.statusCode(), "Erro interno, refaça a consulta");
    }
}
