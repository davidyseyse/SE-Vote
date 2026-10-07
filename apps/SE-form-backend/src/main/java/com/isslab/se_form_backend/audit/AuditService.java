package com.isslab.se_form_backend.audit;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;

@Slf4j
public class AuditService {
    /**
     * 異步記錄用戶訪問
     */
    @Async("auditExecutor")
    public void recordAccess(AuditContext context) {
        try {
            // 結構化日誌記錄
            if (context.hasError()) {
                log.warn("用戶訪問異常 - 用戶: {}, IP: {}, 路徑: {} {}, 狀態: {}, 處理時間: {}ms, 錯誤: {}",
                        context.getUsername(),
                        context.getIpAddress(),
                        context.getMethod(),
                        context.getFullUri(),
                        context.getResponseStatus(),
                        context.getProcessingTimeMs(),
                        context.getException() != null ? context.getException().getMessage() : "HTTP錯誤");
            } else {
                log.info("用戶訪問記錄 - 用戶: {}, IP: {}, 路徑: {} {}, 狀態: {}, 處理時間: {}ms",
                        context.getUsername(),
                        context.getIpAddress(),
                        context.getMethod(),
                        context.getFullUri(),
                        context.getResponseStatus(),
                        context.getProcessingTimeMs());
            }

            // 只記錄重要的訪問到資料庫（避免資料庫過載）
            if (shouldPersist(context)) {
//                persistAuditLog(context);
            }

        } catch (Exception e) {
            log.error("記錄訪問日誌失敗: {}", e.getMessage(), e);
        }
    }

    /**
     * 判斷是否需要持久化到資料庫
     */
    private boolean shouldPersist(AuditContext context) {
        // 只記錄以下情況：
        // 1. 錯誤請求
        // 2. 敏感操作 (POST, PUT, DELETE)
        // 3. 登入/登出相關
        return context.hasError()
                || !context.getMethod().equals("GET")
                || context.getUri().contains("/auth/");
    }


    /**
     * 記錄安全事件（登入失敗、權限不足等）
     */
    @Async("auditExecutor")
    public void recordSecurityEvent(String username, String event, String details, String ipAddress) {
        log.warn("安全事件 - 用戶: {}, 事件: {}, 詳情: {}, IP: {}", username, event, details, ipAddress);
    }
}
