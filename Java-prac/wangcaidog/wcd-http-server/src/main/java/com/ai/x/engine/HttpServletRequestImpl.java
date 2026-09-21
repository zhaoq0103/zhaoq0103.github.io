package com.ai.x.engine;

import com.ai.x.Config;
import com.ai.x.connector.HttpExchangeRequest;
import com.ai.x.engine.support.Attributes;
import com.ai.x.engine.support.HttpHeaders;
import com.ai.x.engine.support.Parameters;
import com.ai.x.utils.HttpUtils;
import jakarta.servlet.*;
import jakarta.servlet.http.*;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.security.Principal;
import java.util.*;
import java.util.regex.Pattern;

public class HttpServletRequestImpl implements HttpServletRequest {
    final ServletContextImpl servletContext;
    HttpExchangeRequest adapter;
    final HttpServletResponse response;
    final HttpHeaders headers;
    final Parameters parameters;

    final String method;
    String characterEncoding;
    int contentLength = 0;

    String requestId = null;
    Attributes attributes = new Attributes();

    Boolean inputCalled = null;
    final Config config;
    public HttpServletRequestImpl(Config config, ServletContextImpl servletContext, HttpExchangeRequest exchangeRequest, HttpServletResponse response) {
        this.config = config;
        this.servletContext = servletContext;
        this.adapter = exchangeRequest;
        this.response = response;

        this.characterEncoding = config.server.requestEncoding;
        this.method = this.adapter.getRequestMethod();

        this.headers = new HttpHeaders(this.adapter.getRequestHeaders());
        this.parameters = new Parameters(this.adapter, this.characterEncoding);

        if ("POST".equals(this.method) || "PUT".equals(this.method) || "DELETE".equals(this.method) || "PATCH".equals(this.method)) {
            this.contentLength = getIntHeader("Content-Length");
        }
    }

    @Override
    public String getParameter(String name) {
        return this.parameters.getParameter(name);
    }

    @Override
    public String getAuthType() {
        return null;
    }

    @Override
    public Cookie[] getCookies() {
        String cookieValue = this.getHeader("Cookie");
        return HttpUtils.parseCookies(cookieValue);
    }

    @Override
    public long getDateHeader(String name) {
        return this.headers.getDateHeader(name);
    }

    @Override
    public String getHeader(String s) {
        return this.headers.getHeader(s);
    }

    @Override
    public Enumeration<String> getHeaders(String name) {
        List<String> hs = this.headers.getHeaders(name);
        if (hs == null) {
            return Collections.emptyEnumeration();
        }
        return Collections.enumeration(hs);
    }

    @Override
    public Enumeration<String> getHeaderNames() {
        return Collections.enumeration(this.headers.getHeaderNames());
    }

    @Override
    public int getIntHeader(String name) {
        return this.headers.getIntHeader(name);
    }

    @Override
    public HttpServletMapping getHttpServletMapping() {
        return HttpServletRequest.super.getHttpServletMapping();
    }

    @Override
    public String getMethod() {
        return this.method;
    }

    @Override
    public String getPathInfo() {
        return null;
    }

    @Override
    public String getPathTranslated() {
        return this.servletContext.getRealPath(getRequestURI());
    }

    @Override
    public PushBuilder newPushBuilder() {
        return HttpServletRequest.super.newPushBuilder();
    }

    @Override
    public String getContextPath() {
        return "";
    }

    @Override
    public String getQueryString() {
        return this.adapter.getRequestURI().getRawQuery();
    }

    @Override
    public String getRemoteUser() {
        return null;
    }

    @Override
    public boolean isUserInRole(String s) {
        return false;
    }

    @Override
    public Principal getUserPrincipal() {
        return null;
    }

    @Override
    public String getRequestedSessionId() {
        return null;
    }

    @Override
    public String getRequestURI() {
        return this.adapter.getRequestURI().getPath();
    }


    @Override
    public StringBuffer getRequestURL() {
        StringBuffer sb = new StringBuffer(128);
        sb.append(getScheme()).append("://").append(getServerName()).append(':').append(getServerPort()).append(getRequestURI());
        return sb;
    }

    @Override
    public String getServletPath() {
        return getRequestURI();
    }

    @Override
    public HttpSession getSession(boolean create) {
        String sessionId = null;
        Cookie[] cookies = getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("JSESSIONID".equals(cookie.getName())) {
                    sessionId = cookie.getValue();
                    break;
                }
            }
        }
        if (sessionId == null && !create) {
            return null;
        }
        if (sessionId == null) {
            if (this.response.isCommitted()) {
                throw new IllegalStateException("Cannot create session for response is commited.");
            }
            sessionId = UUID.randomUUID().toString();
            // set cookie:
            String cookieValue = "JSESSIONID=" + sessionId + "; Path=/; SameSite=Strict; HttpOnly";
            this.response.addHeader("Set-Cookie", cookieValue);
        }
        return this.servletContext.sessionManager.getSession(sessionId);
    }

    @Override
    public HttpSession getSession() {
        return getSession(true);
    }

    @Override
    public String changeSessionId() {
        throw new UnsupportedOperationException("changeSessionId() is not supported.");
    }

    @Override
    public boolean isRequestedSessionIdValid() {
        return false;
    }

    @Override
    public boolean isRequestedSessionIdFromCookie() {
        return true;
    }

    @Override
    public boolean isRequestedSessionIdFromURL() {
        return false;
    }

    @Override
    public boolean authenticate(HttpServletResponse httpServletResponse) throws IOException, ServletException {
        return false;
    }

    @Override
    public void login(String s, String s1) throws ServletException {

    }

    @Override
    public void logout() throws ServletException {

    }

    @Override
    public Collection<Part> getParts() throws IOException, ServletException {
        // not suport multipart:
        return List.of();
    }

    @Override
    public Part getPart(String s) throws IOException, ServletException {
        return null;
    }

    @Override
    public <T extends HttpUpgradeHandler> T upgrade(Class<T> aClass) throws IOException, ServletException {
        return null;
    }

    @Override
    public Map<String, String> getTrailerFields() {
        return HttpServletRequest.super.getTrailerFields();
    }

    @Override
    public boolean isTrailerFieldsReady() {
        return HttpServletRequest.super.isTrailerFieldsReady();
    }

    @Override
    public Object getAttribute(String name) {
        return this.attributes.getAttribute(name);
    }

    @Override
    public Enumeration<String> getAttributeNames() {
        return this.attributes.getAttributeNames();
    }

    @Override
    public String getCharacterEncoding() {
        return this.characterEncoding;
    }

    @Override
    public void setCharacterEncoding(String env) throws UnsupportedEncodingException {
        this.characterEncoding = env;
        this.parameters.setCharset(env);
    }

    @Override
    public int getContentLength() {
        return this.contentLength;
    }

    @Override
    public long getContentLengthLong() {
        return this.contentLength;
    }

    @Override
    public String getContentType() {
        return getHeader("Content-Type");
    }

    @Override
    public ServletInputStream getInputStream() throws IOException {
        if (this.inputCalled == null) {
            this.inputCalled = Boolean.TRUE;
            return new ServletInputStreamImpl(this.adapter.getRequestBody());
        }
        throw new IllegalStateException("Cannot reopen input stream after " + (this.inputCalled ? "getInputStream()" : "getReader()") + " was called.");
    }


    @Override
    public Enumeration<String> getParameterNames() {
        return this.parameters.getParameterNames();
    }

    @Override
    public String[] getParameterValues(String s) {
        return this.parameters.getParameterValues(s);
    }

    @Override
    public Map<String, String[]> getParameterMap() {
        return this.parameters.getParameterMap();
    }

    @Override
    public String getProtocol() {
        return "HTTP/1.1";
    }

    @Override
    public String getScheme() {
        String header = "http";
        String forwarded = this.config.server.forwardedHeaders.forwardedProto;
        if (!forwarded.isEmpty()) {
            String forwardedHeader = getHeader(forwarded);
            if (forwardedHeader != null) {
                header = forwardedHeader;
            }
        }
        return header;
    }

    @Override
    public String getServerName() {
        String header = getHeader("Host");
        String forwarded = config.server.forwardedHeaders.forwardedHost;
        if (!forwarded.isEmpty()) {
            String forwardedHeader = getHeader(forwarded);
            if (forwardedHeader != null) {
                header = forwardedHeader;
            }
        }
        if (header == null) {
            InetSocketAddress address = this.adapter.getLocalAddress();
            header = address.getHostString();
        }
        return header;
    }

    @Override
    public int getServerPort() {
        InetSocketAddress address = this.adapter.getLocalAddress();
        return address.getPort();
    }

    @Override
    public BufferedReader getReader() throws IOException {
        if (this.inputCalled == null) {
            this.inputCalled = Boolean.FALSE;
            return new BufferedReader(new InputStreamReader(new ByteArrayInputStream(this.adapter.getRequestBody()), StandardCharsets.UTF_8));
        }
        throw new IllegalStateException("Cannot reopen input stream after " + (this.inputCalled ? "getInputStream()" : "getReader()") + " was called.");
    }


    // address and port ///////////////////////////////////////////////////////
    @Override
    public String getRemoteAddr() {
        String addr = null;
        String forwarded = this.config.server.forwardedHeaders.forwardedFor;
        if (forwarded != null && !forwarded.isEmpty()) {
            String forwardedHeader = getHeader(forwarded);
            if (forwardedHeader != null) {
                int n = forwardedHeader.indexOf(',');
                addr = n < 0 ? forwardedHeader : forwardedHeader.substring(n);
            }
        }
        if (addr == null) {
            InetSocketAddress address = this.adapter.getRemoteAddress();
            addr = address.getHostString();
        }
        return addr;
    }

    @Override
    public String getRemoteHost() {
        // avoid DNS lookup by IP:
        return getRemoteAddr();
    }

    @Override
    public void setAttribute(String name, Object value) {
        if (value == null) {
            removeAttribute(name);
        } else {
            Object oldValue = this.attributes.setAttribute(name, value);
            if (oldValue == null) {
                this.servletContext.invokeServletRequestAttributeAdded(this, name, value);
            } else {
                this.servletContext.invokeServletRequestAttributeReplaced(this, name, value);
            }
        }
    }

    @Override
    public void removeAttribute(String name) {
        Object oldValue = this.attributes.removeAttribute(name);
        this.servletContext.invokeServletRequestAttributeRemoved(this, name, oldValue);
    }

    @Override
    public Locale getLocale() {
        String langs = getHeader("Accept-Language");
        if (langs == null) {
            return HttpUtils.DEFAULT_LOCALE;
        }
        return HttpUtils.parseLocales(langs).get(0);
    }

    @Override
    public Enumeration<Locale> getLocales() {
        String langs = getHeader("Accept-Language");
        if (langs == null) {
            return Collections.enumeration(HttpUtils.DEFAULT_LOCALES);
        }
        return Collections.enumeration(HttpUtils.parseLocales(langs));
    }

    @Override
    public boolean isSecure() {
        return "https".equals(getScheme().toLowerCase());
    }

    @Override
    public RequestDispatcher getRequestDispatcher(String s) {
        // do not support request dispatcher:
        return null;
    }

    @Override
    public int getRemotePort() {
        InetSocketAddress address = this.adapter.getRemoteAddress();
        return address.getPort();
    }

    @Override
    public String getLocalName() {
        // avoid DNS lookup:
        return getLocalAddr();
    }

    @Override
    public String getLocalAddr() {
        InetSocketAddress address = this.adapter.getLocalAddress();
        return address.getHostString();
    }

    @Override
    public int getLocalPort() {
        InetSocketAddress address = this.adapter.getLocalAddress();
        return address.getPort();
    }

    @Override
    public ServletContext getServletContext() {
        return this.servletContext;
    }

    @Override
    public AsyncContext startAsync() throws IllegalStateException {
        throw new IllegalStateException("Async is not supported.");
    }

    @Override
    public AsyncContext startAsync(ServletRequest servletRequest, ServletResponse servletResponse) throws IllegalStateException {
        throw new IllegalStateException("Async is not supported.");
    }

    @Override
    public boolean isAsyncStarted() {
        return false;
    }

    @Override
    public boolean isAsyncSupported() {
        return false;
    }

    @Override
    public AsyncContext getAsyncContext() {
        throw new IllegalStateException("Async is not supported.");
    }

    @Override
    public DispatcherType getDispatcherType() {
        return DispatcherType.REQUEST;
    }

    @Override
    public String getRequestId() {
        if (this.requestId == null) {
            this.requestId = UUID.randomUUID().toString();
        }
        return this.requestId;
    }

    @Override
    public String getProtocolRequestId() {
        // empty string for http 1.x:
        return "";
    }

    @Override
    public ServletConnection getServletConnection() {
        throw new UnsupportedOperationException("getServletConnection");
    }

    @Override
    public String toString() {
        return String.format("HttpServletRequestImpl@%s[%s:%s]", Integer.toHexString(hashCode()), getMethod(), getRequestURI());
    }
}
