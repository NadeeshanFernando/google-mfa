package com.anton.mfa.repository;

import com.anton.mfa.model.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author by nadeeshan_fdz
 */
@Repository
public interface UserRepository extends JpaRepository<Users, Long> {
    Users findByUsername(String username);

    Users findByUsernameAndPassword(String username, String password);
}
