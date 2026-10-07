package com.isslab.se_form_backend.service;

import com.isslab.se_form_backend.entity.FormScoreRecordEntity;
import com.isslab.se_form_backend.model.FormScoreRecord;

import java.util.List;

public abstract class AbstractFormScoreRecordService {

    public abstract List<FormScoreRecord> getAll();
    public abstract FormScoreRecord getById(Long id);
    public abstract void save(FormScoreRecordEntity record);
    public abstract void saveAll(List<FormScoreRecordEntity> records);
    public abstract void update(Long id, FormScoreRecordEntity newRecord);
    public abstract void delete(Long id);
    public abstract FormScoreRecordEntity findByFormIdAndPresenterId(Long formId, String presenterId);
    public abstract List<FormScoreRecordEntity> findByFormIdIn(List<Long> formIds);
}
