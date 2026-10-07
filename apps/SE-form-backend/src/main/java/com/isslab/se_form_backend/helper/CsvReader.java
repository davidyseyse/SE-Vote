package com.isslab.se_form_backend.helper;

import com.isslab.se_form_backend.constant.MyConstant;
import com.isslab.se_form_backend.entity.StudentEntity;
import com.isslab.se_form_backend.model.ClassType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class CsvReader {
    private final PasswordEncoder passwordEncoder;
    public CsvReader(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    public List<StudentEntity> loadStudentsFromCsv(MultipartFile csvFile) {
        List<StudentEntity> StudentEntities = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(csvFile.getInputStream()))) {
            reader.readLine(); // 讀掉 header，不用處理

            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                String name = parts[0];
                String studentId = parts[1];
                String email = parts[2];
                ClassType classType = ClassType.valueOf(parts[3]);
                String encodedPassword = passwordEncoder.encode(MyConstant.DEFAULT_PASSWORD);

                StudentEntity student = new StudentEntity(studentId, name, email, classType, encodedPassword, 0);

                StudentEntities.add(student);
            }
            return StudentEntities;

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
