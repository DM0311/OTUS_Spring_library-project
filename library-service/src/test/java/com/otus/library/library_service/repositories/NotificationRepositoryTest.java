package com.otus.library.library_service.repositories;

import com.otus.library.library_service.model.entity.Notification;
import com.otus.library.library_service.model.entity.User;
import com.otus.library.library_service.model.enums.NotificationType;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Репозиторий для работы с уведомлениями:")
@DataJpaTest
public class NotificationRepositoryTest {

    @Autowired
    private NotificationRepository notificationRepository;

    @BeforeAll
    static void setUpData(@Autowired NotificationRepository notificationRepository,
                          @Autowired UserRepository userRepository) throws InterruptedException {
        User user = userRepository.findById(1L)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        Notification notification1 = new Notification();
        notification1.setUser(user);
        notification1.setType(NotificationType.OVERDUE);
        notification1.setMessage("test msg 1");
        notification1.setRead(false);

        Notification notification2 = new Notification();
        notification2.setUser(user);
        notification2.setType(NotificationType.PENALTY);
        notification2.setMessage("test msg 2");
        notification2.setRead(false);

        notificationRepository.save(notification1);
        Thread.sleep(10);
        notificationRepository.save(notification2);
    }

    @Test
    @DisplayName("- должен создать уведомление")
    void shouldCreateNotification() {
        List<Notification> notificationList = notificationRepository.findAll();
        assertThat(notificationList).hasSize(2);
    }

    @Test
    @DisplayName("- должен найти уведомление по ID")
    void shouldFindById() {
        Notification notification = notificationRepository.findById(1L)
                .orElseThrow(() -> new EntityNotFoundException("Notification not found"));
        assertThat(notification.getType()).isEqualTo(NotificationType.OVERDUE);
        assertThat(notification.getMessage()).isEqualTo("test msg 1");

    }

    @Test
    @DisplayName("- должен найти непрочитанные уведомления пользователя")
    void shouldFindByUserIdAnIsReadFalse() {
        List<Notification> notifications = notificationRepository.findByUserIdAndIsReadFalse(1L);
        assertThat(notifications.size()).isEqualTo(2);
        assertThat(notifications.get(0).getMessage()).isEqualTo("test msg 1");
        assertThat(notifications.get(1).getMessage()).isEqualTo("test msg 2");

    }

    @Test
    @DisplayName("- должен найти все уведомления пользователя начиная с самого последнего")
    void shouldFindByUserIdOrderByCreatedAtDesc() {
        List<String> messages = notificationRepository.findByUserIdOrderByCreatedAtDesc(1L)
                .stream()
                .map(Notification::getMessage)
                .toList();
        assertThat(messages).containsExactly("test msg 2", "test msg 1");
    }
}
