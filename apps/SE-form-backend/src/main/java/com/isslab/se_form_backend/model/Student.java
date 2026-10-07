package com.isslab.se_form_backend.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class Student {

    private String studentId;
    private String name;
    private String email;
    private ClassType classType;
    private String password;
    private int classSkipped;
}
