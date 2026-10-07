package com.isslab.se_form_backend.model;

import lombok.*;

import java.util.Date;
import java.util.List;

/*
 * 這個 model 是用來承載前端回傳的表單回應用的
 * 內容會被 FormEntity 和 ReviewEntity 調用
 */

@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
public class FormScoreRecord {
    private String score;
    private String presenterId;
}
