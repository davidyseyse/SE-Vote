package com.isslab.se_form_backend.controller;

import com.isslab.se_form_backend.model.Email;
import com.isslab.se_form_backend.service.impl.PresenterService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/info")
public class InformationController {

    private final PresenterService presenterService;

    public InformationController(PresenterService presenterService) {
        this.presenterService = presenterService;
    }

    @Value("${app.frontEnd.taEmail}")
    private String TA_EMAIL;

    @GetMapping("/email/ta")
    public ResponseEntity<Email> getTAEmail() {
        Email taEmail = Email.builder().email(TA_EMAIL).build();
        return ResponseEntity.ok(taEmail);
    }

    @GetMapping("/week/current")
    public ResponseEntity<Map<String, String>> getCurrentWeek() {
        return ResponseEntity.ok(Map.of("week", presenterService.getCurrentWeek()));
    }
}
