package com.isslab.se_form_backend.controller;

import com.isslab.se_form_backend.service.impl.ExportService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping("/api/export")
@PreAuthorize("hasRole('ADMIN')")
public class ExportController {

    private final ExportService exportService;

    public ExportController(ExportService exportService) {
        this.exportService = exportService;
    }

    @GetMapping("/presenter/grades/{week}")
    public ResponseEntity<?> exportPresenterGradesByWeek(@PathVariable String week) throws IOException {
        exportService.exportPresenterGradesByWeek(week);

        return ResponseEntity.ok("報告者成績檔已匯出");
    }

    @GetMapping("/reviewer/grades/{week}")
    public ResponseEntity<?> exportReviewerGradesByWeek(@PathVariable String week) throws IOException {
        exportService.exportReviewerGradesByWeek(week);

        return ResponseEntity.ok("評分者成績檔已匯出");
    }

    @GetMapping("/form-submissions/{week}")
    public ResponseEntity<?> exportFormSubmissionsByWeek(@PathVariable String week) throws IOException {
        exportService.exportFormSubmissionsByWeek(week);

        return ResponseEntity.ok("表單提交紀錄已匯出");
    }

    @GetMapping("/score-records/{week}")
    public ResponseEntity<?> exportScoreRecordsByWeek(@PathVariable String week) throws IOException {
        exportService.exportScoreRecordsByWeek(week);

        return ResponseEntity.ok("評分紀錄已匯出");
    }
}
