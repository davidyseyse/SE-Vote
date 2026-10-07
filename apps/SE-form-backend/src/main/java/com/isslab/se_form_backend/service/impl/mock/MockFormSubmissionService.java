package com.isslab.se_form_backend.service.impl.mock;

import com.isslab.se_form_backend.entity.FormSubmissionEntity;
import com.isslab.se_form_backend.helper.MockDataBuilder;
import com.isslab.se_form_backend.service.AbstractFormSubmissionService;

import java.util.List;

public class MockFormSubmissionService extends AbstractFormSubmissionService {

    @Override
    public void save(FormSubmissionEntity formSubmissionEntity) {
    }

    @Override
    public List<FormSubmissionEntity> findAllSubmissionsByWeek(String week) {
        return MockDataBuilder.loadSubmissionFromCsv("src/test/resources/test_data/sample.csv");
    }

    @Override
    public Long getFormId(String submitterId, String week) {
        return 0L;
    }

}
