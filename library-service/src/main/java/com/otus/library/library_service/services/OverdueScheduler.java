package com.otus.library.library_service.services;

import com.otus.library.library_service.events.AuditEvent;
import com.otus.library.library_service.events.NotificationEvent;
import com.otus.library.library_service.model.entity.Booking;
import com.otus.library.library_service.model.entity.User;
import com.otus.library.library_service.model.enums.ActionType;
import com.otus.library.library_service.model.enums.BookingStatus;
import com.otus.library.library_service.model.enums.NotificationType;
import com.otus.library.library_service.repositories.BookingRepository;
import com.otus.library.library_service.repositories.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@RequiredArgsConstructor
@Slf4j
@Component
public class OverdueScheduler {

    private static final String OVERDUE_CHECK = "Запуск проверки просроченных бронирований";

    private static final String AUTO_UNBLOCK = "Запуск разблокировки пользователей";

    private final BookingRepository bookingRepository;

    private final UserRepository userRepository;

    private final ApplicationEventPublisher eventPublisher;

    @Scheduled(cron = "0 0 0 * * ?")
    @Transactional
    public void checkOverdueBookings() {

        publishAuditEvent(ActionType.SCHEDULER_RUN, OverdueScheduler.class.getSimpleName(), OVERDUE_CHECK, null);

        LocalDateTime now = LocalDateTime.now();
        List<Booking> overdueBookings = bookingRepository.findByStatusAndDueDateBefore(BookingStatus.ACTIVE, now);

        if (overdueBookings.isEmpty()) {
            log.info("Просроченных бронирований нет");
            return;
        }

        for (Booking booking : overdueBookings) {
            processOverdue(booking);
        }

        log.info("Проверка завершена, обработано {} просроченных бронирований", overdueBookings.size());
    }

    private void processOverdue(Booking booking) {

        User user = booking.getUser();
        int penalty = user.getPenaltyPoints() + 1;
        user.setPenaltyPoints(penalty);

        publishNotificationEvent(user, "Вы просрочили книгу - начислен штрафной балл.", NotificationType.OVERDUE);

        if (penalty >= 10) {
            user.setBlocked(true);
            user.setBlockedUntil(LocalDateTime.now().plusDays(30));
            String notificationMessage = "Аккаунт заблокирован - превышение штрафных баллов.";
            publishNotificationEvent(user, notificationMessage, NotificationType.USER_BLOCKED);
            publishAuditEvent(ActionType.USER_AUTO_BLOCKED, User.class.getSimpleName(), notificationMessage, user);

        } else {
            String notificationMessage = "Начислен штрафной балл. Всего: " + penalty;
            publishNotificationEvent(user, notificationMessage, NotificationType.PENALTY);
            publishAuditEvent(ActionType.PENALTY_APPLIED, User.class.getSimpleName(), notificationMessage, user);
        }

        userRepository.save(user);

        booking.setStatus(BookingStatus.OVERDUE);
        bookingRepository.save(booking);
    }

    @Scheduled(cron = "0 10 0 * * ?")
    @Transactional
    public void unblockUsers() {

        publishAuditEvent(ActionType.SCHEDULER_RUN, OverdueScheduler.class.getSimpleName(), AUTO_UNBLOCK, null);

        List<User> blockedUsers = userRepository.findByIsBlockedTrueAndBlockedUntilBefore(LocalDateTime.now());

        if (blockedUsers.isEmpty()) {
            log.info("Пользователей для разблокировки нет");
            return;
        }

        for (User user : blockedUsers) {
            user.setBlocked(false);
            user.setBlockedUntil(null);
            userRepository.save(user);
            String notificationMessage = "Аккаунт разблокирован";
            publishNotificationEvent(user, notificationMessage, NotificationType.USER_UNBLOCKED);
            publishAuditEvent(ActionType.USER_UNBLOCK, User.class.getSimpleName(), notificationMessage, user);
        }
    }

    private void publishAuditEvent(ActionType actionType, String entityType, String details, User user) {
        eventPublisher.publishEvent(AuditEvent.builder()
                .actionType(actionType)
                .entityType(entityType)
                .details(details)
                .user(user)
                .build());
    }

    private void publishNotificationEvent(User user, String message, NotificationType notificationType) {
        eventPublisher.publishEvent(NotificationEvent.builder()
                .user(user)
                .message(message)
                .type(notificationType)
                .build());
    }
}
