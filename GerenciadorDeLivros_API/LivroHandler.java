package GerenciadorDeLivros_API;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class LivroHandler implements HttpHandler {
    private final LivroService service;
    private static final ObjectMapper mapper = new ObjectMapper();

    public LivroHandler(LivroService service) {
        this.service = service;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        exchange.getResponseHeaders().add("Access-Control-Allow-Origin", "*");
        exchange.getResponseHeaders().add("Access-Control-Allow-Methods", "GET, POST, PUT, OPTIONS");
        exchange.getResponseHeaders().add("Access-Control-Allow-Headers", "Content-Type");

        if ("OPTIONS".equals(exchange.getRequestMethod())) {
            exchange.sendResponseHeaders(204, -1);
            return;
        }

        try {
            switch (exchange.getRequestMethod()) {
                case "GET" -> {
                    List<Livro> livros = service.listarLivros();
                    byte[] response = mapper.writeValueAsBytes(livros);
                    enviar(exchange, 200, response);
                }
                case "POST" -> {
                    Livro novo = mapper.readValue(exchange.getRequestBody(), Livro.class);
                    service.adicionarLivro(novo.titulo());
                    enviar(exchange, 201, "{\"msg\": \"Sucesso\"}".getBytes());
                }
                case "PUT" -> {
                    String path = exchange.getRequestURI().getPath();
                    String[] partes = path.split("/");
                    if (partes.length >= 5) {
                        int id = Integer.parseInt(partes[3]);
                        String msg = partes[4].equals("alugar") ? service.alugarLivro(id) : service.devolverLivro(id);
                        enviar(exchange, 200, ("{\"msg\": \"" + msg + "\"}").getBytes());
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("Erro: " + e.getMessage());
            enviar(exchange, 500, "{\"erro\": \"Erro interno\"}".getBytes());
        }
    }

    private void enviar(HttpExchange ex, int code, byte[] res) throws IOException {
        ex.getResponseHeaders().set("Content-Type", "application/json");
        ex.sendResponseHeaders(code, res.length);
        try (OutputStream os = ex.getResponseBody()) { os.write(res); }
    }
}