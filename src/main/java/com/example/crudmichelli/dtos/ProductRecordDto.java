package com.example.crudmichelli.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record ProductRecordDto(@NotBlank String nome, @NotNull BigDecimal valor) {  //para definicao dos campos do arqumento, sao os campos da classe entity
    //jah pode colocar algumas validacoes de nulo
}
