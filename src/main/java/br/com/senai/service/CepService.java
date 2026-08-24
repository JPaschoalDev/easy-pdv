package br.com.senai.service;

import br.com.senai.model.Cliente;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

// CLASSE "CepService" RESPONSÁVEL POR CONSULTAR A API PÚBLICA E PREENCHER OS DADOS DO ENDEREÇO DO CLIENTE
// LOGRADOURO, BAIRRO, CIDADE E UF
public class CepService {

    private static final String URL_BASE = "https://viacep.com.br/ws/%s/json/";

    // "HttpClient" É A CLASSE NATIVA DO JAVA PARA FAZER REQUISIÇÕES HTTP
    private final HttpClient httpClient = HttpClient.newHttpClient();

    // CONSULTA P CEP NA API "ViaCEP" E PREENCHE OS CAMPOS DE ENDEREÇO DIRETAMENTE NO OBJETO "Cliente"
    // LANÇA "CepNaoEncontradoException" CASO O CEP NÃO EXISTA
    // LANÇA "RuntimeException" SE HOUVER ERRO DE CONEXÃO (SEM INTERNET, API FORA DO AR, ETC.)
    public void preencherEndereco(Cliente cliente) throws CepNaoEncontradoException {
        String url = String.format(URL_BASE, cliente.getCep());

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .GET()
                .build();

        HttpResponse<String> response;
        try {
            response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException("Erro ao consultar a API ViaCEP: " + e.getMessage(), e);
        }
        JSONObject json = new JSONObject(response.body());

        // A API "ViaCEP" RESPONDE COM "{"erro": true}" QUANDO O CEP NÃO EXISTE
        // AO INVÉS DE UM CÓDIGO DE ERRO HTTP É CHECADO DE FORMA MANUAL
        if (json.optBoolean("erro", false)) {
            throw new CepNaoEncontradoException(cliente.getCep());
        }

        cliente.setLogradouro(json.optString("logradouro", null));
        cliente.setBairro(json.optString("bairro", null));
        // NO "ViaCEP" O CAMPO DA CIDADE RECEBE O NOME DE "localidade"
        cliente.setCidade(json.optString("localidade", null));
        cliente.setUf(json.optString("uf", null));
    }
}