package ch.usi.inf.sa4.sphinx.service;

import ch.usi.inf.sa4.sphinx.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
public interface UserStorage extends JpaRepository<User, Integer> {
    @Transactional
    Optional<User> findByUsername(String username);
    @Transactional
    Optional<User> findByEmail(String email);
    @Transactional
    void deleteByUsername(String username);

}
