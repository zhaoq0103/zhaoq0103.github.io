package com.ai.x.engine;

import com.ai.x.Config;
import com.ai.x.connector.HttpExchangeAdapter;
import com.ai.x.connector.HttpExchangeResponse;
import com.ai.x.engine.support.HttpHeaders;
import com.sun.net.httpserver.Headers;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class HttpServletResponseImpl implements HttpServletResponse {

    HttpExchangeResponse adapter;

    int status = 200;
    String contentType;

    final HttpHeaders headers;

    int bufferSize = 1024*100;
    Boolean callOutput = null;
    ServletOutputStream output;
    PrintWriter writer;
    final Config config;
    String characterEncoding;
    long contentLength = 0;
    List<Cookie> cookies = null;
    boolean committed = false;
    Locale locale = null;

    public HttpServletResponseImpl(Config config, HttpExchangeResponse exchangeResponse) {
        this.config = config;
        this.adapter = exchangeResponse;
        this.headers = new HttpHeaders(exchangeResponse.getResponseHeaders());
        this.characterEncoding = config.server.responseEncoding;
        this.setContentType("text/html");
    }

    @Override
    public PrintWriter getWriter() throws IOException {
        if (callOutput == null) {
            commitHeaders(0);
            this.writer = new PrintWriter(this.adapter.getResponseBody(), true, StandardCharsets.UTF_8);
            this.callOutput = Boolean.FALSE;
            return this.writer;
        }
        if (!callOutput.booleanValue()) {
            return this.writer;
        }
        throw new IllegalStateException("Cannot open writer when output stream is opened.");
    }

    @Override
    public void setContentType(String type) {
        this.contentType = type;
        if (type.startsWith("text/")) {
            setHeader("Content-Type", contentType + "; charset=" + this.characterEncoding);
        } else {
            setHeader("Content-Type", contentType);
        }
    }

    @Override
    public void setHeader(String name, String value) {
        checkNotCommitted();
        this.headers.setHeader(name, value);
    }

    @Override
    public void addCookie(Cookie cookie) {
        checkNotCommitted();
        if (this.cookies == null) {
            this.cookies = new ArrayList<>();
        }
        this.cookies.add(cookie);
    }

    @Override
    public boolean containsHeader(String s) {
        return this.headers.containsHeader(s);
    }

    @Override
    public String encodeURL(String s) {
        return s;
    }

    @Override
    public String encodeRedirectURL(String s) {
        return s;
    }

    @Override
    public void sendError(int sc, String msg) throws IOException {
        checkNotCommitted();
        this.status = sc;
        commitHeaders(-1);
    }

    @Override
    public void sendError(int sc) throws IOException {
        sendError(sc, "Error");
    }

    @Override
    public void sendRedirect(String location) throws IOException {
        checkNotCommitted();
        this.status = 302;
        this.headers.setHeader("Location", location);
        commitHeaders(-1);
    }

    void checkNotCommitted() {
        if (this.committed) {
            throw new IllegalStateException("Response is committed.");
        }
    }

    public void cleanup() throws IOException {
        // 这里如果不提交commitHeaders(-1) 客户端就一直在等待服务器响应
        if (!this.committed) {
            commitHeaders(-1);
        }
        if (this.callOutput != null) {
            if (this.callOutput.booleanValue()) {
                this.output.close();
            } else {
                this.writer.close();
            }
        }
    }

    void commitHeaders(long length) throws IOException {
        this.adapter.sendResponseHeaders(this.status, length);
        this.committed = true;
    }

    @Override
    public void setDateHeader(String s, long l) {
        checkNotCommitted();
        this.headers.setDateHeader(s, l);
    }

    @Override
    public void addDateHeader(String s, long l) {
        checkNotCommitted();
        this.headers.addDateHeader(s, l);
    }

    @Override
    public void addHeader(String name, String value) {
        checkNotCommitted();
        this.headers.addHeader(name, value);
    }

    @Override
    public void setIntHeader(String s, int i) {
        checkNotCommitted();
        this.headers.setIntHeader(s, i);
    }

    @Override
    public void addIntHeader(String s, int i) {
        checkNotCommitted();
        this.headers.addIntHeader(s, i);
    }

    @Override
    public void setStatus(int i) {
        checkNotCommitted();
        this.status = i;
    }

    @Override
    public int getStatus() {
        return this.status;
    }

    @Override
    public String getHeader(String s) {
        return this.headers.getHeader(s);
    }

    @Override
    public Collection<String> getHeaders(String name) {
        List<String> hs = this.headers.getHeaders(name);
        if (hs == null) {
            return List.of();
        }
        return hs;
    }

    @Override
    public Collection<String> getHeaderNames() {
        return Collections.unmodifiableSet(this.headers.getHeaderNames());
    }

    @Override
    public String getCharacterEncoding() {
        return this.characterEncoding;
    }

    @Override
    public String getContentType() {
        return this.contentType;
    }

    @Override
    public ServletOutputStream getOutputStream() throws IOException {
        if (callOutput == null) {
            commitHeaders(0);
            this.output = new ServletOutputStreamImpl(this.adapter.getResponseBody());
            this.callOutput = Boolean.TRUE;
            return this.output;
        }
        if (callOutput.booleanValue()) {
            return this.output;
        }
        throw new IllegalStateException("Cannot open output stream when writer is opened.");
    }

    @Override
    public void setCharacterEncoding(String charset) {
        this.characterEncoding = charset;
    }

    @Override
    public void setContentLength(int i) {
        this.contentLength = i;
    }

    @Override
    public void setContentLengthLong(long l) {
        this.contentLength = l;
    }

    @Override
    public void setBufferSize(int size) {
        if (this.callOutput != null) {
            throw new IllegalStateException("Output stream or writer is opened.");
        }
        if (size < 0) {
            throw new IllegalArgumentException("Invalid size: " + size);
        }
        this.bufferSize = size;
    }

    @Override
    public int getBufferSize() {
        return this.bufferSize;
    }

    @Override
    public void flushBuffer() throws IOException {
        if (this.callOutput == null) {
            throw new IllegalStateException("Output stream or writer is not opened.");
        }
        if (this.callOutput.booleanValue()) {
            this.output.flush();
        } else {
            this.writer.flush();
        }
    }

    @Override
    public void resetBuffer() {
        checkNotCommitted();
    }

    @Override
    public boolean isCommitted() {
        return this.committed;
    }

    @Override
    public void reset() {
        checkNotCommitted();
        this.status = 200;
        this.headers.clearHeaders();
    }

    @Override
    public void setLocale(Locale locale) {
        checkNotCommitted();
       this.locale = locale;
    }

    @Override
    public Locale getLocale() {
        return this.locale == null ? Locale.getDefault() : this.locale;
    }
}
