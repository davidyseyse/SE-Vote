package com.isslab.se_form_backend.config;

import com.isslab.se_form_backend.audit.AuditInterceptor;
import com.isslab.se_form_backend.audit.AuditService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.filter.CommonsRequestLoggingFilter;

@Configuration
public class AuditConfig {
    @Bean
    AuditService auditService(){
        return new AuditService();
    }

    @Bean
    AuditInterceptor auditInterceptor(AuditService auditService){
        return new AuditInterceptor(auditService);
    }
}
