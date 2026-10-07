package com.isslab.se_form_backend.entity;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(
        name = "form_score_record",
        uniqueConstraints = @UniqueConstraint(columnNames = {"formId", "reviewerId", "presenterId"})
)
public class FormScoreRecordEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long formId;
    private String score;
    private String reviewerId;
    private String presenterId;
}
