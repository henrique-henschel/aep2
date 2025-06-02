package br.com.api.eletromap.controller;

import br.com.api.eletromap.model.dtos.UnidadeDto;
import br.com.api.eletromap.model.entities.Unidade;
import br.com.api.eletromap.repository.UnidadeRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class UnidadeController {
    private final UnidadeRepository unidadeRepository;

    public UnidadeController(UnidadeRepository unidadeRepository) {
        this.unidadeRepository = unidadeRepository;
    }

    @GetMapping("/unidades")
    public List<Unidade> findAll() {
        return this.unidadeRepository.findAll();
    }

    @GetMapping("/unidades/{id}")
    public Unidade findById(@PathVariable Long id) {
        return this.unidadeRepository.findById(id).orElse(null);
    }

    @PostMapping("/unidades")
    public Unidade save(@RequestBody UnidadeDto unidadeDto) {
        Unidade novaUnidade = new Unidade(
            unidadeDto.endereco(),
            unidadeDto.conexoes()
        );

        return this.unidadeRepository.save(novaUnidade);
    }

    @PutMapping("/unidades/{id}")
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

    @DeleteMapping("/unidades/{id}")
    public void delete(@PathVariable Long id) {
        this.unidadeRepository.deleteById(id);
    }
}
