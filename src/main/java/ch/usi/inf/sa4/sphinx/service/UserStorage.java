package ch.usi.inf.sa4.sphinx.service;

import ch.usi.inf.sa4.sphinx.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * User repository.
 */
@Repository
public interface UserStorage extends JpaRepository<User, Integer> {
    /**
     * finds a User by its unique username
     * @param username the username
     * @return the found User
     */
    @Transactional
    Optional<User> findByUsername(String username);
    /**
     * finds a User by its unique email
     * @param email the email
     * @return the found User
     */
    @Transactional
    Optional<User> findByEmail(String email);

    /**
     * Deletes a user by its unique username.
     * @param username the username
     */
    @Transactional
    void deleteByUsername(String username);

}
