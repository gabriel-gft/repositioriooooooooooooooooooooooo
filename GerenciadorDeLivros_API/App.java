package GerenciadorDeLivros_API;

import com.sun.net.httpserver.HttpServer;
import java.net.InetSocketAddress;

public class App {
  public static void main(String[] args) throws Exception {
    LivroRepository repository = new LivroRepository();
    LivroService service = new LivroService(repository);
    LivroHandler handler = new LivroHandler(service);

    HttpServer server = HttpServer.create(new InetSocketAddress(8080), 0);
    server.createContext("/api/livros", handler);
    server.setExecutor(null);
    server.start();

    System.out.println("🚀 Servidor 'online' em http://localhost:8080/api/livros");
  }
}