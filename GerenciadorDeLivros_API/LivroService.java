package GerenciadorDeLivros_API;
import java.util.List;
public class LivroService {
    private final LivroRepository repository;

    public LivroService(LivroRepository repository) {
        this.repository = repository;
    }

    public String adicionarLivro(String titulo) {
        if (titulo == null || titulo.trim().isEmpty()) {
            return "Erro: O título não pode ser vazio.";
        }
        repository.AdicionarLivro(titulo);
        return "Livro cadastrado com sucesso!";
    }

    public String alugarLivro(int id) {
        if (repository.marcarComoAlugado(id)) {
            return "Livro alugado com sucesso!";
        }
        return "Erro: Livro não encontrado ou já está alugado.";
    }

    public String devolverLivro(int id) {
        if (repository.marcarComoDisponivel(id)) {
            return "Livro devolvido com sucesso!";
        }
        return "Erro: Livro não encontrado ou já está na biblioteca.";
    }

    public List<Livro> listarLivros() {
        return repository.listarLivros();
    }
}
