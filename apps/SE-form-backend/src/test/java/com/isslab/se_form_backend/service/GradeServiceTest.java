//package com.isslab.se_form_backend.service;
//
//import com.isslab.se_form_backend.entity.FormScoreRecordEntity;
//import com.isslab.se_form_backend.repository.FormScoreRecordRepository;
//import com.isslab.se_form_backend.service.impl.FormProcessingService;
//import com.isslab.se_form_backend.service.impl.mock.*;
//import com.isslab.se_form_backend.service.util.FormScoreRecordLoader;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.TestInstance;
//import org.mockito.Mock;
//
//import java.io.IOException;
//import java.util.List;
//import java.util.Map;
//
//import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
//
//@TestInstance(TestInstance.Lifecycle.PER_CLASS)
//class GradeServiceTest {
//
//    private List<FormScoreRecordEntity> allRecordsNoOutlier;
//    private List<FormScoreRecordEntity> bScoreRecords;
//    private MockGradeService gradeService;
//    @Mock
//    FormScoreRecordRepository formScoreRecordRepository;
//
//
//    @BeforeEach
//    public void setUp() throws IOException {
//        List<FormScoreRecordEntity> allRecords = FormScoreRecordLoader.loadFromCsv("/test_data/sample.csv");
//        allRecordsNoOutlier = FormScoreRecordLoader.loadFromCsv("/test_data/sample_no_outlier.csv");
//        bScoreRecords = FormScoreRecordLoader.filterByScore(allRecords, "B");
//
//        MockReviewerService reviewerService = new MockReviewerService();
//        MockPresenterService presenterService = new MockPresenterService();
//        AbstractStudentService studentService = new MockStudentService();
//        AbstractFormSubmissionService formSubmissionService = new MockFormSubmissionService();
//        AbstractFormScoreRecordService formScoreRecordService = new MockFormScoreRecordService();
//        FormProcessingService formProcessingService = new FormProcessingService(formSubmissionService, formScoreRecordService, studentService);
//        gradeService = new MockGradeService(reviewerService, presenterService, studentService, formProcessingService);
//
//        // prepare for testGetGradeByIdAndWeek()
//        gradeService.saveGradeToReviewer("113525009", "113552015", "1", 60);
//    }
//
//    @Test
//    public void testCalculateGrades() throws IOException {
//        Map<String, Double> answerGradeListOnServiceClass = gradeService.calculateGradeBySinglePresenter("1", 1);
//        assertThat(answerGradeListOnServiceClass.get("113525009")).isEqualTo(60);
//    }
//
//    @Test
//    public void testCalculateGradesNoOutliers(){
//        //計算成績
//        Map<String, Double> answerGradeListOnServiceClass = gradeService.calculateGradeBySinglePresenter(allRecordsNoOutlier);
//
//        Double answerGradePresenter1 = answerGradeListOnServiceClass.get("113525009");
//        assertThat(answerGradePresenter1).isEqualTo(96.2);
//
//        Double answerGradePresenter2 = answerGradeListOnServiceClass.get("110502545");
//        assertThat(answerGradePresenter2).isEqualTo(96.2);
//
//        Double answerGradePresenter3 = answerGradeListOnServiceClass.get("113525010");
//        assertThat(answerGradePresenter3).isEqualTo(88.5);
//    }
//
//    @Test
//    public void testCalculateGradesAllB(){
//        //計算成績
//        Map<String, Double> answerGradeListOnServiceClass = gradeService.calculateGradeBySinglePresenter(bScoreRecords);
//
//        Double answerGradePresenter1 = answerGradeListOnServiceClass.get("110502516");
//        assertThat(answerGradePresenter1).isEqualTo(100);
//
//        Double answerGradePresenter2 = answerGradeListOnServiceClass.get("109502004");
//        assertThat(answerGradePresenter2).isEqualTo(100);
//    }
//
//    @Test
//    public void testSaveGradesToStudent(){
//        gradeService.saveGradeToPresenter("1135520015", "1", 60);
//    }
//
//    @Test
//    public void testGetGradeByIdAndWeek(){
//        double answer = gradeService.getGradesByIdAndWeek("113525009", "1").get(0);
//        assertThat(answer).isEqualTo(60);
//    }
//
////    @Test
////    public void testUpdateGradeToStudent(){
////        gradeService.updateGradeByIdAndWeek("113525009", "1", 70);
////    }
//}