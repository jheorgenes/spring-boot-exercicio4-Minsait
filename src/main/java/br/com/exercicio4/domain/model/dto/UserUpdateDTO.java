package br.com.exercicio4.domain.model.dto;

import jakarta.validation.constraints.Email;

public record UserUpdateDTO(
        String name,
        @Email(message = "Formato de e-mail inválido") String email
) {}
