package br.com.api.eletromap.model.dtos;

import br.com.api.eletromap.model.entities.Conexao;
import br.com.api.eletromap.model.enums.Status;

import java.util.List;

public record UnidadeDto(String endereco, Status status, List<Conexao> conexoes) {
    // DTO para as unidades
}
