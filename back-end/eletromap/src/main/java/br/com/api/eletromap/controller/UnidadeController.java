package br.com.api.eletromap.controller;

import br.com.api.eletromap.repository.UnidadeRepository;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UnidadeController {
    private UnidadeRepository unidadeRepository;

    public UnidadeController(UnidadeRepository unidadeRepository) {
        this.unidadeRepository = unidadeRepository;
    }

    // Agora falta implementar as rotas, criar o front e integrar com ele...
}
