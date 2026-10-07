package com.isslab.se_form_backend.security.service;

import com.isslab.se_form_backend.entity.StudentEntity;
import com.isslab.se_form_backend.helper.exception.UserNotFoundException;
import com.isslab.se_form_backend.model.ClassType;
import com.isslab.se_form_backend.model.Student;
import com.isslab.se_form_backend.repository.StudentRepository;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Optional;

public class StudentUserDetailsService implements UserDetailsService {
    private final StudentRepository studentRepository;

    public StudentUserDetailsService(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        StudentEntity student = studentRepository.getStudentByStudentId(username)
                .orElseThrow(UserNotFoundException::new);

        if(ClassType.ON_SERVICE.equals(student.getClassType())){
            throw new DisabledException(String.format("該用戶:%s 為夜間部，無法進行評分，請不用登入此系統，謝謝", username));
        }

        return User.builder()
                .username(student.getStudentId())
                .password(student.getPassword())
                .roles("USER")
                .build();
    }
}
