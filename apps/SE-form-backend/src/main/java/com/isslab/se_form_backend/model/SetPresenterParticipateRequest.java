package com.isslab.se_form_backend.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SetPresenterParticipateRequest {
    private String presenterId;
    private String week;
    private Boolean participate;
}
