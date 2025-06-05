package br.com.exercicio4.application.controller;

import br.com.exercicio4.domain.model.User;
import br.com.exercicio4.domain.model.dto.ResponseDTO;
import br.com.exercicio4.domain.model.dto.UserCreateDTO;
import br.com.exercicio4.domain.model.dto.UserUpdateDTO;
import br.com.exercicio4.domain.service.UserService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService service;

    public UserController(UserService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<User> create(@RequestBody @Valid UserCreateDTO dto) {
        User user = new User(dto.name(), dto.email());
        User created = service.create(user);
        return ResponseEntity.created(URI.create("/users/" + created.getId())).body(created);
    }

    @GetMapping
    public ResponseEntity<ResponseDTO<User>> list(Pageable pageable) {
        var users = service.listAll(pageable);
        return ResponseEntity.ok(ResponseDTO.fromPage(users));
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> get(@PathVariable Long id) {
        return ResponseEntity.ok(service.findById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<User> update(@PathVariable Long id, @RequestBody @Valid UserUpdateDTO request) {
        User updated = service.update(id, request.name(), request.email());
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
