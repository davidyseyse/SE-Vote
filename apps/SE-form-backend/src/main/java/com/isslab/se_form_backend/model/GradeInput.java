package com.isslab.se_form_backend.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class GradeInput {
    private String studentId;
    private String presenterId;
    private String week;
    private double grade;
}
