package br.com.exercicio4.domain.model.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record UserRequest(
        @NotBlank(message = "O nome é obrigatório") String name,
        @Email(message = "Email inválido") String email
) {}
