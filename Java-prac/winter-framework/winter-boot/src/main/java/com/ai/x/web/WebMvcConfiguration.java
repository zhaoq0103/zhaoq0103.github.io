package com.ai.x.web;


import com.ai.x.annotation.*;
import jakarta.servlet.ServletContext;

import java.util.Objects;

//@ComponentScan
@Configuration
public class WebMvcConfiguration {
    private static ServletContext servletContext = null;

    /**
     * Set by web listener.
     */
    static void setServletContext(ServletContext ctx) {
        servletContext = ctx;
    }

    @Bean(initMethod = "init")
    ViewResolver viewResolver( //
                               @Autowired ServletContext servletContext, //
                               @Value("${winter.web.freemarker.template-path:/WEB-INF/templates}") String templatePath, //
                               @Value("${winter.web.freemarker.template-encoding:UTF-8}") String templateEncoding) {
        return new FreeMarkerViewResolver(servletContext, templatePath, templateEncoding);
    }

    @Bean
    ServletContext servletContext() {
        return Objects.requireNonNull(servletContext, "ServletContext is not set.");
    }
}
