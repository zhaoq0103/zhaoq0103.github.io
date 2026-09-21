package com.ai.x.engine;

import com.ai.x.utils.DateUtils;
import jakarta.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class SessionManager implements Runnable{

    final Logger logger = LoggerFactory.getLogger(getClass());
    final ServletContextImpl servletContext;
    final Map<String, HttpSessionImpl> sessions = new ConcurrentHashMap<>();
    final int inactiveInterval;

    public SessionManager(ServletContextImpl servletContext, int inactiveInterval) {
        this.servletContext = servletContext;
        this.inactiveInterval = inactiveInterval;

        Thread t = new Thread(this, "Session-Cleanup-Thread");
        t.setDaemon(true);
        t.start();
    }

    public void remove(HttpSession session) {
        this.sessions.remove(session.getId());
        this.servletContext.invokeHttpSessionDestroyed(session);
    }

    public HttpSession getSession(String sessionId) {
        HttpSessionImpl session = this.sessions.get(sessionId);
        if (session == null) {
            session = new HttpSessionImpl(this.servletContext, sessionId, inactiveInterval);
            this.sessions.put(sessionId, session);
            this.servletContext.invokeHttpSessionCreated(session);
        } else {
            session.lastAccessedTime = System.currentTimeMillis();
        }
        return session;
    }

    @Override
    public void run() {
        for (;;) {
            try {
                Thread.sleep(60_000L);
            } catch (InterruptedException e) {
                break;
            }
            long now = System.currentTimeMillis();
            for (String sessionId : this.sessions.keySet()) {
                HttpSession session = this.sessions.get(sessionId);
                if (session.getLastAccessedTime() + session.getMaxInactiveInterval() * 1000L < now) {
                    logger.warn("remove expired session: {}, last access time: {}", sessionId, DateUtils.formatDateTimeGMT(session.getLastAccessedTime()));
                    session.invalidate();
                }
            }
        }
    }
}
