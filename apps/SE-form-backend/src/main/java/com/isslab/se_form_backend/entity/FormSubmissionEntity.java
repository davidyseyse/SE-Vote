package com.isslab.se_form_backend.entity;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(
        name = "form_submission",
        uniqueConstraints = @UniqueConstraint(columnNames = {"submitterId", "week"})
)
public class FormSubmissionEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String submitterId;
    private String week;
    private String submitDateTime;
    private String comment;
}
