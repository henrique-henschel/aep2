package br.com.api.eletromap.controller;

import br.com.api.eletromap.model.entities.Conexao;
import br.com.api.eletromap.model.entities.Unidade;
import br.com.api.eletromap.repository.ConexaoRepository;
import br.com.api.eletromap.repository.UnidadeRepository; // Precisaremos do UnidadeRepository
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/conexoes")
@CrossOrigin(origins = "*") // Libera CORS para o front-end, se necessário
public class ConexaoController {

    private final ConexaoRepository conexaoRepository;
    private final UnidadeRepository unidadeRepository; // Injetar UnidadeRepository

    // Injeção de dependência via construtor
    public ConexaoController(ConexaoRepository conexaoRepository, UnidadeRepository unidadeRepository) {
        this.conexaoRepository = conexaoRepository;
        this.unidadeRepository = unidadeRepository;
    }

    // Criar nova conexão
    // Recebe os IDs da origem e destino para criar a conexão
    @PostMapping
    public ResponseEntity<Conexao> criarConexao(@RequestParam Long origemId,
                                                @RequestParam Long destinoId) {
        Optional<Unidade> optionalOrigem = unidadeRepository.findById(origemId);
        Optional<Unidade> optionalDestino = unidadeRepository.findById(destinoId);

        if (optionalOrigem.isEmpty() || optionalDestino.isEmpty()) {
            // Se uma das unidades não for encontrada, retorna erro
            return ResponseEntity.badRequest().build();
        }

        Unidade origem = optionalOrigem.get();
        Unidade destino = optionalDestino.get();

        // Cria a nova conexão
        Conexao novaConexao = new Conexao(origem, destino);

        // Salva a conexão no banco de dados
        Conexao salva = conexaoRepository.save(novaConexao);

        // Opcional: Atualizar as listas de conexões nas unidades (se necessário carregá-las imediatamente)
        // Isso é normalmente tratado automaticamente pela JPA com mappedBy se os relacionamentos estão bem definidos
        // origem.getConexoesDeOrigem().add(salva);
        // destino.getConexoesDeDestino().add(salva);
        // unidadeRepository.save(origem);
        // unidadeRepository.save(destino);

        return ResponseEntity.ok(salva);
    }

    // Listar todas as conexões
    @GetMapping
    public ResponseEntity<List<Conexao>> listarConexoes() {
        List<Conexao> conexoes = conexaoRepository.findAll();
        return ResponseEntity.ok(conexoes);
    }

    // Buscar por ID
    @GetMapping("/{id}")
    public ResponseEntity<Conexao> buscarPorId(@PathVariable Long id) {
        Optional<Conexao> conexao = conexaoRepository.findById(id);
        return conexao.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Deletar conexão
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarConexao(@PathVariable Long id) {
        if (conexaoRepository.existsById(id)) {
            conexaoRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}