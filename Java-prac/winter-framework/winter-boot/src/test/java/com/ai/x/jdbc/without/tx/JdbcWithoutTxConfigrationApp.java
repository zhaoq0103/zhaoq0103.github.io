package com.ai.x.jdbc.without.tx;

import com.ai.x.annotation.ComponentScan;
import com.ai.x.annotation.Configuration;
import com.ai.x.annotation.Import;
import com.ai.x.jdbc.JdbcConfiguration;

@ComponentScan
@Configuration
@Import(JdbcConfiguration.class)
public class JdbcWithoutTxConfigrationApp {
}
