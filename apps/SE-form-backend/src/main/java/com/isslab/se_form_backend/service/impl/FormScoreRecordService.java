package com.isslab.se_form_backend.service.impl;

import com.isslab.se_form_backend.entity.FormScoreRecordEntity;
import com.isslab.se_form_backend.model.FormScoreRecord;
import com.isslab.se_form_backend.repository.FormScoreRecordRepository;
import com.isslab.se_form_backend.service.AbstractFormScoreRecordService;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

public class FormScoreRecordService extends AbstractFormScoreRecordService {

    private final FormScoreRecordRepository repository;

    public FormScoreRecordService(FormScoreRecordRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<FormScoreRecord> getAll() {
        List<FormScoreRecordEntity> entities = repository.findAll();
        List<FormScoreRecord> records = new ArrayList<>();

        for(FormScoreRecordEntity entity : entities) {
            FormScoreRecord record = new FormScoreRecord(
                    entity.getScore(),
                    entity.getPresenterId()
            );

            records.add(record);
        }

        return records;
    }

    @Override
    public FormScoreRecord getById(Long id) {
        FormScoreRecordEntity entity = repository.findById(id).orElse(null);
        if(entity == null) return null;

        return new FormScoreRecord(
                entity.getScore(),
                entity.getPresenterId()
        );
    }

    @Override
    public void save(FormScoreRecordEntity record) {
        repository.save(record);
    }

    @Override
    public void saveAll(List<FormScoreRecordEntity> records) {
        if (records.isEmpty()) return;

        Long formId = records.get(0).getFormId();
        List<String> reviewerIds = records.stream().map(FormScoreRecordEntity::getReviewerId).toList();
        List<String> presenterIds = records.stream().map(FormScoreRecordEntity::getPresenterId).toList();

        List<FormScoreRecordEntity> existing = repository.findByFormIdAndReviewerIdInAndPresenterIdIn(
                formId, reviewerIds, presenterIds
        );

        Map<String, Long> existingMap = existing.stream().collect(Collectors.toMap(
                r -> r.getReviewerId() + "|" + r.getPresenterId(),
                FormScoreRecordEntity::getId
        ));

        for (FormScoreRecordEntity r : records) {
            String key = r.getReviewerId() + "|" + r.getPresenterId();
            if (existingMap.containsKey(key)) {
                r.setId(existingMap.get(key)); // 設定 id → save 時變 update
            }
        }

        repository.saveAll(records);
    }


    @Override
    public void update(Long id, FormScoreRecordEntity newRecord) {
        FormScoreRecordEntity record = repository.findById(id).orElse(null);

        if(record != null) {

            // 確認評分者是同一個人
            if(!Objects.equals(record.getReviewerId(), newRecord.getReviewerId())){
                throw new IllegalArgumentException("Reviewer 必須相同\n" + "Recorded reviewer: " + record.getReviewerId() + "\n" + "New reviewer: " + newRecord.getReviewerId());
            };

            // 只需要改 score 就好，表單 id、評分者跟被評者都沿用就可以了
            record.setScore(newRecord.getScore());
            repository.save(record);
        }
    }

    @Override
    public void delete(Long id) {
        repository.deleteById(id);
    }

    @Override
    public FormScoreRecordEntity findByFormIdAndPresenterId(Long formId, String presenterId) {
        return repository.findByFormIdAndPresenterId(formId, presenterId);
    }

    @Override
    public List<FormScoreRecordEntity> findByFormIdIn(List<Long> formIds) {
        return repository.findByFormIdIn(formIds);
    }
}
