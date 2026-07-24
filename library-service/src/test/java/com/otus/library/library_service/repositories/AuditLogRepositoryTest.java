package com.otus.library.library_service.repositories;

import com.otus.library.library_service.model.entity.AuditLog;
import com.otus.library.library_service.model.entity.User;
import com.otus.library.library_service.model.enums.ActionType;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;


@DisplayName("Репозиторий для работы с логами:")
@DataJpaTest
public class AuditLogRepositoryTest {

    @Autowired
    private AuditLogRepository repository;

    @BeforeAll
    static void setupAllData(@Autowired AuditLogRepository logRepository,
                             @Autowired UserRepository userRepository) throws InterruptedException {
        User user = userRepository.findById(1L)
                .orElseThrow(()-> new EntityNotFoundException("User not found"));

        AuditLog log1 = new AuditLog();
        log1.setUser(user);
        log1.setActionType(ActionType.BOOK_CREATE);
        log1.setEntityType(User.class.getSimpleName());
        logRepository.save(log1);

        Thread.sleep(10);

        AuditLog log2 = new AuditLog();
        log2.setUser(user);
        log2.setActionType(ActionType.AUTHOR_CREATE);
        log2.setEntityType(User.class.getSimpleName());
        logRepository.save(log2);
    }

    @Test
    @DisplayName("- должен найти все логи по убыванию даты создания")
    void shouldFindAllLogsDesc() {
        List<AuditLog> logs = repository.findAllByOrderByCreatedAtDesc();
        List<ActionType> actions = logs.stream().map(AuditLog::getActionType).toList();
        assertThat(actions).containsExactly(ActionType.AUTHOR_CREATE, ActionType.BOOK_CREATE);
    }
}
