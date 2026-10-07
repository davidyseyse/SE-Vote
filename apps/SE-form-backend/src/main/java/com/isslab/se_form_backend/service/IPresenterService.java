package com.isslab.se_form_backend.service;

import com.isslab.se_form_backend.model.Presenter;

import java.util.Date;

public interface IPresenterService {
    void savePresenter(Presenter presenter);
    String getPresenterIdByWeekAndOrder(String week, int presentOrder);
}
