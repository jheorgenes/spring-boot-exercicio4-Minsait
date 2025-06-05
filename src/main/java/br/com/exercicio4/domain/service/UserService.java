package br.com.exercicio4.domain.service;

import br.com.exercicio4.domain.ValidationException;
import br.com.exercicio4.domain.model.User;
import br.com.exercicio4.infra.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    private final UserRepository repository;

    public UserService(UserRepository repository) {
        this.repository = repository;
    }

    @Transactional
    public User create(User user) {
        if (repository.existsByEmail(user.getEmail())) {
            throw new ValidationException("Já existe um usuário com esse e-mail.");
        }
        return repository.save(user);
    }

    public Page<User> listAll(Pageable pageable) {
        return repository.findAll(pageable);
    }

    public User findById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Usuário não encontrado com ID: " + id));
    }

    @Transactional
    public User update(Long id, String name, String email) {
        User user = findById(id);

        if (email != null && !email.isBlank() && !email.equals(user.getEmail()) && repository.existsByEmail(email)) {
            throw new ValidationException("Email já está em uso por outro usuário.");
        }

        user.updateInfo(name, email);
        return repository.save(user);
    }

    @Transactional
    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new EntityNotFoundException("Usuário não encontrado com ID: " + id);
        }

        try {
            repository.deleteById(id);
        } catch (DataIntegrityViolationException e) {
            throw e; // será tratado pelo ErrorHandler
        }
    }
}
