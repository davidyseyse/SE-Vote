package com.isslab.se_form_backend.repository;

import com.isslab.se_form_backend.entity.FormSubmissionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

public interface FormSubmissionRepository extends JpaRepository<FormSubmissionEntity, Long> {
    List<FormSubmissionEntity> getAllByWeek(String week);

    @Query("SELECT f.id FROM FormSubmissionEntity f WHERE f.submitterId = :submitterId AND f.week = :week")
    Long getIdBySubmitterIdAndWeek(String submitterId, String week);

    Optional<FormSubmissionEntity> findBySubmitterIdAndWeek(String submitterId, String week);

}
