package br.com.exercicio4.domain.model;

import jakarta.persistence.*;
import jakarta.validation.ValidationException;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(unique = true, nullable = false)
    private String email;

    protected User() {}

    public User(Long id, String name, String email) {
        this.id = id;
        this.name = name;
        this.email = email;
    }

    public void update(String name, String email) {
        validate(name, email);
        this.name = name;
        this.email = email;
    }

    private void validate(String name, String email) {
        if(name == null || name.isBlank()) {
            throw new ValidationException("Nome é obrigatório");
        }
        if (email == null || email.isBlank() || !email.contains("@")) {
            throw new ValidationException("Email inválido");
        }
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }
}
