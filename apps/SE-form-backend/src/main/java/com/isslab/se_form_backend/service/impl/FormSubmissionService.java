package com.isslab.se_form_backend.service.impl;

import com.isslab.se_form_backend.entity.FormSubmissionEntity;
import com.isslab.se_form_backend.model.FormSubmission;
import com.isslab.se_form_backend.repository.FormSubmissionRepository;
import com.isslab.se_form_backend.service.AbstractFormSubmissionService;

import java.util.List;

public class FormSubmissionService extends AbstractFormSubmissionService {

    private final FormSubmissionRepository formSubmissionRepository;

    public FormSubmissionService(FormSubmissionRepository formSubmissionRepository) {
        this.formSubmissionRepository = formSubmissionRepository;
    }

    @Override
    public void save(FormSubmissionEntity formSubmission) {
        formSubmissionRepository
                .findBySubmitterIdAndWeek(formSubmission.getSubmitterId(), formSubmission.getWeek())
                .ifPresent(existing -> formSubmission.setId(existing.getId()));

        formSubmissionRepository.save(formSubmission); // update or insert
    }

    @Override
    public List<FormSubmissionEntity> findAllSubmissionsByWeek(String week) {
        return formSubmissionRepository.getAllByWeek(week);
    }

    @Override
    public Long getFormId(String submitterId, String week) {
        return formSubmissionRepository.getIdBySubmitterIdAndWeek(submitterId, week);
    }
}
