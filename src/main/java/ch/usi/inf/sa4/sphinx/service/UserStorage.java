package ch.usi.inf.sa4.sphinx.service;

import ch.usi.inf.sa4.sphinx.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserStorage  extends JpaRepository<User, Integer> {
    User findByUsername(String username);

}
