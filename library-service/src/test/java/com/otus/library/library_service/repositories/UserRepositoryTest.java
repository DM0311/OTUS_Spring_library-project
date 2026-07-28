package com.otus.library.library_service.repositories;

import com.otus.library.library_service.model.entity.User;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Репозиторий для работы с пользователями:")
@DataJpaTest
public class UserRepositoryTest {

    private static final int USERS_INITIAL_COUNT = 4;

    @Autowired
    private UserRepository userRepository;

    @Test
    @DisplayName("- должен найти всех пользователей")
    void shouldFindAllUsers() {
        List<User> users = userRepository.findAll();
        assertThat(users).hasSize(USERS_INITIAL_COUNT);
    }

    @Test
    @DisplayName("- должен найти пользователя по ID")
    void shouldFindUserById() {
        User user = userRepository.findById(1L)
                .orElseThrow(()-> new EntityNotFoundException("User not found"));
        assertThat(user.getUserName()).isEqualTo("admin");
    }
    @Test
    @DisplayName("- должен создать нового пользователя")
    void shouldCreateUser() {
        User user = new User();
        user.setUserName("new user");
        user.setFullName("Новый Пользователь");
        user.setPassword("password123");
        user.setEmail("test@email.com");
        User saved = userRepository.save(user);
        List<User> userList = userRepository.findAll();

        assertThat(saved.getId()).isEqualTo(5L);
        assertThat(userList).hasSize(USERS_INITIAL_COUNT+1);
    }
    @Test
    @DisplayName("- должен удалить пользователя")
    void shouldDelete() {
        userRepository.deleteById(5L);
        List<User> users = userRepository.findAll();
        assertThat(users).hasSize(USERS_INITIAL_COUNT);
    }
}
