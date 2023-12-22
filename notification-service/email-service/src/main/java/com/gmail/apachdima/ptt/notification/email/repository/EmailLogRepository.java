package com.gmail.apachdima.ptt.notification.email.repository;

import com.gmail.apachdima.ptt.notification.email.model.EmailLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmailLogRepository extends JpaRepository<EmailLog, String> {
}
