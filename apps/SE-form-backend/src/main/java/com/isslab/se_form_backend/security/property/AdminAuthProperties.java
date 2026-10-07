package com.isslab.se_form_backend.security.property;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@EnableConfigurationProperties(AdminAuthProperties.class)
@ConfigurationProperties(prefix = "admin")
@Getter
@Setter
public class AdminAuthProperties {
    private String username;
    private String password;
}
