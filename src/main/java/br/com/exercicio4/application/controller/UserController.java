package br.com.exercicio4.application.controller;

import br.com.exercicio4.domain.model.User;
import br.com.exercicio4.domain.model.dto.UserRequest;
import br.com.exercicio4.domain.model.dto.UserResponse;
import br.com.exercicio4.domain.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService service;

    public UserController(UserService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<UserResponse> create(@RequestBody @Valid UserRequest request) {
        User user = new User(null, request.name(), request.email());
        User created = service.create(user);
        return ResponseEntity.ok(new UserResponse(created.getId(), created.getName(), created.getEmail()));
    }

    @GetMapping
    public List<UserResponse> listAll() {
        return service.listAll().stream()
                .map(user -> new UserResponse(user.getId(), user.getName(), user.getEmail()))
                .toList();
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> findById(@PathVariable Long id) {
        User user = service.findById(id);
        return ResponseEntity.ok(new UserResponse(user.getId(), user.getName(), user.getEmail()));
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserResponse> update(@PathVariable Long id, @RequestBody @Valid UserRequest request) {
        User updated = service.update(id, request.name(), request.email());
        return ResponseEntity.ok(new UserResponse(updated.getId(), updated.getName(), updated.getEmail()));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
