package br.com.exercicio4.domain.service;

import br.com.exercicio4.domain.model.User;
import br.com.exercicio4.infra.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    private final UserRepository repository;

    public UserService(UserRepository repository) {
        this.repository = repository;
    }

    public User create(User user) {
        return repository.save(user);
    }

    public List<User> listAll() {
        return repository.findAll();
    }

    public User findById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Usuário não encontrado"));
    }

    public User update(Long id, String name, String email) {
        User user = findById(id);
        user.update(name, email);
        return repository.save(user);
    }

    public void delete(Long id) {
        repository.deleteById(id);
    }
}
