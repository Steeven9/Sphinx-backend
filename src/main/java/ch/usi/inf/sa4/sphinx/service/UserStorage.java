package ch.usi.inf.sa4.sphinx.service;

import ch.usi.inf.sa4.sphinx.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserStorage extends JpaRepository<User, Integer> {
    Optional<User> findByUsername(String username);
    User findByEmail(String email);
    void deleteByUsername(String username);

}
