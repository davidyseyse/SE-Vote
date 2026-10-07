package com.isslab.se_form_backend.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@Builder
public class Presenter {

    private String presenterId;
    private String presentWeek;
    private String presentOrder;
    private String presenterName;
    private boolean participate;
}
