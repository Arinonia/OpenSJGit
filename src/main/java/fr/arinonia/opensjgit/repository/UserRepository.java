package fr.arinonia.opensjgit.repository;

import fr.arinonia.opensjgit.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByUsername(final String username);
    User findByEmail(final String email);

}
