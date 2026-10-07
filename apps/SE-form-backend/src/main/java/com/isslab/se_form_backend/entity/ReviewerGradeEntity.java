package com.isslab.se_form_backend.entity;

import com.isslab.se_form_backend.entity.id.ReviewerGradeEntityId;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "reviewer")
@IdClass(ReviewerGradeEntityId.class)
public class ReviewerGradeEntity {
    @Id
    private String reviewerId;
    @Id
    private String presenterId;
    @Id
    private String week;

    private double grade;
}


