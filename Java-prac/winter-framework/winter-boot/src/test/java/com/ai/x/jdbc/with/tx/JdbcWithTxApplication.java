package com.ai.x.jdbc.with.tx;


import com.ai.x.annotation.ComponentScan;
import com.ai.x.annotation.Configuration;
import com.ai.x.annotation.Import;
import com.ai.x.jdbc.JdbcConfiguration;

@ComponentScan
@Configuration
@Import(JdbcConfiguration.class)
public class JdbcWithTxApplication {

}
