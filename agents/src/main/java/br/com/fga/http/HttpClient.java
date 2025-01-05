package br.com.fga.http;

import br.com.fga.models.Simulation;
import br.com.fga.models.Truck;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.OutputStream;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.util.List;

public class HttpClient {

    private static final ObjectMapper mapper = new ObjectMapper();

//    public static void main(String[] args) {
//        post("http://localhost:8080/vehicles", new Truck(10));
//    }

    public static void get(String path) {
        try {
            // URL para onde será enviada a requisição
            URI uri = new URI(path);
            URL url = uri.toURL();
            HttpURLConnection conn = getHttpURLConnection(url, "GET");

            // Verificando a resposta
            int responseCode = conn.getResponseCode();
            System.out.println("Código de resposta: " + responseCode);

            assert responseCode == 200;

            conn.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static int post(String path, Object data) {
        int responseCode = -1;

        try {
            URI uri = new URI(path);
            URL url = uri.toURL();
            HttpURLConnection conn = getHttpURLConnection(url, "POST");
            conn.connect();

            String jsonInput = mapper.writeValueAsString(data);

            try (OutputStream os = conn.getOutputStream()) {
                byte[] input = jsonInput.getBytes("utf-8");
                os.write(input, 0, input.length);
            }

            responseCode = conn.getResponseCode();

            conn.disconnect();
        } catch (ConnectException e) {
            //System.out.println(e.getMessage());
            //System.out.println("Tentando novamente...");
        } catch (Exception e) {
            e.printStackTrace();
        }

        return responseCode;
    }

    private static HttpURLConnection getHttpURLConnection(URL url, String method) throws IOException {
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();

        conn.setRequestMethod(method);
        conn.setRequestProperty("Content-Type", "application/json");
        conn.setDoOutput(true);

        return conn;
    }

}
