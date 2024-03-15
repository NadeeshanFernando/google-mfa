package com.anton.mfa.repository;

import com.anton.mfa.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author by nadeeshan_fdz
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    User findByUsername(String username);

    User findByUsernameAndPassword(String username, String password);

    User findByUsernameAndSecretKey(String username, String secretKey);
}
