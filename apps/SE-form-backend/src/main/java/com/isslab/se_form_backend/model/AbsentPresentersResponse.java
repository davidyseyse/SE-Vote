package com.isslab.se_form_backend.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
public class AbsentPresentersResponse {
    private String message;
    private Set<Presenter> presenters;
}
