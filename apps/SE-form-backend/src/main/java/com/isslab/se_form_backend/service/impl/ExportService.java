package com.isslab.se_form_backend.service.impl;

import com.isslab.se_form_backend.helper.CsvWriter;
import com.isslab.se_form_backend.model.*;
import com.isslab.se_form_backend.service.AbstractStudentRoleService;
import org.springframework.beans.factory.annotation.Value;

import java.io.IOException;
import java.util.List;

public class ExportService {

    private final PresenterService presenterService;
    private final ReviewerService reviewerService;
    private final FormProcessingService formProcessingService;

    @Value("${csv.output-path.grades}")
    private String GRADE_OUT_PATH;
    @Value("${csv.output-path.forms}")
    private String FORM_OUT_PATH;
    @Value("${csv.output-path.records}")
    private String RECORDS_OUT_PATH;

    public ExportService(PresenterService presenterService, ReviewerService reviewerService, FormProcessingService formProcessingService) {
        this.presenterService = presenterService;
        this.reviewerService = reviewerService;
        this.formProcessingService = formProcessingService;
    }

    public void exportPresenterGradesByWeek(String week) throws IOException {
        List<PresenterGrade> presenterGrades = presenterService.getPresenterGradesByWeek(week);
        CsvWriter.writePresenterGrades(presenterGrades, GRADE_OUT_PATH, week);
    }

    public void exportReviewerGradesByWeek(String week) throws IOException {
        List<ReviewerGrade> reviewerGrades = reviewerService.getReviewerGradesByWeek(week);
        CsvWriter.writeReviewerGrades(reviewerGrades, GRADE_OUT_PATH, week);
    }

    public void exportFormSubmissionsByWeek(String week) throws IOException {
        List<FormSubmission> submissions = formProcessingService.getAllSubmissionsByWeek(week);
        CsvWriter.writeFormSubmissions(submissions, FORM_OUT_PATH, week);
    }

    public void exportScoreRecordsByWeek(String week) throws IOException {
        List<FormSubmission> scoreRecords = formProcessingService.getAllScoreRecordsByWeek(week);
        CsvWriter.writeScoreRecords(scoreRecords, RECORDS_OUT_PATH, week);
    }
}
