package com.isslab.se_form_backend.audit;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;

import java.time.LocalDateTime;
import java.util.Collection;

@Getter
@Setter
@Builder
public class AuditContext {
    private String username;
    private Collection<? extends GrantedAuthority> authorities;
    private String ipAddress;
    private String userAgent;
    private String method;
    private String uri;
    private String queryString;
    private String sessionId;
    private Integer responseStatus;
    private Long processingTimeMs;
    private Exception exception;

    @Builder.Default
    private LocalDateTime timestamp = LocalDateTime.now();

    // 便利方法
    public String getRolesAsString() {
        return authorities != null ? authorities.toString() : "none";
    }

    public String getFullUri() {
        return queryString != null ? uri + "?" + queryString : uri;
    }

    public boolean isSuccessful() {
        return responseStatus != null && responseStatus >= 200 && responseStatus < 300;
    }

    public boolean hasError() {
        return exception != null || (responseStatus != null && responseStatus >= 400);
    }
}