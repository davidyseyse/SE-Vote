package com.isslab.se_form_backend.repository;

import com.isslab.se_form_backend.entity.FormScoreRecordEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FormScoreRecordRepository extends JpaRepository<FormScoreRecordEntity, Long> {
    FormScoreRecordEntity findByFormIdAndPresenterId(Long formId, String presenterId);
    List<FormScoreRecordEntity> findByFormIdIn(List<Long> formIds);
    List<FormScoreRecordEntity> findByFormIdAndReviewerIdInAndPresenterIdIn(Long formId, List<String> reviewerIds, List<String> presenterIds);
}
