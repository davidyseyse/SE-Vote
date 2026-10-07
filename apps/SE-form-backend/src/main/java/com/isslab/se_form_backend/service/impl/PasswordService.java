package com.isslab.se_form_backend.service.impl;

import com.isslab.se_form_backend.entity.StudentEntity;
import com.isslab.se_form_backend.helper.exception.UserNotFoundException;
import com.isslab.se_form_backend.repository.StudentRepository;
import org.springframework.security.crypto.password.PasswordEncoder;

import static com.isslab.se_form_backend.constant.MyConstant.DEFAULT_PASSWORD;

public class PasswordService {

    private final PasswordEncoder passwordEncoder;
    private final StudentRepository studentRepository;

    public PasswordService(PasswordEncoder passwordEncoder, StudentRepository studentRepository) {
        this.passwordEncoder = passwordEncoder;
        this.studentRepository = studentRepository;
    }

    public boolean checkPassword(String studentId, String password) {
        String existedPassword = studentRepository.getPasswordByStudentId(studentId);

        return passwordEncoder.matches(password, existedPassword);
    }

    public void changePassword(String studentId, String newPassword) {
        StudentEntity user = studentRepository.getStudentByStudentId(studentId)
                .orElseThrow(UserNotFoundException::new);
        String encodePassword = passwordEncoder.encode(newPassword);

        StudentEntity studentEntity = new StudentEntity(user.getStudentId(), user.getName(), user.getEmail(), user.getClassType(), encodePassword, user.getClassSkipped());

        studentRepository.save(studentEntity);
    }

    public void resetPassword(String studentId) {
        StudentEntity user = studentRepository.getStudentByStudentId(studentId)
                .orElseThrow(UserNotFoundException::new);
        String encodePassword = passwordEncoder.encode(DEFAULT_PASSWORD);
        user.setPassword(encodePassword);

        studentRepository.save(user);
    }
}
