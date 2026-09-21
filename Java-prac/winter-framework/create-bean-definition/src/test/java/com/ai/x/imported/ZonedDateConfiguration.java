package com.ai.x.imported;

import java.time.ZonedDateTime;

import com.ai.x.annotation.Bean;
import com.ai.x.annotation.Configuration;


@Configuration
public class ZonedDateConfiguration {

    @Bean
    ZonedDateTime startZonedDateTime() {
        return ZonedDateTime.now();
    }
}
