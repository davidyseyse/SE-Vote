package com.isslab.se_form_backend.service.impl;

import com.isslab.se_form_backend.service.AbstractFormScoreRecordService;
import com.isslab.se_form_backend.service.AbstractGradeService;
import com.isslab.se_form_backend.service.AbstractStudentRoleService;
import com.isslab.se_form_backend.service.AbstractStudentService;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

public class GradeService extends AbstractGradeService {

    public GradeService(AbstractStudentRoleService reviewerService, AbstractStudentRoleService presenterService, AbstractStudentService studentService, FormProcessingService formProcessingService) {
        this.reviewerService = reviewerService;
        this.presenterService = presenterService;
        this.studentService = studentService;
        this.formProcessingService = formProcessingService;
    }

    @Override
    public void saveGradeToPresenter(String presenterId, String week, double grade) {
        presenterService.saveGradeToPresenter(presenterId, week, grade);
    }

    @Override
    public void saveGradeToReviewer(String reviewerId, String presenterId, String week, double grade) {
        reviewerService.saveGradeToReviewer(reviewerId, presenterId, week, grade);
    }

    @Override
    public List<Double> getGradesByIdAndWeek(String studentId, String week) {
        AbstractStudentRoleService roleService = getServiceByRole(studentId);
        return roleService.getGradesByIdAndWeek(studentId, week);
    }

    @Override
    public void deleteGradeByIdAndWeek(String studentId, String week) {
        AbstractStudentRoleService roleService = getServiceByRole(studentId);
        roleService.deleteGradeByIdAndWeek(studentId, week);
    }

}
