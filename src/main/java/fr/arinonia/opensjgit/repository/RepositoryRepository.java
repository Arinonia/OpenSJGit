package fr.arinonia.opensjgit.repository;

import fr.arinonia.opensjgit.entity.Repository;
import fr.arinonia.opensjgit.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

//Sound funny haha   (sorry it's 3AM and I have to wake up in 4 :c)
public interface RepositoryRepository extends JpaRepository<Repository, Long> {
    Page<Repository> findAllByOwner(User owner, Pageable pageable);
}
