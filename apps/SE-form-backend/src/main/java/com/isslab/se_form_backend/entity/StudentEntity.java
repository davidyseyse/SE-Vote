package com.isslab.se_form_backend.entity;

import com.isslab.se_form_backend.model.ClassType;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class StudentEntity {
    @Id
    private String studentId;

    private String name;
    private String email;
    private ClassType classType;
    private String password;
    private int classSkipped;
}
