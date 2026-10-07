package com.isslab.se_form_backend.repository;

import com.isslab.se_form_backend.entity.ReviewerGradeEntity;
import com.isslab.se_form_backend.entity.id.ReviewerGradeEntityId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

public interface ReviewerRepository extends JpaRepository<ReviewerGradeEntity, ReviewerGradeEntityId> {
    void deleteByReviewerIdAndWeek(String reviewerId, String week);

    @Query("SELECT r.grade FROM ReviewerGradeEntity r WHERE r.reviewerId = :reviewerId AND r.week = :week")
    List<Double> getGradesByReviewerIdAndWeek(@Param("reviewerId") String reviewerId,
                                             @Param("week") String week);

    List<ReviewerGradeEntity> findAllByWeekAndGradeIsNull(String week);

    List<ReviewerGradeEntity> findAllByWeek(String week);

    @Query("SELECT r FROM ReviewerGradeEntity r WHERE r.reviewerId = :reviewerId AND r.presenterId = :presenterId AND r.week = :week")
    ReviewerGradeEntity findByIdsAndGrade(
            @Param("reviewerId") String reviewerId,
            @Param("presenterId") String presenterId,
            @Param("week") String week
    );
}
