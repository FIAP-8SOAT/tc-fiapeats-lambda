package br.com.fiap.fiapeats.adapter.integration;

import java.io.IOException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.URI;

public class ValidaCliente {

    public boolean consulta(String documento) throws IOException, InterruptedException {

        HttpClient client = HttpClient.newHttpClient();

        String url = "https://3651-191-227-243-148.ngrok-free.app/fiapeats/cliente/"+documento;

        System.out.println("Url API: " + url);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .GET()
                .version(HttpClient.Version.HTTP_1_1)
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        System.out.println("Status Code: " + response.statusCode());
        System.out.println("Response Body: " + response.body());

        return response.statusCode() == 200;
    }
}