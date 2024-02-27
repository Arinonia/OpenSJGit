package fr.arinonia.opensjgit.repository;

import fr.arinonia.opensjgit.entity.Repository;
import fr.arinonia.opensjgit.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

//Sound funny haha   (sorry it's 3AM and I have to wake up in 4 :c)
public interface RepositoryRepository extends JpaRepository<Repository, Long> {
    Page<Repository> findAllByOwner(final User owner, final Pageable pageable);
    List<Repository> findAllByOwner(final User owner);
    Repository save(final Repository repository);
    Optional<Repository> findById(final Long id);
}
