package com.gmail.apachdima.asfosis.launcher;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication(
    scanBasePackages = {
        "com.gmail.apachdima.asfosis.launcher",
        "com.gmail.apachdima.asfosis.common",
        "com.gmail.apachdima.asfosis.tool",
        "com.gmail.apachdima.asfosis.user",
        "com.gmail.apachdima.asfosis.file.storage"
    })
@EnableJpaRepositories(basePackages = {
    "com.gmail.apachdima.asfosis.tool",
    "com.gmail.apachdima.asfosis.user",
    "com.gmail.apachdima.asfosis.file.storage"
})
@EntityScan(basePackages = {
    "com.gmail.apachdima.asfosis.tool",
    "com.gmail.apachdima.asfosis.user",
    "com.gmail.apachdima.asfosis.file.storage"
})
public class AsfosisApplication {

    public static void main(String[] args) {
        SpringApplication.run(AsfosisApplication.class, args);
    }

}
