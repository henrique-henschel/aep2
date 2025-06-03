package br.com.api.eletromap.controller;

import br.com.api.eletromap.model.dtos.UnidadeDto;
import br.com.api.eletromap.model.entities.Unidade;
import br.com.api.eletromap.repository.UnidadeRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/unidades")
public class UnidadeController {
    private final UnidadeRepository unidadeRepository;

    public UnidadeController(UnidadeRepository unidadeRepository) {
        this.unidadeRepository = unidadeRepository;
    }

    @GetMapping
    public List<Unidade> findAll() {
        return this.unidadeRepository.findAll();
    }

    @GetMapping("/{id}")
    public Unidade findById(@PathVariable Long id) {
        return this.unidadeRepository.findById(id).orElse(null);
    }

    @PostMapping
    public Unidade save(@RequestBody UnidadeDto unidadeDto) {
        Unidade novaUnidade = new Unidade(
            unidadeDto.endereco(),
            unidadeDto.conexoes()
        );

        return this.unidadeRepository.save(novaUnidade);
    }

    @PutMapping("/{id}")
    public Unidade update(@PathVariable Long id, @RequestBody UnidadeDto unidadeDto) {
        Unidade unidade = this.unidadeRepository.findById(id).orElse(null);
        if (unidade != null) {
            unidade.setEndereco(unidadeDto.endereco());
            unidade.setStatus(unidadeDto.status());
            unidade.setConexoes(unidadeDto.conexoes());

            return this.unidadeRepository.save(unidade);
        }

        return null;
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        this.unidadeRepository.deleteById(id);
    }
}
