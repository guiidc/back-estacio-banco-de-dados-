package com.guilherme.stock.dto.requests;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserLoginDTO {
    @NotBlank(message = "O campo email é obrigatório")
    private String email;

    @Length(min = 6, message = "O campo senha deve ter no mínimo 6 caracteres")
    @NotNull(message = "O campo senha é obrigatório")
    private String password;
}
