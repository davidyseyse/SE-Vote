package com.isslab.se_form_backend.entity;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Entity
@Builder
@Table(
        name = "presenter",
        uniqueConstraints = @UniqueConstraint(columnNames = {"presenterId", "week"})
)
@NoArgsConstructor
@AllArgsConstructor
public class PresenterGradeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String presenterId;
    private String week;
    private int presentOrder;
    private double grade;

    @Column(nullable = false, columnDefinition = "boolean default true")
    private boolean participate;
}
