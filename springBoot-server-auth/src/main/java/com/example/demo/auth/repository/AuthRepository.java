// AuthRepository.java
package com.example.demo.auth.repository;

import com.example.demo.auth.model.Auth;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AuthRepository extends JpaRepository<Auth, Long> {
    Optional<Auth> findByEmail(String email);
    boolean existsByEmail(String email);
}
