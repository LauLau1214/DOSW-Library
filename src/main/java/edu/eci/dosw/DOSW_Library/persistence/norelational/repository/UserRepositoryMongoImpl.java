package edu.eci.dosw.DOSW_Library.persistence.norelational.repository;

import edu.eci.dosw.DOSW_Library.core.model.User;
import edu.eci.dosw.DOSW_Library.persistence.UserRepository;
import edu.eci.dosw.DOSW_Library.persistence.norelational.mapper.UserDocumentMapper;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
@Profile("mongo")
public class UserRepositoryMongoImpl implements UserRepository {

    private final UserMongoRepository repository;

    public UserRepositoryMongoImpl(UserMongoRepository repository) {
        this.repository = repository;
    }

    @Override
    public User save(User user) {
        return UserDocumentMapper.toDomain(
                repository.save(UserDocumentMapper.toDocument(user))
        );
    }

    @Override
    public Optional<User> findById(String id) {
        return repository.findById(id).map(UserDocumentMapper::toDomain);
    }

    @Override
    public Optional<User> findByUsername(String username) {
        return repository.findByUsername(username).map(UserDocumentMapper::toDomain);
    }

    @Override
    public List<User> findAll() {
        return repository.findAll().stream()
                .map(UserDocumentMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public void delete(String id) {
        repository.deleteById(id);
    }

    @Override
    public boolean existsById(String id) {
        return repository.existsById(id);
    }
}