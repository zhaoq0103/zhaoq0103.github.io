package com.ai.x.engine;

import com.ai.x.Config;
import com.ai.x.engine.mapping.FilterMapping;
import com.ai.x.engine.mapping.ServletMapping;
import com.ai.x.engine.servlet.DefaultServlet;
import com.ai.x.engine.support.Attributes;
import com.ai.x.utils.AnnoUtils;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.annotation.WebListener;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.descriptor.JspConfigDescriptor;
import jakarta.servlet.http.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.lang.reflect.Constructor;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

public class ServletContextImpl implements ServletContext {
    final Logger logger = LoggerFactory.getLogger(getClass());
    final ClassLoader classLoader;
    final Config config;
    // web root dir:
    final Path webRoot;

    private boolean initialized = false;
    private SessionCookieConfig sessionCookieConfig;
    Servlet defaultServlet;

    // 设置 session 10分钟过期
    final SessionManager sessionManager;
    private Attributes attributes = new Attributes(true);
    private Map<String, ServletRegistrationImpl> servletRegistrations = new HashMap<>();
    final Map<String, FilterRegistrationImpl> filterRegistrations = new HashMap<>();
    final Map<String, Servlet> nameToServlets = new HashMap<>();
    final Map<String, Filter> nameToFilters = new HashMap<>();

    final List<ServletMapping> servletMappings = new ArrayList<>();
    final List<FilterMapping> filterMappings = new ArrayList<>();

    private List<ServletContextListener> servletContextListeners = null;
    private List<ServletContextAttributeListener> servletContextAttributeListeners = null;
    private List<ServletRequestListener> servletRequestListeners = null;
    private List<ServletRequestAttributeListener> servletRequestAttributeListeners = null;
    private List<HttpSessionAttributeListener> httpSessionAttributeListeners = null;
    private List<HttpSessionListener> httpSessionListeners = null;


    public ServletContextImpl(ClassLoader classLoader, Config config, String webRoot) {
        this.classLoader = classLoader;
        this.config = config;
        this.sessionCookieConfig = new SessionCookieConfigImpl(config);
        this.webRoot = Paths.get(webRoot).normalize().toAbsolutePath();
        this.sessionManager = new SessionManager(this, config.server.webApp.sessionTimeout);
        logger.info("set web root: {}", this.webRoot);
    }

    public void initialize(List<Class<?>> autoScannedClasses) {
        if (this.initialized) {
            throw new IllegalStateException("Cannot re-initialize.");
        }

        this.initListeners(autoScannedClasses);
        this.invokeServletContextInitialized();

        this.initServlets(autoScannedClasses);
        this.initFilters(autoScannedClasses);
        this.initialized = true;
    }
    public void process(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String path = request.getRequestURI();
        // search servlet:
        Servlet servlet = this.defaultServlet;
        for (ServletMapping mapping : this.servletMappings) {
            if (mapping.matches(path)) {
                servlet = mapping.servlet;
                break;
            }
        }
        if (servlet == null) {
            // 404 Not Found:
            PrintWriter pw = response.getWriter();
            pw.write("<h1>404 Not Found</h1><p>No mapping for URL: " + path + "</p>");
            pw.close();
            return;
        }

        //filter
        List<Filter> enabledFilters = new ArrayList<>();
        for (FilterMapping mapping : this.filterMappings) {
            if (mapping.matches(path)) {
                enabledFilters.add(mapping.filter);
            }
        }
        Filter[] filters = enabledFilters.toArray(Filter[]::new);
        logger.atDebug().log("process {} by filter {}, servlet {}", path, Arrays.toString(filters), servlet);
        FilterChain chain = new FilterChainImpl(filters, servlet);
        try {
            this.invokeServletRequestInitialized(request);
            chain.doFilter(request, response);
        } catch (ServletException e) {
            logger.error(e.getMessage(), e);
            throw new IOException(e);
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
            throw e;
        }finally {
            this.invokeServletRequestDestroyed(request);
        }
    }


    public void initFilters(List<Class<?>> filterClasses) {
        for (Class<?> c : filterClasses) {
            WebFilter wf = c.getAnnotation(WebFilter.class);
            if (wf != null) {
                logger.info("auto register @WebFilter: {}", c.getName());
                @SuppressWarnings("unchecked")
                Class<? extends Filter> clazz = (Class<? extends Filter>) c;
                FilterRegistration.Dynamic registration = this.addFilter(AnnoUtils.getFilterName(clazz), clazz);
                registration.addMappingForUrlPatterns(EnumSet.of(DispatcherType.REQUEST), true, AnnoUtils.getFilterUrlPatterns(clazz));
                registration.setInitParameters(AnnoUtils.getFilterInitParams(clazz));
            }
        }

        // init filters:
        for (String name : this.filterRegistrations.keySet()) {
            var registration = this.filterRegistrations.get(name);
            try {
                registration.filter.init(registration.getFilterConfig());
                this.nameToFilters.put(name, registration.filter);
                for (String urlPattern : registration.getUrlPatternMappings()) {
                    this.filterMappings.add(new FilterMapping(name, urlPattern, registration.filter));
                }
                registration.initialized = true;
            } catch (ServletException e) {
                logger.error("init filter failed: " + name + " / " + registration.filter.getClass().getName(), e);
            }
        }

        // important: sort by filter name:
        Collections.sort(this.filterMappings, (f1, f2) -> {
            int cmp = f1.filterName.compareTo(f2.filterName);
            if (cmp == 0) {
                cmp = f1.compareTo(f2);
            }
            return cmp;
        });
    }

    @Override
    public FilterRegistration.Dynamic addFilter(String name, String className) {
        if (className == null || className.isEmpty()) {
            throw new IllegalArgumentException("class name is null or empty.");
        }
        Filter filter = null;
        try {
            Class<? extends Filter> clazz = createInstance(className);
            filter = createInstance(clazz);
        } catch (ServletException e) {
            throw new RuntimeException(e);
        }
        return addFilter(name, filter);
    }

    @Override
    public FilterRegistration.Dynamic addFilter(String name, Class<? extends Filter> clazz) {
        if (clazz == null) {
            throw new IllegalArgumentException("class is null.");
        }
        Filter filter = null;
        try {
            filter = createInstance(clazz);
        } catch (ServletException e) {
            throw new RuntimeException(e);
        }
        return addFilter(name, filter);
    }

    @Override
    public FilterRegistration.Dynamic addFilter(String name, Filter filter) {
        if (name == null) {
            throw new IllegalArgumentException("name is null.");
        }
        if (filter == null) {
            throw new IllegalArgumentException("filter is null.");
        }
        var registration = new FilterRegistrationImpl(this, name, filter);
        this.filterRegistrations.put(name, registration);
        return registration;
    }

    @Override
    public <T extends Filter> T createFilter(Class<T> clazz) throws ServletException {
        return createInstance(clazz);
    }

    @Override
    public FilterRegistration getFilterRegistration(String name) {
        return this.filterRegistrations.get(name);
    }

    @Override
    public Map<String, ? extends FilterRegistration> getFilterRegistrations() {
        return Map.copyOf(this.filterRegistrations);
    }

    public void initListeners(List<Class<?>> listenerClasses) {
        // register @WebListener:
        for (Class<?> c : listenerClasses) {
            if (c.isAnnotationPresent(WebListener.class)) {
                logger.info("auto register @WebListener: {}", c.getName());
                @SuppressWarnings("unchecked")
                Class<? extends EventListener> clazz = (Class<? extends EventListener>) c;
                this.addListener(clazz);
            }
        }
    }

    public void initServlets(List<Class<?>> servletClasses) {
        for (Class<?> c : servletClasses) {
            WebServlet ws = c.getAnnotation(WebServlet.class);
            if (ws != null) {
                logger.info("auto register @WebServlet: {}", c.getName());
                @SuppressWarnings("unchecked")
                Class<? extends Servlet> clazz = (Class<? extends Servlet>) c;
                ServletRegistration.Dynamic registration = this.addServlet(AnnoUtils.getServletName(clazz), clazz);
                registration.addMapping(AnnoUtils.getServletUrlPatterns(clazz));
                registration.setInitParameters(AnnoUtils.getServletInitParams(clazz));
            }
        }

        // init servlets:
        // init servlets while find default servlet:
        Servlet defaultServlet = null;
        for (String name : this.servletRegistrations.keySet()) {
            var registration = this.servletRegistrations.get(name);
            try {
                registration.servlet.init(registration.getServletConfig());
                this.nameToServlets.put(name, registration.servlet);
                for (String urlPattern : registration.getMappings()) {
                    this.servletMappings.add(new ServletMapping(urlPattern, registration.servlet));
                    if (urlPattern.equals("/")) {
                        if (defaultServlet == null) {
                            defaultServlet = registration.servlet;
                            logger.info("set default servlet: " + registration.getClassName());
                        } else {
                            logger.warn("found duplicate default servlet: " + registration.getClassName());
                        }
                    }
                }
                registration.initialized = true;
            } catch (ServletException e) {
                logger.error("init servlet failed: " + name + " / " + registration.servlet.getClass().getName(), e);
            }
        }

        if (defaultServlet == null && config.server.webApp.fileListings) {
            logger.info("no default servlet. auto register {}...", DefaultServlet.class.getName());
            defaultServlet = new DefaultServlet();
            try {
                defaultServlet.init(new ServletConfig() {
                    @Override
                    public String getServletName() {
                        return "DefaultServlet";
                    }

                    @Override
                    public ServletContext getServletContext() {
                        return ServletContextImpl.this;
                    }

                    @Override
                    public String getInitParameter(String name) {
                        return null;
                    }

                    @Override
                    public Enumeration<String> getInitParameterNames() {
                        return Collections.emptyEnumeration();
                    }
                });
                this.servletMappings.add(new ServletMapping("/", defaultServlet));
            } catch (ServletException e) {
                logger.error("init default servlet failed.", e);
            }
        }
        this.defaultServlet = defaultServlet;
        // important: sort mappings:
        Collections.sort(this.servletMappings);
    }

    @Override
    public ServletRegistration.Dynamic addServlet(String name, String className) {
        if (className == null || className.isEmpty()) {
            throw new IllegalArgumentException("class name is null or empty.");
        }
        Servlet servlet = null;
        try {
            Class<? extends Servlet> clazz = createInstance(className);
            servlet = createInstance(clazz);
        } catch (ServletException e) {
            throw new RuntimeException(e);
        }
        return addServlet(name, servlet);
    }

    @SuppressWarnings("unchecked")
    private <T> T createInstance(String className) throws ServletException {
        Class<T> clazz;
        try {
            clazz = (Class<T>) Class.forName(className);
        } catch (ClassNotFoundException e) {
            throw new IllegalArgumentException("Class not found.", e);
        }
        return createInstance(clazz);
    }

    private <T> T createInstance(Class<T> clazz) throws ServletException {
        try {
            Constructor<T> constructor = clazz.getConstructor();
            return constructor.newInstance();
        } catch (ReflectiveOperationException e) {
            throw new ServletException("Cannot instantiate class " + clazz.getName(), e);
        }
    }

    @Override
    public ServletRegistration.Dynamic addServlet(String name, Servlet servlet) {
        if (name == null) {
            throw new IllegalArgumentException("name is null.");
        }
        if (servlet == null) {
            throw new IllegalArgumentException("servlet is null.");
        }
        var registration = new ServletRegistrationImpl(this, name, servlet);
        this.servletRegistrations.put(name, registration);
        return registration;
    }

    @Override
    public ServletRegistration.Dynamic addServlet(String name, Class<? extends Servlet> clazz) {
        if (clazz == null) {
            throw new IllegalArgumentException("class is null.");
        }
        Servlet servlet = null;
        try {
            servlet = createInstance(clazz);
        } catch (ServletException e) {
            throw new RuntimeException(e);
        }
        return addServlet(name, servlet);
    }

    @Override
    public <T extends Servlet> T createServlet(Class<T> aClass) throws ServletException {
        return createInstance(aClass);
    }

    @Override
    public ServletRegistration getServletRegistration(String name) {
        return this.servletRegistrations.get(name);
    }

    @Override
    public Map<String, ? extends ServletRegistration> getServletRegistrations() {
        return Map.copyOf(this.servletRegistrations);
    }

    @Override
    public String getContextPath() {
        return "";
    }

    @Override
    public ServletContext getContext(String uripath) {
        if ("".equals(uripath)) {
            return this;
        }

        return null;
    }

    @Override
    public int getMajorVersion() {
        return 0;
    }

    @Override
    public int getMinorVersion() {
        return 0;
    }

    @Override
    public int getEffectiveMajorVersion() {
        return 0;
    }

    @Override
    public int getEffectiveMinorVersion() {
        return 0;
    }

    @Override
    public String getMimeType(String file) {
        return config.server.getMimeType(file);
    }

    @Override
    public Set<String> getResourcePaths(String path) {
        String originPath = path;
        if (path.startsWith("/")) {
            path = path.substring(1);
        }
        Path loc = this.webRoot.resolve(path).normalize();
        if (loc.startsWith(this.webRoot)) {
            if (Files.isDirectory(loc)) {
                try {
                    return Files.list(loc).map(p -> p.getFileName().toString()).collect(Collectors.toSet());
                } catch (IOException e) {
                    logger.warn("list files failed for path: {}", originPath);
                }
            }
        }
        return null;
    }

    @Override
    public URL getResource(String path) throws MalformedURLException {
        String originPath = path;
        if (path.startsWith("/")) {
            path = path.substring(1);
        }
        Path loc = this.webRoot.resolve(path).normalize();
        if (loc.startsWith(this.webRoot)) {
            return URI.create("file://" + loc.toString()).toURL();
        }
        throw new MalformedURLException("Path not found: " + originPath);
    }

    @Override
    public InputStream getResourceAsStream(String path) {
        if (path.startsWith("/")) {
            path = path.substring(1);
        }
        Path loc = this.webRoot.resolve(path).normalize();
        if (loc.startsWith(this.webRoot)) {
            if (Files.isReadable(loc)) {
                try {
                    return new BufferedInputStream(new FileInputStream(loc.toFile()));
                } catch (FileNotFoundException e) {
                    throw new UncheckedIOException(e);
                }
            }
        }
        return null;
    }

    @Override
    public RequestDispatcher getRequestDispatcher(String s) {
        return null;
    }

    @Override
    public RequestDispatcher getNamedDispatcher(String s) {
        return null;
    }

    @Override
    public void log(String s) {

    }

    @Override
    public void log(String s, Throwable throwable) {

    }

    @Override
    public String getRealPath(String path) {
        if (path.startsWith("/")) {
            path = path.substring(1);
        }
        Path loc = this.webRoot.resolve(path).normalize();
        if (loc.startsWith(this.webRoot)) {
            return loc.toString();
        }
        return null;
    }

    @Override
    public String getServerInfo() {
        return this.config.server.name;
    }

    @Override
    public String getInitParameter(String s) {
        return null;
    }

    @Override
    public Enumeration<String> getInitParameterNames() {
        return Collections.emptyEnumeration();
    }

    @Override
    public boolean setInitParameter(String s, String s1) {
        throw new UnsupportedOperationException("setInitParameter");
    }

    @Override
    public Object getAttribute(String s) {
        return this.attributes.getAttribute(s);
    }

    @Override
    public Enumeration<String> getAttributeNames() {
        return this.attributes.getAttributeNames();
    }

    @Override
    public void setAttribute(String name, Object value) {
        if (value == null) {
            removeAttribute(name);
        } else {
            Object old = this.attributes.setAttribute(name, value);
            if (old == null) {
                this.invokeServletContextAttributeAdded(name, value);
            } else {
                this.invokeServletContextAttributeReplaced(name, value);
            }
        }
    }

    @Override
    public void removeAttribute(String name) {
        Object old = this.attributes.removeAttribute(name);
        this.invokeServletContextAttributeRemoved(name, old);
    }

    @Override
    public String getServletContextName() {
        return null;
    }



    @Override
    public ServletRegistration.Dynamic addJspFile(String s, String s1) {
        return null;
    }



    @Override
    public SessionCookieConfig getSessionCookieConfig() {
        return null;
    }

    @Override
    public void setSessionTrackingModes(Set<SessionTrackingMode> set) {

    }

    @Override
    public Set<SessionTrackingMode> getDefaultSessionTrackingModes() {
        return null;
    }

    @Override
    public Set<SessionTrackingMode> getEffectiveSessionTrackingModes() {
        return null;
    }

    @Override
    public void addListener(String className) {
        EventListener listener = null;
        try {
            Class<EventListener> clazz = createInstance(className);
            listener = createInstance(clazz);
        } catch (ServletException e) {
            throw new RuntimeException(e);
        }
        addListener(listener);
    }

    @Override
    public void addListener(Class<? extends EventListener> aClass) {
        EventListener listener = null;
        try {
            listener = createInstance(aClass);
        } catch (ServletException e) {
            throw new RuntimeException(e);
        }
        addListener(listener);
    }

    @Override
    public <T extends EventListener> void addListener(T t) {
        if (t instanceof ServletContextListener listener) {
            if (this.servletContextListeners == null) {
                this.servletContextListeners = new ArrayList<>();
            }
            this.servletContextListeners.add(listener);
        } else if (t instanceof ServletContextAttributeListener listener) {
            if (this.servletContextAttributeListeners == null) {
                this.servletContextAttributeListeners = new ArrayList<>();
            }
            this.servletContextAttributeListeners.add(listener);
        } else if (t instanceof ServletRequestListener listener) {
            if (this.servletRequestListeners == null) {
                this.servletRequestListeners = new ArrayList<>();
            }
            this.servletRequestListeners.add(listener);
        } else if (t instanceof ServletRequestAttributeListener listener) {
            if (this.servletRequestAttributeListeners == null) {
                this.servletRequestAttributeListeners = new ArrayList<>();
            }
            this.servletRequestAttributeListeners.add(listener);
        } else if (t instanceof HttpSessionAttributeListener listener) {
            if (this.httpSessionAttributeListeners == null) {
                this.httpSessionAttributeListeners = new ArrayList<>();
            }
            this.httpSessionAttributeListeners.add(listener);
        } else if (t instanceof HttpSessionListener listener) {
            if (this.httpSessionListeners == null) {
                this.httpSessionListeners = new ArrayList<>();
            }
            this.httpSessionListeners.add(listener);
        } else {
            throw new IllegalArgumentException("Unsupported listener: " + t.getClass().getName());
        }
    }

    // invoke listeners ///////////////////////////////////////////////////////
    void invokeServletContextInitialized() {
        logger.debug("invoke ServletContextInitialized: {}", this);
        if (this.servletContextListeners != null) {
            var event = new ServletContextEvent(this);
            for (var listener : this.servletContextListeners) {
                listener.contextInitialized(event);
            }
        }
    }

    void invokeServletContextDestroyed() {
        logger.debug("invoke ServletContextDestroyed: {}", this);
        if (this.servletContextListeners != null) {
            var event = new ServletContextEvent(this);
            for (var listener : this.servletContextListeners) {
                listener.contextDestroyed(event);
            }
        }
    }
    void invokeServletContextAttributeAdded(String name, Object value) {
        logger.info("invoke ServletContextAttributeAdded: {} = {}", name, value);
        if (this.servletContextAttributeListeners != null) {
            var event = new ServletContextAttributeEvent(this, name, value);
            for (var listener : this.servletContextAttributeListeners) {
                listener.attributeAdded(event);
            }
        }
    }

    void invokeServletContextAttributeRemoved(String name, Object value) {
        logger.info("invoke ServletContextAttributeRemoved: {} = {}", name, value);
        if (this.servletContextAttributeListeners != null) {
            var event = new ServletContextAttributeEvent(this, name, value);
            for (var listener : this.servletContextAttributeListeners) {
                listener.attributeRemoved(event);
            }
        }
    }

    void invokeServletContextAttributeReplaced(String name, Object value) {
        logger.info("invoke ServletContextAttributeReplaced: {} = {}", name, value);
        if (this.servletContextAttributeListeners != null) {
            var event = new ServletContextAttributeEvent(this, name, value);
            for (var listener : this.servletContextAttributeListeners) {
                listener.attributeReplaced(event);
            }
        }
    }

    void invokeServletRequestAttributeAdded(HttpServletRequest request, String name, Object value) {
//        logger.info("invoke ServletRequestAttributeAdded: {} = {}, request = {}", name, value, request);
        if (this.servletRequestAttributeListeners != null) {
            var event = new ServletRequestAttributeEvent(this, request, name, value);
            for (var listener : this.servletRequestAttributeListeners) {
                listener.attributeAdded(event);
            }
        }
    }

    void invokeServletRequestAttributeRemoved(HttpServletRequest request, String name, Object value) {
//        logger.info("invoke ServletRequestAttributeRemoved: {} = {}, request = {}", name, value, request);
        if (this.servletRequestAttributeListeners != null) {
            var event = new ServletRequestAttributeEvent(this, request, name, value);
            for (var listener : this.servletRequestAttributeListeners) {
                listener.attributeRemoved(event);
            }
        }
    }

    void invokeServletRequestAttributeReplaced(HttpServletRequest request, String name, Object value) {
        logger.info("invoke ServletRequestAttributeReplaced: {} = {}, request = {}", name, value, request);
        if (this.servletRequestAttributeListeners != null) {
            var event = new ServletRequestAttributeEvent(this, request, name, value);
            for (var listener : this.servletRequestAttributeListeners) {
                listener.attributeReplaced(event);
            }
        }
    }

    void invokeHttpSessionAttributeAdded(HttpSession session, String name, Object value) {
        logger.info("invoke HttpSessionAttributeAdded: {} = {}, session = {}", name, value, session);
        if (this.httpSessionAttributeListeners != null) {
            var event = new HttpSessionBindingEvent(session, name, value);
            for (var listener : this.httpSessionAttributeListeners) {
                listener.attributeAdded(event);
            }
        }
    }

    void invokeHttpSessionAttributeRemoved(HttpSession session, String name, Object value) {
        logger.info("invoke ServletContextAttributeRemoved: {} = {}, session = {}", name, value, session);
        if (this.httpSessionAttributeListeners != null) {
            var event = new HttpSessionBindingEvent(session, name, value);
            for (var listener : this.httpSessionAttributeListeners) {
                listener.attributeRemoved(event);
            }
        }
    }

    void invokeHttpSessionAttributeReplaced(HttpSession session, String name, Object value) {
        logger.info("invoke ServletContextAttributeReplaced: {} = {}, session = {}", name, value, session);
        if (this.httpSessionAttributeListeners != null) {
            var event = new HttpSessionBindingEvent(session, name, value);
            for (var listener : this.httpSessionAttributeListeners) {
                listener.attributeReplaced(event);
            }
        }
    }

    void invokeServletRequestInitialized(HttpServletRequest request) {
        logger.info("invoke ServletRequestInitialized: request = {}", request);
        if (this.servletRequestListeners != null) {
            var event = new ServletRequestEvent(this, request);
            for (var listener : this.servletRequestListeners) {
                listener.requestInitialized(event);
            }
        }
    }

    void invokeServletRequestDestroyed(HttpServletRequest request) {
        logger.info("invoke ServletRequestDestroyed: request = {}", request);
        if (this.servletRequestListeners != null) {
            var event = new ServletRequestEvent(this, request);
            for (var listener : this.servletRequestListeners) {
                listener.requestDestroyed(event);
            }
        }
    }

    void invokeHttpSessionCreated(HttpSession session) {
        logger.info("invoke HttpSessionCreated: session = {}", session);
        if (this.httpSessionListeners != null) {
            var event = new HttpSessionEvent(session);
            for (var listener : this.httpSessionListeners) {
                listener.sessionCreated(event);
            }
        }
    }

    void invokeHttpSessionDestroyed(HttpSession session) {
        logger.info("invoke HttpSessionDestroyed: session = {}", session);
        if (this.httpSessionListeners != null) {
            var event = new HttpSessionEvent(session);
            for (var listener : this.httpSessionListeners) {
                listener.sessionDestroyed(event);
            }
        }
    }

    @Override
    public <T extends EventListener> T createListener(Class<T> aClass) throws ServletException {
        return null;
    }

    @Override
    public JspConfigDescriptor getJspConfigDescriptor() {
        return null;
    }

    @Override
    public ClassLoader getClassLoader() {
        return null;
    }

    @Override
    public void declareRoles(String... strings) {

    }

    public void destroy() {
        // destroy filter and servlet:
        this.filterMappings.forEach(mapping -> {
            try {
                mapping.filter.destroy();
            } catch (Exception e) {
                logger.error("destroy filter '" + mapping.filter + "' failed.", e);
            }
        });

        this.servletMappings.forEach(mapping -> {
            try {
                mapping.servlet.destroy();
            } catch (Exception e) {
                logger.error("destroy servlet '" + mapping.servlet + "' failed.", e);
            }
        });

        // notify:
        this.invokeServletContextDestroyed();
    }
    @Override
    public String getVirtualServerName() {
        return null;
    }

    @Override
    public int getSessionTimeout() {
        return 0;
    }

    @Override
    public void setSessionTimeout(int i) {

    }

    @Override
    public String getRequestCharacterEncoding() {
        return null;
    }

    @Override
    public void setRequestCharacterEncoding(String s) {

    }

    @Override
    public String getResponseCharacterEncoding() {
        return null;
    }

    @Override
    public void setResponseCharacterEncoding(String s) {

    }
}
