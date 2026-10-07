package com.isslab.se_form_backend.config;

import com.isslab.se_form_backend.controller.AuthController;
import com.isslab.se_form_backend.repository.StudentRepository;
import com.isslab.se_form_backend.security.JwtUtil;
import com.isslab.se_form_backend.security.filter.JwtAuthFilter;
import com.isslab.se_form_backend.security.property.AdminAuthProperties;
import com.isslab.se_form_backend.security.service.StudentUserDetailsService;
import com.isslab.se_form_backend.service.AbstractStudentService;
import com.isslab.se_form_backend.service.impl.StudentService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
public class SecurityConfig {

    @Value("${jwt.secretKey}")
    private String secretKey;

    @Bean
    public JwtUtil jwtUtil(){
        return new JwtUtil(secretKey);
    }

    @Bean
    public JwtAuthFilter jwtAuthFilter(JwtUtil jwtUtil){
        return new JwtAuthFilter(jwtUtil);
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http, JwtAuthFilter jwtAuthFilter) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                .exceptionHandling(ex -> ex
                        .authenticationEntryPoint((req, res, e) -> res.sendError(HttpServletResponse.SC_UNAUTHORIZED))
                        .accessDeniedHandler((req, res, e) -> res.sendError(HttpServletResponse.SC_FORBIDDEN))
                )

                //許前端資源訪問
                .authorizeHttpRequests(auth -> auth
                        // 靜態資源（前端文件）- 完全開放
                        .requestMatchers(
                                "/",                    // 根路徑
                                "/index.html",          // 主頁面
                                "/static/**",           // 靜態資源
                                "/assets/**",           // Vite 構建的資源
                                "/*.js",               // JS 文件
                                "/*.css",              // CSS 文件
                                "/*.ico",              // 圖標
                                "/*.png", "/*.jpg", "/*.jpeg", "/*.gif", "/*.svg", // 圖片
                                "/*.woff", "/*.woff2", "/*.ttf", "/*.eot"          // 字體
                        ).permitAll()

                        // SPA 路由 - 開放（讓前端處理路由）
                        .requestMatchers(
                                "/login",
                                "/dashboard",
                                "/users",
                                "/users/**",
                                "/profile",
                                "/profile/**",
                                "/forms",
                                "/forms/**"
                        ).permitAll()

                        // API 端點
                        .requestMatchers("/api/auth/login").permitAll()    // 登入 API
                        .requestMatchers("/api/health").permitAll()        // 健康檢查
                        .requestMatchers("/api/test").permitAll()          // 測試 API（可選）
                        .requestMatchers("/api/info/email/ta").permitAll()          // 取得助教信箱
                        .requestMatchers("/api/info/week/current").permitAll()      // 取得目前週次

                        // 其他 API 需要認證
                        .requestMatchers("/api/**").authenticated()

                        // 其他所有請求都允許（給前端路由處理）
                        .anyRequest().permitAll()

                )

                // CORS 配置
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))

                // JWT 過濾器
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();

        // 允許的來源
        configuration.setAllowedOrigins(Arrays.asList(
                "http://localhost:3000",    // Vite 開發服務器
                "http://localhost:4173",    // Vite 預覽服務器 1
                "http://localhost:5173",    // Vite 預覽服務器 2
                "http://localhost:8080"     // 生產環境（同域名）
        ));

        // 允許的方法
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));

        // 允許的標頭
        configuration.setAllowedHeaders(Arrays.asList("*"));

        // 允許憑證
        configuration.setAllowCredentials(true);

        // 應用到所有路徑
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);

        return source;
    }

    @Bean
    public StudentUserDetailsService studentUserDetailsService(StudentRepository studentRepository){
        return new StudentUserDetailsService(studentRepository);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http,PasswordEncoder passwordEncoder, StudentUserDetailsService userDetailsService)
            throws Exception {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailsService);
        provider.setPasswordEncoder(passwordEncoder);

        AuthenticationManagerBuilder builder =
                http.getSharedObject(AuthenticationManagerBuilder.class);
        builder.authenticationProvider(provider);

        return builder.build();
    }

    @Bean
    public AdminAuthProperties adminAuthProperties() {
        return new AdminAuthProperties();
    }

    @Bean
    public AuthController authController(AuthenticationManager authenticationManager, JwtUtil jwtUtil, AdminAuthProperties adminAuthProperties, AbstractStudentService studentService) {
        return new AuthController(authenticationManager, jwtUtil, adminAuthProperties, studentService);
    }
}
