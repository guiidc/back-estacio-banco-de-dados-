package com.guilherme.stock.dto.requests;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class UpdateStockItemQuantityDTO {
    @NotNull(message = "A quantidade é obrigatória")
    private Integer quantity;
}
