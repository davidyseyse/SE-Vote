package com.isslab.se_form_backend.controller;

import com.isslab.se_form_backend.entity.FormScoreRecordEntity;
import com.isslab.se_form_backend.helper.ResponseStatus;
import com.isslab.se_form_backend.model.FormScoreRecord;
import com.isslab.se_form_backend.model.Status;
import com.isslab.se_form_backend.service.AbstractFormScoreRecordService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/form-score")
@PreAuthorize("hasRole('ADMIN')")
public class FormScoreRecordController {

    private final AbstractFormScoreRecordService formScoreRecordService;

    public FormScoreRecordController(AbstractFormScoreRecordService formScoreRecordService) {
        this.formScoreRecordService = formScoreRecordService;
    }

    @GetMapping("/")
    public ResponseEntity<List<FormScoreRecord>> getAllFormScoreRecords() {
        return ResponseEntity.ok(formScoreRecordService.getAll());
    }

    @GetMapping("{id}")
    public ResponseEntity<FormScoreRecord> getFormScoreRecordById(@PathVariable Long id) {
        return ResponseEntity.ok(formScoreRecordService.getById(id));
    }

    @PutMapping("/")
    public ResponseEntity<?> updateFormScoreRecordById(@RequestBody FormScoreRecordEntity newFormScoreRecord) {
        Long id = newFormScoreRecord.getId();
        formScoreRecordService.update(id, newFormScoreRecord);
        return ResponseEntity.ok(ResponseStatus.statusOk());
    }

    @DeleteMapping("/")
    public ResponseEntity<Status> deleteFormScoreRecordById(@RequestBody Map<String, Object> payload) {
        String idString = (String) payload.get("id");
        Long id = Long.parseLong(idString);
        formScoreRecordService.delete(id);
        return ResponseEntity.ok(ResponseStatus.statusOk());
    }
}
