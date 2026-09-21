package com.ai.x.imported;

import java.time.LocalDate;
import java.time.LocalDateTime;

import com.ai.x.annotation.Bean;
import com.ai.x.annotation.Configuration;

@Configuration
public class LocalDateConfiguration {

    @Bean
    LocalDate startLocalDate() {
        return LocalDate.now();
    }

    @Bean
    LocalDateTime startLocalDateTime() {
        return LocalDateTime.now();
    }
}
