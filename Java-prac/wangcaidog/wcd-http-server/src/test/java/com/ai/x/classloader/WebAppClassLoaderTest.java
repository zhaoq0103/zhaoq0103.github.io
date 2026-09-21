package com.ai.x.classloader;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.net.URL;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class WebAppClassLoaderTest {

    WebAppClassLoader cl;

    @BeforeEach
    void setUp() throws Exception {
        Path classPath = Path.of("src", "test", "resources", "test-classpath", "WEB-INF", "classes");
        Path libPath = Path.of("src", "test", "resources", "test-classpath", "WEB-INF", "lib");
        this.cl = new WebAppClassLoader(classPath, libPath);
    }

    @Test
    void testUrls() {
        URL[] urls = this.cl.getURLs();
        System.out.println(cl);
        Arrays.stream(urls).forEach(System.out::println);
        assertEquals(3, urls.length);
        assertTrue(urls[0].toString().endsWith("/test-classpath/WEB-INF/classes/"));
        assertTrue(urls[1].toString().endsWith("/test-classpath/WEB-INF/lib/logback-core-1.4.11.jar"));
        assertTrue(urls[2].toString().endsWith("/test-classpath/WEB-INF/lib/slf4j-api-2.0.9.jar"));
    }

    @Test
    void testLoadFromClasses() throws Exception {
//        String cacheName = "com.ai.x.connector.HttpConnector";
//        Class<?> cacheClass = cl.loadClass(cacheName);
//        assertEquals(cacheName, cacheClass.getName());
////        assertSame(cl, cacheClass.getClassLoader());
//        @SuppressWarnings("unchecked")
//        Map<String, String> instance = (Map<String, String>) cacheClass.getConstructor().newInstance();
//        assertEquals(cacheName, instance.getClass().getName());
//
//
//        Class<?> servletClass = Class.forName("jakarta.servlet.http.HttpServlet");
//        assertNotSame(cl, servletClass.getClassLoader());
    }

    @Test
    void scanClassPath0() {
    }

    @Test
    void scanJar() {
    }

    @Test
    void scanJar0() {
    }

    @Test
    void createUrls() {
    }

    @Test
    void toDirURL() {
    }

    @Test
    void toJarURL() {
    }

    @Test
    void toAbsPath() {
    }
}