package com.isslab.se_form_backend.repository;

import com.isslab.se_form_backend.entity.PresenterGradeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Set;

public interface PresenterRepository extends JpaRepository<PresenterGradeEntity, Long> {
    void deleteByPresenterIdAndWeek(String presenterId, String week);

    @Query("SELECT r.grade FROM PresenterGradeEntity r WHERE r.presenterId = :presenterId AND r.week = :week")
    List<Double> getGradesByPresenterIdAndWeek(@Param("presenterId") String presenterId,
                                              @Param("week") String week);

    PresenterGradeEntity getByPresenterIdAndWeek(String presenterId, String week);
    PresenterGradeEntity getByWeekAndPresentOrder(String week, int order);

    List<PresenterGradeEntity> findAllByWeek(String week);
    List<PresenterGradeEntity> findAllByPresenterIdInAndWeekIn(List<String> presenterId, List<String> week);

    @Query("SELECT p.presenterId FROM PresenterGradeEntity p WHERE p.week = :week")
    Set<String> getPresenterIdsByWeek(String week);
}
