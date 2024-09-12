package com.guilherme.stock.dto.requests;

import com.guilherme.stock.validations.GreaterThanZero;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateStockItemDTO {

    @NotBlank(message = "O nome é obrigatório")
    private String name;

    @NotNull(message = "A quantidade é obrigatória")
    @Min(value = 1, message = "A quantidade deve ser um numero inteiro maior que zero")
    private Integer quantity;

    private String description;

    @NotNull(message = "O preço é obrigatório")
    @GreaterThanZero(message = "O preço deve ser maior que zero")
    private Double price;
}
