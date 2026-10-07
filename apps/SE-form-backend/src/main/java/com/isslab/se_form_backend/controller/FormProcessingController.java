package com.isslab.se_form_backend.controller;

import com.isslab.se_form_backend.helper.ResponseStatus;
import com.isslab.se_form_backend.model.AbsentStudentsResponse;
import com.isslab.se_form_backend.model.FormSubmission;
import com.isslab.se_form_backend.model.Student;
import com.isslab.se_form_backend.service.impl.FormProcessingService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@Slf4j
@RestController
@RequestMapping("/api/form-processing")
public class FormProcessingController {

    private final FormProcessingService formProcessingService;

    public FormProcessingController(FormProcessingService formProcessingService) {
        this.formProcessingService = formProcessingService;
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/participant/{week}")
    public ResponseEntity<?> getParticipant(@PathVariable String week) {
        Set<Student> absentStudents = formProcessingService.checkParticipant(week);
        String msg = "本周有未參加評分的同學，已完成紀錄";

        if(absentStudents.isEmpty()) {
            msg = "本周所有同學都有參加評分活動";
        }

        return ResponseEntity.ok(new AbsentStudentsResponse(msg, absentStudents));
    }

    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @PostMapping("/")
    public ResponseEntity<?> submit(@RequestBody FormSubmission formSubmission,
                                    //此 authentication 是由 JwtAuthFilter 處理後的資訊
                                    Authentication authentication) {
        String username = authentication.getName();
        log.info("username: {}", username);
        formSubmission.setSubmitterId(username);

        formProcessingService.process(formSubmission);

        return ResponseEntity.ok(ResponseStatus.statusOk());
    }
}
