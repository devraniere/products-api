package com.products.productsapi.dtos;

import java.math.BigDecimal;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

// data transfer object criado para tranferir os atributos dos produtos não
public record ProductRecordDto(@NotBlank String name, @NotNull BigDecimal value) {

}
