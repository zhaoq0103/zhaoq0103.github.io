package com.ai.x.engine;

import com.ai.x.engine.support.InitParameters;
import jakarta.servlet.*;

import java.util.*;

public class ServletRegistrationImpl implements ServletRegistration.Dynamic{
    final ServletContext servletContext;
    final String name;
    final Servlet servlet;
    final List<String> urlPatterns = new ArrayList<>(4);
    final InitParameters initParameters = new InitParameters();

    boolean initialized = false;

    public ServletRegistrationImpl(ServletContext servletContext, String name, Servlet servlet) {
        this.servletContext = servletContext;
        this.name = name;
        this.servlet = servlet;
    }

    public ServletConfig getServletConfig() {
        return new ServletConfig() {
            @Override
            public String getServletName() {
                return ServletRegistrationImpl.this.name;
            }

            @Override
            public ServletContext getServletContext() {
                return ServletRegistrationImpl.this.servletContext;
            }

            @Override
            public String getInitParameter(String name) {
                return ServletRegistrationImpl.this.initParameters.getInitParameter(name);
            }

            @Override
            public Enumeration<String> getInitParameterNames() {
                return ServletRegistrationImpl.this.initParameters.getInitParameterNames();
            }
        };
    }

    @Override
    public void setLoadOnStartup(int i) {
        checkNotInitialized("setLoadOnStartup");
    }

    @Override
    public Set<String> setServletSecurity(ServletSecurityElement servletSecurityElement) {
        checkNotInitialized("setServletSecurity");
        throw new UnsupportedOperationException("Servlet security is not supported.");
    }

    @Override
    public void setMultipartConfig(MultipartConfigElement multipartConfigElement) {
        checkNotInitialized("setMultipartConfig");
        throw new UnsupportedOperationException("Multipart config is not supported.");
    }

    @Override
    public void setRunAsRole(String roleName) {
        checkNotInitialized("setRunAsRole");
        if (roleName != null) {
            throw new UnsupportedOperationException("Role is not supported.");
        }
    }

    @Override
    public void setAsyncSupported(boolean isAsyncSupported) {
        checkNotInitialized("setAsyncSupported");
        if (isAsyncSupported) {
            throw new UnsupportedOperationException("Async is not supported.");
        }
    }

    @Override
    public Set<String> addMapping(String... strings) {
        if (strings == null || strings.length == 0) {
            throw new IllegalArgumentException("Missing urlPatterns.");
        }
        for (String urlPattern : strings) {
            this.urlPatterns.add(urlPattern);
        }
        return Set.of();
    }

    @Override
    public Collection<String> getMappings() {
        return this.urlPatterns;
    }

    @Override
    public String getRunAsRole() {
        return null;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public String getClassName() {
        return this.servlet.getClass().getName();
    }

    @Override
    public boolean setInitParameter(String nm, String value) {
        checkNotInitialized("setInitParameter");
        return this.initParameters.setInitParameter(nm, value);
    }

    @Override
    public String getInitParameter(String nm) {
        return this.initParameters.getInitParameter(nm);
    }

    @Override
    public Set<String> setInitParameters(Map<String, String> map) {
        checkNotInitialized("setInitParameter");
        return this.initParameters.setInitParameters(map);
    }

    @Override
    public Map<String, String> getInitParameters() {
        return this.initParameters.getInitParameters();
    }

    private void checkNotInitialized(String name) {
        if (this.initialized) {
            throw new IllegalStateException("Cannot call " + name + " after initialization.");
        }
    }
}
