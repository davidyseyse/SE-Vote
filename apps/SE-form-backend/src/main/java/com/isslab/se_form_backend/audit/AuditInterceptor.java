package com.isslab.se_form_backend.audit;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.servlet.HandlerInterceptor;

@Slf4j
public class AuditInterceptor implements HandlerInterceptor {
    private final AuditService auditService;

    public AuditInterceptor(AuditService auditService){
        this.auditService = auditService;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response,
                             Object handler) throws Exception {

        // 記錄請求開始時間
        request.setAttribute("startTime", System.currentTimeMillis());

        // 記錄所有請求，不論是否已認證
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        // 決定用戶名稱
        String username;
        if (auth != null && auth.isAuthenticated() && !"anonymousUser".equals(auth.getName())) {
            username = auth.getName();
        } else {
            username = "anonymous";  // 匿名用戶
        }

        AuditContext context = AuditContext.builder()
                .username(username)
                .authorities(auth != null ? auth.getAuthorities() : null)
                .ipAddress(getClientIP(request))
                .userAgent(request.getHeader("User-Agent"))
                .method(request.getMethod())
                .uri(request.getRequestURI())
                .queryString(request.getQueryString())
                .sessionId(request.getSession(false) != null ? request.getSession().getId() : null)
                .build();

        // 添加除錯日誌
        log.info("🔍 請求記錄 - 用戶: {}, 路徑: {} {}", username, request.getMethod(), request.getRequestURI());

        // 將 context 存入 request attribute 供後續使用
        request.setAttribute("auditContext", context);

        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response,
                                Object handler, Exception ex) throws Exception {

        AuditContext context = (AuditContext) request.getAttribute("auditContext");
        if (context != null) {
            Long startTime = (Long) request.getAttribute("startTime");
            long processingTime = startTime != null ? System.currentTimeMillis() - startTime : 0;

            context.setResponseStatus(response.getStatus());
            context.setProcessingTimeMs(processingTime);
            context.setException(ex);

            log.info("📊 請求完成 - 用戶: {}, 狀態: {}, 耗時: {}ms",
                    context.getUsername(), response.getStatus(), processingTime);

            // 異步記錄，不影響響應時間
            auditService.recordAccess(context);
        }
    }

    private String getClientIP(HttpServletRequest request) {
        String xForwardedFor = request.getHeader("X-Forwarded-For");
        if (xForwardedFor != null && !xForwardedFor.isEmpty()) {
            return xForwardedFor.split(",")[0].trim();
        }

        String xRealIP = request.getHeader("X-Real-IP");
        if (xRealIP != null && !xRealIP.isEmpty()) {
            return xRealIP;
        }

        return request.getRemoteAddr();
    }
}