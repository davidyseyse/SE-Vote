package com.isslab.se_form_backend.model;

import com.isslab.se_form_backend.service.AbstractStudentRoleService;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Map;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
public class StudentGroupingContext {
    private final String studentId;
    private final String week;
    private final Map<String, Double> grades;
    private final Set<String> gradedStudentIds;
    private final Map<AbstractStudentRoleService, List<GradeInput>> grouped;
}
