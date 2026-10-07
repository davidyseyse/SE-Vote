package com.isslab.se_form_backend.service.impl;

import com.isslab.se_form_backend.entity.FormScoreRecordEntity;
import com.isslab.se_form_backend.entity.FormSubmissionEntity;
import com.isslab.se_form_backend.entity.PresenterGradeEntity;
import com.isslab.se_form_backend.model.*;
import com.isslab.se_form_backend.service.AbstractFormScoreRecordService;
import com.isslab.se_form_backend.service.AbstractFormSubmissionService;
import com.isslab.se_form_backend.service.AbstractStudentService;
import lombok.extern.slf4j.Slf4j;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
public class FormProcessingService {

    private final AbstractFormSubmissionService formSubmissionService;
    private final AbstractFormScoreRecordService formScoreRecordService;
    private final AbstractStudentService studentService;

    public FormProcessingService(AbstractFormSubmissionService formSubmissionService, AbstractFormScoreRecordService formScoreRecordService, AbstractStudentService studentService) {
        this.formSubmissionService = formSubmissionService;
        this.formScoreRecordService = formScoreRecordService;
        this.studentService = studentService;
    }

    public List<FormScoreRecordEntity> loadFormScoreRecordsByWeekAndPresenter(String week, String presenterId) {
        List<FormSubmissionEntity> formSubmissions = formSubmissionService.findAllSubmissionsByWeek(week);
        List<FormScoreRecordEntity> formScoreRecords = new ArrayList<>();

        for(FormSubmissionEntity formSubmission : formSubmissions) {
            Long formId = formSubmission.getId();
            FormScoreRecordEntity record = formScoreRecordService.findByFormIdAndPresenterId(formId, presenterId);
            formScoreRecords.add(record);
        }

        return formScoreRecords;
    }

    public Map<String, List<FormScoreRecordEntity>> loadFormScoreRecordsByWeek(String week) {
        List<FormSubmissionEntity> submissions = formSubmissionService.findAllSubmissionsByWeek(week);

        List<Long> formIds = submissions.stream()
                .map(FormSubmissionEntity::getId)
                .collect(Collectors.toList());

        if (formIds.isEmpty()) {
            return new HashMap<>(); // 該週沒資料就回空
        }

        List<FormScoreRecordEntity> allRecords = formScoreRecordService.findByFormIdIn(formIds);

        return allRecords.stream()
                .collect(Collectors.groupingBy(FormScoreRecordEntity::getPresenterId));
    }


    public void process(FormSubmission formSubmission) {
        log.info(formSubmission.toString());
        saveFormSubmission(formSubmission);

        String submitterId = formSubmission.getSubmitterId();
        String week = formSubmission.getWeek();
        Long formId = formSubmissionService.getFormId(submitterId, week);

        saveFormScoreRecords(formSubmission, formId);
    }

    public List<FormSubmission> getAllSubmissionsByWeek(String week) {
        List<FormSubmissionEntity> submissions = formSubmissionService.findAllSubmissionsByWeek(week);
        List<FormSubmission> formSubmissions = new ArrayList<>();

        for(FormSubmissionEntity formSubmission : submissions) {
            FormSubmission submission = FormSubmission.builder()
                    .week(formSubmission.getWeek())
                    .submitterId(formSubmission.getSubmitterId())
                    .submitDateTime(formSubmission.getSubmitDateTime())
                    .comment(formSubmission.getComment())
                    .build();

            formSubmissions.add(submission);
        }

        return formSubmissions;
    }

    public List<FormSubmission> getAllScoreRecordsByWeek(String week) {
        List<FormSubmissionEntity> submissions = formSubmissionService.findAllSubmissionsByWeek(week);
        if (submissions.isEmpty()) {
            return List.of();
        }

        List<Long> formIds = submissions.stream()
                .map(FormSubmissionEntity::getId)
                .toList();

        Map<String, List<FormScoreRecord>> scoresByReviewer = formScoreRecordService.findByFormIdIn(formIds).stream()
                .collect(Collectors.groupingBy(
                        FormScoreRecordEntity::getReviewerId,
                        Collectors.mapping(e -> FormScoreRecord.builder()
                                        .presenterId(e.getPresenterId())
                                        .score(e.getScore())
                                        .build(),
                                Collectors.toList())
                ));

        // return 的資料格式是 Map<ReviewerId, List<PresenterId, Score>>
        return submissions.stream()
                .map(s -> FormSubmission.builder()
                        .submitterId(s.getSubmitterId())
                        .scores(scoresByReviewer.getOrDefault(s.getSubmitterId(), List.of()))
                        .build())
                .toList();
    }

    public Set<Student> checkParticipant(String week) {
        List<FormSubmission> submissions = getAllSubmissionsByWeek(week);
        Set<String> studentIds= studentService.getAllStudentIds();

        Set<String> submitters = new HashSet<>();
        Set<Student> absentStudents = new HashSet<>();

        for(FormSubmission submission : submissions) {
            submitters.add(submission.getSubmitterId());
        }

        for(String studentId : studentIds) {
            setClassSkippedIfAbsent(studentId, submitters, absentStudents);
        }

        return absentStudents;
    }

    private void setClassSkippedIfAbsent(String studentId, Set<String> submitters, Set<Student> absentStudents) {
        Student student = studentService.getStudentById(studentId);

        if(!submitters.contains(studentId) && student.getClassType() == ClassType.DAY) {
            studentService.setClassSkipped(studentId);
            int skipped = student.getClassSkipped() + 1;
            student.setClassSkipped(skipped);
            absentStudents.add(student);
        }
    }


    private void saveFormSubmission(FormSubmission formSubmission) {
        FormSubmissionEntity formSubmissionEntity = FormSubmissionEntity.builder()
                .submitterId(formSubmission.getSubmitterId())
                .week(formSubmission.getWeek())
                .submitDateTime(formSubmission.getSubmitDateTime())
                .comment(formSubmission.getComment())
                .build();

        formSubmissionService.save(formSubmissionEntity);
    }

    private void saveFormScoreRecords(FormSubmission formSubmission, Long formId) {
        String reviewerId = formSubmission.getSubmitterId();

        List<FormScoreRecordEntity> formScoreRecords = new ArrayList<>();

        for(FormScoreRecord scoreRecord : formSubmission.getScores()) {

            // 如果沒有提供評分就不要記錄這份 score record
            String score = scoreRecord.getScore();
            if(score == null) {
                continue;
            }

            String presenterId = scoreRecord.getPresenterId();

            FormScoreRecordEntity formScoreRecordEntity = FormScoreRecordEntity.builder()
                    .formId(formId)
                    .score(score)
                    .presenterId(presenterId)
                    .reviewerId(reviewerId)
                    .build();

            formScoreRecords.add(formScoreRecordEntity);
        }

        formScoreRecordService.saveAll(formScoreRecords);
    }
}
