package br.com.fga.http;

import br.com.fga.models.Truck;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;

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

    public static void post(String path, Object data) {
        try {
            URI uri = new URI(path);
            URL url = uri.toURL();
            HttpURLConnection conn = getHttpURLConnection(url, "POST");

            String jsonInput = mapper.writeValueAsString(data);

            try (OutputStream os = conn.getOutputStream()) {
                byte[] input = jsonInput.getBytes("utf-8");
                os.write(input, 0, input.length);
            }

            // Verificando a resposta
            int responseCode = conn.getResponseCode();
            System.out.println("Código de resposta: " + responseCode);

            assert responseCode == 200;

            conn.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static HttpURLConnection getHttpURLConnection(URL url, String method) throws IOException {
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();

        conn.setRequestMethod(method);
        conn.setRequestProperty("Content-Type", "application/json");
        conn.setDoOutput(true);

        return conn;
    }

}
