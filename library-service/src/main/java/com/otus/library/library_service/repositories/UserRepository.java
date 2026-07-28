package com.otus.library.library_service.repositories;

import com.otus.library.library_service.model.entity.User;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    @EntityGraph(attributePaths = {"roles"})
    Optional<User> findByUserName(String email);

    @EntityGraph(attributePaths = {"roles"})
    Optional<User> findById(Long id);

    boolean existsByEmail(String email);

    @EntityGraph(attributePaths = {"roles"})
    List<User> findByIsBlockedTrueAndBlockedUntilBefore(LocalDateTime date);
}
