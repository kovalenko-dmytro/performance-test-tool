package com.gmail.apachdima.ptt.launcher;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication(
    scanBasePackages = {
        "com.gmail.apachdima.ptt.launcher",
        "com.gmail.apachdima.ptt.common",
        "com.gmail.apachdima.ptt.user",
        "com.gmail.apachdima.ptt.file.storage",
        "com.gmail.apachdima.ptt.notification.email",
        "com.gmail.apachdima.ptt.notification.sms",
        "com.gmail.apachdima.ptt.execution"
    })
@EnableJpaRepositories(basePackages = {
    "com.gmail.apachdima.ptt.user",
    "com.gmail.apachdima.ptt.file.storage",
    "com.gmail.apachdima.ptt.notification.email",
    "com.gmail.apachdima.ptt.notification.sms",
    "com.gmail.apachdima.ptt.execution"
})
@EntityScan(basePackages = {
    "com.gmail.apachdima.ptt.user",
    "com.gmail.apachdima.ptt.file.storage",
    "com.gmail.apachdima.ptt.notification.email",
    "com.gmail.apachdima.ptt.notification.sms",
    "com.gmail.apachdima.ptt.execution"
})
@EnableAsync
public class PerformanceTestToolApplication {

    public static void main(String[] args) {
        SpringApplication.run(PerformanceTestToolApplication.class, args);
    }

}
