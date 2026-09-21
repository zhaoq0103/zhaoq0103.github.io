package com.ai.apps;


import com.ai.x.annotation.ComponentScan;
import com.ai.x.annotation.Configuration;
import com.ai.x.annotation.Import;
import com.ai.x.jdbc.JdbcConfiguration;
import com.ai.x.web.WebMvcConfiguration;

@ComponentScan
@Configuration
@Import({ JdbcConfiguration.class, WebMvcConfiguration.class })
public class HelloConfiguration {
}
