package com.isslab.se_form_backend.service.impl;

import com.isslab.se_form_backend.constant.MyConstant;
import com.isslab.se_form_backend.entity.StudentEntity;
import com.isslab.se_form_backend.helper.CsvReader;
import com.isslab.se_form_backend.helper.exception.UserNotFoundException;
import com.isslab.se_form_backend.model.ClassType;
import com.isslab.se_form_backend.model.Student;
import com.isslab.se_form_backend.model.StudentUpdate;
import com.isslab.se_form_backend.repository.StudentRepository;
import com.isslab.se_form_backend.service.AbstractStudentService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class StudentService extends AbstractStudentService {
    private final PasswordEncoder passwordEncoder;
    private final StudentRepository studentRepository;
    private final CsvReader csvReader;

    public StudentService(PasswordEncoder passwordEncoder, StudentRepository studentRepository, CsvReader csvReader) {
        this.passwordEncoder = passwordEncoder;
        this.studentRepository = studentRepository;
        this.csvReader = csvReader;
    }

    @Override
    public Set<String> getAllStudentIds() {
        return studentRepository.getAllStudentIds();
    }

    @Override
    public boolean isReviewer(String studentId) {
        StudentEntity student = getStudentEntityById(studentId);
        return student.getClassType() == ClassType.DAY;
    }

    @Override
    public boolean isPresenter(String studentId) {
        StudentEntity student = getStudentEntityById(studentId);
        return student.getClassType() == ClassType.ON_SERVICE;
    }

    @Override
    public Student getStudentById(String studentId) {
        StudentEntity entity = getStudentEntityById(studentId);

        return Student.builder()
                .classType(entity.getClassType())
                .studentId(entity.getStudentId())
                .email(entity.getEmail())
                .name(entity.getName())
                .classSkipped(entity.getClassSkipped())
                .build();
    }

    private StudentEntity getStudentEntityById(String studentId) {
        return studentRepository.getStudentByStudentId(studentId)
                .orElseThrow(UserNotFoundException::new);
    }

    @Override
    public void createSingleStudent(Student studentInfo) {
        String studentId = studentInfo.getStudentId();
        String name = studentInfo.getName();
        String email = studentInfo.getEmail();
        ClassType classType = studentInfo.getClassType();
        String encodePassword = passwordEncoder.encode(MyConstant.DEFAULT_PASSWORD);

        StudentEntity studentEntity = new StudentEntity(studentId, name, email, classType, encodePassword, 0);
        studentRepository.save(studentEntity);
    }

    @Override
    public void createMultiStudents(MultipartFile file) {
        List<StudentEntity> studentList = csvReader.loadStudentsFromCsv(file);
        studentRepository.saveAll(studentList);
    }

    @Override
    public void updateStudentById(StudentUpdate studentInfo) {
        String studentId = studentInfo.getStudentId();
        StudentEntity student = studentRepository.getStudentByStudentId(studentId)
                .orElseThrow(UserNotFoundException::new);

        student.setName(studentInfo.getName());
        student.setEmail(studentInfo.getEmail());

        studentRepository.save(student);
    }


    @Override
    public void deleteStudentById(String studentId) {
        studentRepository.deleteByStudentId(studentId);
    }

    @Override
    public String getNameByStudentId(String studentId) {
        return studentRepository.getNameByStudentId(studentId);
    }

    @Override
    public Set<Student> checkEligibility() {
        Set<String> studentIds = studentRepository.getAllStudentIds();
        Set<String> ineligibleUsers = new HashSet<>();
        Set<Student> ineligibleStudents = new HashSet<>();

        for(String studentId :studentIds) {
            boolean eligibility = checkEligibilityByStudentId(studentId);
            if(!eligibility) {
                ineligibleUsers.add(studentId);
            }
        }

        for(String ineligibleUser : ineligibleUsers) {
            StudentEntity removedStudent = studentRepository.getStudentByStudentId(ineligibleUser)
                    .orElseThrow(UserNotFoundException::new);

            Student ineligibleStudent = Student.builder()
                    .studentId(ineligibleUser)
                    .name(removedStudent.getName())
                    .email(removedStudent.getEmail())
                    .classType(removedStudent.getClassType())
                    .classSkipped(removedStudent.getClassSkipped())
                    .build();

            if(removedStudent.getClassType() == ClassType.DAY) {
                studentRepository.deleteByStudentId(ineligibleUser);
                ineligibleStudents.add(ineligibleStudent);
            }
        }

        return ineligibleStudents;
    }

    @Override
    public void setClassSkipped(String studentId) {
        StudentEntity student = studentRepository.getStudentByStudentId(studentId)
                .orElseThrow(UserNotFoundException::new);

        int count = student.getClassSkipped();
        student.setClassSkipped(count + 1);
        studentRepository.save(student);
    }

    private boolean checkEligibilityByStudentId(String studentId) {
        StudentEntity studentEntity = getStudentEntityById(studentId);
        return studentEntity.getClassSkipped() <= 1;
    }
}
