package com.isslab.se_form_backend.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
public class ReviewerGrade {
    private String reviewerId;
    private List<PresenterGrade> grades;  // presenterId: 被評分的報告者, grade: 評分者對應的得分
}
