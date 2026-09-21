package com.ai.x.connector;

import com.ai.x.Config;
import com.ai.x.engine.HttpServletRequestImpl;
import com.ai.x.engine.HttpServletResponseImpl;
import com.ai.x.engine.ServletContextImpl;
import com.ai.x.utils.DateUtils;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.time.Duration;
import java.util.List;
import java.util.concurrent.Executor;


public class HttpConnector implements HttpHandler, AutoCloseable {
    final Logger logger = LoggerFactory.getLogger(getClass());
    final HttpServer httpServer;
    final ServletContextImpl servletContext;
    final Duration stopDelay = Duration.ofSeconds(5);
    final Config config;
    final ClassLoader classLoader;

    public HttpConnector(Config config, String webRoot, Executor executor, ClassLoader classLoader, List<Class<?>> autoScannedClasses)  throws IOException {
        logger.info("starting wcdog http server at {}:{}...", config.server.host, config.server.port);
        this.config = config;
        this.classLoader = classLoader;

        // init servlet context:
        Thread.currentThread().setContextClassLoader(this.classLoader);
        ServletContextImpl ctx = new ServletContextImpl(classLoader, config, webRoot);
        ctx.initialize(autoScannedClasses);
        this.servletContext = ctx;
        Thread.currentThread().setContextClassLoader(null);

        // start http server:
        this.httpServer = HttpServer.create(new InetSocketAddress(config.server.host, config.server.port), config.server.backlog, "/", this);
        this.httpServer.setExecutor(executor);
        this.httpServer.start();
        logger.info("wcdog http server started at {}:{}...", config.server.host, config.server.port);
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        logger.info("handle begin at {} : {}: {}?{}", DateUtils.formatDateTimeGMT(System.currentTimeMillis()), exchange.getRequestMethod(), exchange.getRequestURI().getPath(), exchange.getRequestURI().getRawQuery());
        var adapter = new HttpExchangeAdapter(exchange);
        var response = new HttpServletResponseImpl(this.config, adapter);
        var request = new HttpServletRequestImpl(this.config, this.servletContext, adapter, response);
        // process:
        try {
            Thread.currentThread().setContextClassLoader(this.classLoader);
            this.servletContext.process(request, response);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }finally {
            //服务器没有返回，调试了一天多，发现是 responseheader 没有提交
            response.cleanup();// 这里如果不提交commitHeaders(-1) 客户端就一直在等待服务器响应
            Thread.currentThread().setContextClassLoader(null);
            logger.info("handle finished at {}: {}: {}?{}",DateUtils.formatDateTimeGMT(System.currentTimeMillis()) ,exchange.getRequestMethod(), exchange.getRequestURI().getPath(), exchange.getRequestURI().getRawQuery());
        }
    }

    @Override
    public void close() throws Exception {
        this.servletContext.destroy();
        this.httpServer.stop((int) this.stopDelay.toSeconds());
    }

}
