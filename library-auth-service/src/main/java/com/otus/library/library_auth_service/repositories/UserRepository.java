package com.otus.library.library_auth_service.repositories;


import com.otus.library.library_auth_service.model.entity.User;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

    //TODO: clean up
    @EntityGraph(attributePaths = {"roles"})
    Optional<User> findByEmail(String email);

    @EntityGraph(attributePaths = {"roles"})
    Optional<User> findByUserName(String userName);

    //TODO: clean up
    @EntityGraph(attributePaths = {"roles"})
    Optional<User> findById(Long id);

    //TODO: clean up
    boolean existsByEmail(String email);

    //TODO: clean up
    @EntityGraph(attributePaths = {"roles"})
    List<User> findByIsBlockedTrueAndBlockedUntilBefore(LocalDateTime date);
}
