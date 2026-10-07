package com.isslab.se_form_backend.service.impl.mock;

import com.isslab.se_form_backend.model.ClassType;
import com.isslab.se_form_backend.model.Student;
import com.isslab.se_form_backend.model.StudentUpdate;
import com.isslab.se_form_backend.service.AbstractStudentService;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Set;

public class MockStudentService extends AbstractStudentService {
    @Override
    public Set<String> getAllStudentIds() {
        return Set.of();
    }

    @Override
    public boolean isReviewer(String studentId) {
        return true;
    }

    @Override
    public boolean isPresenter(String studentId) {
        return studentId.equals("113500001");
    }

    @Override
    public Student getStudentById(String studentId) {
        return Student.builder()
                .studentId(studentId)
                .name("Test")
                .email(studentId + "test@gmail.com")
                .classType(ClassType.DAY)
                .build();
    }

    @Override
    public void createSingleStudent(Student studentInfo) {
    }

    @Override
    public void createMultiStudents(MultipartFile file) {}

    @Override
    public void updateStudentById(StudentUpdate studentInfo) {
    }

    @Override
    public void deleteStudentById(String studentId) {

    }

    @Override
    public String getNameByStudentId(String studentId) {
        return "";
    }

    @Override
    public Set<Student> checkEligibility() {
        return Set.of();
    }

    @Override
    public void setClassSkipped(String studentId) {

    }
}
