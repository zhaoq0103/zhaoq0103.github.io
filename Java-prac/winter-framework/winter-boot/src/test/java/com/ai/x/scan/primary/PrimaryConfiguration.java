package com.ai.x.scan.primary;

import com.ai.x.annotation.Bean;
import com.ai.x.annotation.Configuration;
import com.ai.x.annotation.Primary;
@Configuration
public class PrimaryConfiguration {

    @Primary
    @Bean
    DogBean husky() {
        return new DogBean("Husky");
    }

    @Bean
    DogBean teddy() {
        return new DogBean("Teddy");
    }
}
