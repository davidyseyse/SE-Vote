package com.isslab.se_form_backend.repository;

import com.isslab.se_form_backend.entity.StudentEntity;
import com.isslab.se_form_backend.model.Student;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.Set;

public interface StudentRepository extends JpaRepository<StudentEntity, Long> {
    Optional<StudentEntity> getStudentByStudentId(String studentId);

    @Query("SELECT s.studentId FROM StudentEntity s")
    Set<String> getAllStudentIds();

    @Transactional
    void deleteByStudentId(String studentId);

    @Query("SELECT s.name FROM StudentEntity s WHERE s.studentId = :studentId")
    String getNameByStudentId(@Param("studentId") String studentId);

    @Query("SELECT s.password FROM StudentEntity s WHERE s.studentId = :studentId")
    String getPasswordByStudentId(@Param("studentId") String studentId);
}
