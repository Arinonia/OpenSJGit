package fr.arinonia.opensjgit.service;

import fr.arinonia.opensjgit.entity.Repository;
import fr.arinonia.opensjgit.entity.User;
import fr.arinonia.opensjgit.repository.RepositoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class RepositoryService {

    private final RepositoryRepository repositoryRepository;

    @Autowired
    public RepositoryService(RepositoryRepository repositoryRepository) {
        this.repositoryRepository = repositoryRepository;
    }

    /*This all code is in testing, this is not a stable version*/
    public Repository createRepository(final String name, final String description, final boolean isPrivate, final User owner) {
        final Repository repository = new Repository();
        repository.setName(name);
        repository.setDescription(description);
        repository.setPrivate(isPrivate);
        repository.setOwner(owner);
        repository.setCreationDate(LocalDateTime.now());
        return this.repositoryRepository.save(repository);
    }

    public Repository createRepository(final Repository rep) {
        final Repository repository = new Repository();
        repository.setName(rep.getName());
        repository.setDescription(rep.getDescription());
        repository.setPrivate(rep.isPrivate());
        repository.setOwner(rep.getOwner());
        repository.setCreationDate(LocalDateTime.now());
        return this.repositoryRepository.save(repository);
    }

    public List<Repository> findAllRepositories() {
        return this.repositoryRepository.findAll();
    }

    public Optional<Repository> findRepositoryById(final Long id) {
        return this.repositoryRepository.findById(id);
    }

    public Repository updateRepository(final Long id, final String name, final String description, final boolean isPrivate) {
        final Repository repository = repositoryRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Repository not found with id " + id));
        repository.setName(name);
        repository.setDescription(description);
        repository.setPrivate(isPrivate);
        return this.repositoryRepository.save(repository);
    }

    public void deleteRepository(Long id) {
        this.repositoryRepository.deleteById(id);
    }

    public Page<Repository> findAllByUser(final User user, final Pageable pageable) {
        return this.repositoryRepository.findAllByOwner(user, pageable);
    }

    public List<Repository> findAllByOwner(final User owner) {
        return this.repositoryRepository.findAllByOwner(owner);
    }


    public Optional<Repository> findById(final Long id) {
        return this.repositoryRepository.findById(id);
    }

    public void save(final Repository repository) {
        this.repositoryRepository.save(repository);
    }

    public void delete(final Long id) {
        //TODO remove the repo folder
        this.repositoryRepository.deleteById(id);
    }
}
