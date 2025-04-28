package com.pfe.login.repository;

import com.pfe.login.model.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ScheduledNotificationRepository extends JpaRepository<Notification, Long> {
    List<Notification> findByScheduleTimeLessThanEqualAndIsSentFalse(LocalDateTime dateTime);
    List<Notification> findByIsSentFalse();
}