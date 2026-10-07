package com.isslab.se_form_backend.helper;

import com.isslab.se_form_backend.model.Status;

public class ResponseStatus {

    public static Status statusOk() {
        return Status.builder().status("200").response("ok").build();
    }
}
