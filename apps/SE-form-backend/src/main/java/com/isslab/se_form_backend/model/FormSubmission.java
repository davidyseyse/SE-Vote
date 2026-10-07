package com.isslab.se_form_backend.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;
import java.util.List;

@Getter
@Setter
@Builder
@ToString
public class FormSubmission {
    private String submitterId;
    private String week;
    private List<FormScoreRecord> scores;
    private String submitDateTime;
    private String comment;
}
