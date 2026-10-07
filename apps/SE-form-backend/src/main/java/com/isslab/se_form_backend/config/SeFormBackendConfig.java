package com.isslab.se_form_backend.config;

import com.isslab.se_form_backend.helper.CsvReader;
import com.isslab.se_form_backend.helper.FormScoreCsvImporter;
import com.isslab.se_form_backend.helper.FormSubmissionImporter;
import com.isslab.se_form_backend.repository.*;
import com.isslab.se_form_backend.service.*;
import com.isslab.se_form_backend.service.impl.*;
import com.isslab.se_form_backend.service.impl.mock.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableJpaRepositories(basePackages = "com.isslab.se_form_backend.repository")
public class SeFormBackendConfig {

    private final Boolean MOCK = Boolean.FALSE;

    @Bean
    public CsvReader csvReader(PasswordEncoder passwordEncoder){
        return new CsvReader(passwordEncoder);
    }

    @Bean
    public AbstractStudentService studentService(
            PasswordEncoder passwordEncoder,
            StudentRepository studentRepository,
            CsvReader csvReader){
        if (MOCK) {
            return new MockStudentService();
        }
        return new StudentService(passwordEncoder, studentRepository, csvReader);
    }

//    @Bean
//    public AbstractStudentRoleService reviewerService(ReviewerRepository reviewerRepository){
//        if (MOCK) {
//            return new MockReviewerService();
//        }
//        return new ReviewerService(reviewerRepository);
//    }

    @Bean
    public AbstractStudentRoleService presenterServiceAbstract(PresenterRepository presenterRepository, AbstractStudentService studentService){
        if (MOCK) {
            return new MockPresenterService();
        }
        return new PresenterService(presenterRepository, studentService);
    }

    @Bean
    public AbstractGradeService gradeService(AbstractStudentRoleService reviewerService,
                                             AbstractStudentRoleService presenterService,
                                             AbstractStudentService studentService,
                                             FormProcessingService formProcessingService){
        if (MOCK) {
            return new MockGradeService(reviewerService, presenterService, studentService, formProcessingService);
        }
        return new GradeService(reviewerService, presenterService, studentService, formProcessingService);
    }

    @Bean
    public AbstractFormScoreRecordService formScoreRecordService(FormScoreRecordRepository formScoreRecordRepository){
        if (MOCK) {
            return new MockFormScoreRecordService();
        }
        return new FormScoreRecordService(formScoreRecordRepository);
    }

    @Bean
    public FormScoreCsvImporter formScoreCsvImporter(FormScoreRecordRepository formScoreRecordRepository){
        return new FormScoreCsvImporter(formScoreRecordRepository);
    }

    @Bean
    public FormSubmissionImporter formSubmissionImporter(FormSubmissionRepository formSubmissionRepository){
        return new FormSubmissionImporter(formSubmissionRepository);
    }

    @Bean
    public AbstractFormSubmissionService formSubmissionService(FormSubmissionRepository formSubmissionRepository){
        if (MOCK) {
            return new MockFormSubmissionService();
        }
        return new FormSubmissionService(formSubmissionRepository);
    }

    @Bean
    public PresenterService presenterService(PresenterRepository presenterRepository, AbstractStudentService studentService) {
        return new PresenterService(presenterRepository, studentService);
    }

    @Bean
    public ReviewerService reviewerService(ReviewerRepository reviewerRepository) {
        return new ReviewerService(reviewerRepository);
    }

    @Bean
    public FormProcessingService formProcessingService(AbstractFormSubmissionService formSubmissionService, AbstractFormScoreRecordService formScoreRecordService, AbstractStudentService studentService) {
        return new FormProcessingService(formSubmissionService, formScoreRecordService, studentService);
    }

    @Bean
    public PasswordService passwordService(PasswordEncoder passwordEncoder, StudentRepository studentRepository) {
        return new PasswordService(passwordEncoder, studentRepository);
    }

    @Bean
    public ExportService exportService(PresenterService presenterService, ReviewerService reviewerService, FormProcessingService formProcessingService) {
        return new ExportService(presenterService, reviewerService, formProcessingService);
    }
}


