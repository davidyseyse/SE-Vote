package com.isslab.se_form_backend.service;

import com.isslab.se_form_backend.entity.FormScoreRecordEntity;
import com.isslab.se_form_backend.helper.exception.InvalidSaveGradeException;
import com.isslab.se_form_backend.model.GradeInput;
import com.isslab.se_form_backend.service.impl.FormProcessingService;
import com.isslab.se_form_backend.service.impl.GradeHelper;
import com.isslab.se_form_backend.service.impl.PresenterService;
import com.isslab.se_form_backend.service.impl.ReviewerService;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;
import org.springframework.beans.factory.annotation.Value;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

@NoArgsConstructor
@AllArgsConstructor
public abstract class AbstractGradeService {

    //zScore 的閾值
    private static final double zScoreThreshold = 2.5;
    private static final double outlierGrade = 60.0;

    //score 對照 grade，可從 application.yaml 修改
    @Value("#{${score}}")
    private Map<String, Double> scoreMap;

    private final Map<String, Double> presenterGradeMap = new HashMap<>(); // reviewerId : presenterGradeByReviewer
    private final Map<String, Double> reviewerGradeMap = new HashMap<>(); // reviewerId : reviewerGrade
    private final Map<String, Double> reviewerZScoreMap = new HashMap<>(); // reviewerId : reviewerZScore
    private final List<Map<String, Double>> allGradesByPresenter = new ArrayList<>(); // all grades by week

    @Getter
    private String presenterId;

    @Getter
    private double presenterAvgGrade;
    private double stdDev;

    protected AbstractStudentService studentService;
    protected AbstractStudentRoleService reviewerService;
    protected AbstractStudentRoleService presenterService;
    protected FormProcessingService formProcessingService;

    public void calculateGradesByWeek(String week) {
        initGradeVariables();

        Map<String, List<FormScoreRecordEntity>> allPresentersRecords = formProcessingService.loadFormScoreRecordsByWeek(week);

        Iterator<Map.Entry<String, List<FormScoreRecordEntity>>> iterator = allPresentersRecords.entrySet().iterator();
        while(iterator.hasNext()) {
            Map.Entry<String, List<FormScoreRecordEntity>> entry = iterator.next();
            this.presenterId = entry.getKey();

            boolean participated = presenterService.checkParticipated(presenterId, week);
            if(!participated) {
                iterator.remove();
                double basicGrade = presenterService.getBasicGrade();
                saveGradeToPresenter(presenterId, week, basicGrade); // Presenter 如果沒有參加報告就直接給基本分

                continue;
            }

            Map<String, Double> grades = calculateGradeBySinglePresenter(entry.getValue());
            allGradesByPresenter.add(grades); // 每份 grades 含 reviewer 成績 + presenter 成績
        }
    }

    public void saveAllGradesByWeek(String week, Set<String> presenterIds) {
        Map<AbstractStudentRoleService, List<GradeInput>> grouped = new HashMap<>();

        for (Map<String, Double> grades : allGradesByPresenter) {
            Set<String> gradedStudentIds = grades.keySet();

            String currentPresenterId = grades.keySet().stream()
                    .filter(studentService::isPresenter)
                    .findFirst()
                    .orElseThrow(() -> new IllegalStateException("找不到對應的 presenterId"));

            getAllStudents().stream()
                    // Filter 負責篩選出有參加評分的 reviewers 以及 presenters
                    .filter(studentId -> {
                        AbstractStudentRoleService role = getServiceByRole(studentId);
                        return !(role instanceof PresenterService) || gradedStudentIds.contains(studentId);
                    })
                    .forEach(studentId -> {
                        AbstractStudentRoleService roleService = getServiceByRole(studentId);
                        double grade = assignGradesByParticipation(roleService, grades, gradedStudentIds, studentId);
                        setGroupsByRoleService(roleService, week, grade, studentId, grouped, currentPresenterId);
                    });
        }

        saveGroupedGrades(grouped);
    }

    public Map<String, Double> calculateGradeBySinglePresenter(String week, int presentOrder) {
        initGradeVariables();

        if(presenterService instanceof PresenterService pService) {
            presenterId = pService.getPresenterIdByWeekAndOrder(week, presentOrder);
        }
        List<FormScoreRecordEntity> records = formProcessingService.loadFormScoreRecordsByWeekAndPresenter(week, presenterId);
        return calculateGradeBySinglePresenter(records);
    }

    public Map<String, Double> calculateGradeBySinglePresenter(List<FormScoreRecordEntity> formScoreRecordList) {

        // map the reviewers' id and the scores for the presenter.
        presenterGradeMap.clear();
        setPresenterGradeMap(formScoreRecordList);

        // calculate the mean (presenter's grade) and stdDev with grades.
        setPresenterGradeStatics();

        // Map the studentId to their zScore.
        reviewerZScoreMap.clear();
        setReviewerZScoreMap();

        // calculate the reviewers' grade for first time.
        reviewerGradeMap.clear();
        assignReviewerGradesByRound(1);

        // remove the outliers.
        removeOutliersForRound2();

        // re-calculate the statics for round 2.
        setPresenterGradeStatics();
        setReviewerZScoreMap();

        // calculate the reviewers' grade for second time (without outliers.)
        assignReviewerGradesByRound(2);

        // return the reviewers' grades with presenter's grade.
        return concatReviewerGradeMapAndPresenterGrade();
    }

    public void saveAllGradesByWeekAndPresenter(String week, Map<String, Double> grades) {
        Map<AbstractStudentRoleService, List<GradeInput>> grouped = new HashMap<>();
        Set<String> gradedStudentIds = grades.keySet();
        Set<String> allStudentIds = getAllStudents();

        validateStudents(allStudentIds, gradedStudentIds);

        for (String studentId : gradedStudentIds) {
            groupSingleStudentToRole(studentId, week, grades, gradedStudentIds, grouped, this.presenterId);
        }

        for (Map.Entry<AbstractStudentRoleService, List<GradeInput>> entry : grouped.entrySet()) {
            entry.getKey().saveAllGrades(entry.getValue());
        }
    }

    public abstract void saveGradeToPresenter(String presenterId, String week, double grade);
    public abstract void saveGradeToReviewer(String reviewerId, String presenterId, String week, double grade);
    public abstract List<Double> getGradesByIdAndWeek(String studentId, String week);
    public abstract void deleteGradeByIdAndWeek(String studentId, String week);

    protected double calculateZScore(Map.Entry<String, Double> entry) {
        double gradeByReviewer = entry.getValue();
        return (gradeByReviewer - presenterAvgGrade) / stdDev;
    }

    protected void setPresenterGradeMap(List<FormScoreRecordEntity> formScoreRecordList) {
        for(FormScoreRecordEntity formScoreRecord : formScoreRecordList) {
            String presenterScore = formScoreRecord.getScore();
            String reviewerId = formScoreRecord.getReviewerId();

            double presenterGrade = scoreMap.getOrDefault(presenterScore, 0.0);
            presenterGradeMap.put(reviewerId, presenterGrade);
        }
    }

    protected void setPresenterGradeStatics() {
        DescriptiveStatistics presenterGradeStatics = new DescriptiveStatistics();
        for (double grade : presenterGradeMap.values()) {
            presenterGradeStatics.addValue(grade);
        }
        presenterAvgGrade = presenterGradeStatics.getMean();
        stdDev = GradeHelper.calculatePopulationStandardDeviation(presenterGradeStatics);
        if (stdDev == 0) stdDev = 1; // 避免除以 0 的狀況
    }

    protected void setReviewerZScoreMap() {
        for(Map.Entry<String, Double> entry : presenterGradeMap.entrySet()) {
            String studentId = entry.getKey();
            double zScore = calculateZScore(entry);
            reviewerZScoreMap.put(studentId, zScore);
        }
    }

    protected double calculateReviewerGrade(double zScore) {
        double scoreGap = GradeHelper.getGradeGap(scoreMap);
        double reviewerGrade = 100 - (Math.abs(zScore) / 3) * scoreGap;

        // 四捨五入兩次避免精度問題
        BigDecimal bd = new BigDecimal(Double.toString(reviewerGrade))
                .setScale(2, RoundingMode.HALF_UP)
                .setScale(1, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }

    protected void assignReviewerGradesByRound(int round) {
        for (Map.Entry<String, Double> entry : reviewerZScoreMap.entrySet()) {
            String studentId = entry.getKey();
            double zScore = entry.getValue();

            // Round 1: identify the outliers and calculate the grades for other reviewers.
            // Round 2: calculate the grades for reviewers except for the outliers from round 1.
            if (round < 2 && Math.abs(zScore) > zScoreThreshold) {
                reviewerGradeMap.put(studentId, outlierGrade);
            } else {
                double reviewerGrade = calculateReviewerGrade(zScore);
                reviewerGradeMap.put(studentId, reviewerGrade);
            }
        }
    }

    protected void removeOutliersForRound2() {
        // 收集要被移除的 students
        Set<String> keysToRemove = new HashSet<>();
        for (Map.Entry<String, Double> entry : reviewerGradeMap.entrySet()) {
            if (entry.getValue() <= outlierGrade) {
                keysToRemove.add(entry.getKey());
            }
        }

        // 移除第二輪計算時會使用到的 map 中的 outliers
        for (String key : keysToRemove) {
            presenterGradeMap.remove(key);
            reviewerZScoreMap.remove(key);
        }
    }

    protected Map<String, Double> concatReviewerGradeMapAndPresenterGrade() {
        Map<String, Double> studentGradeMap = new HashMap<>(reviewerGradeMap);
        studentGradeMap.put(presenterId, presenterAvgGrade);

        return studentGradeMap;
    }

    protected Set<String> getAllStudents() {
        return studentService.getAllStudentIds();
    }

    protected AbstractStudentRoleService getServiceByRole(String studentId){
        if(studentService.isPresenter(studentId)) return presenterService;
        else if(studentService.isReviewer(studentId)) return reviewerService;
        else throw new IllegalArgumentException("無法辨識學生身分: " + studentId);
    }

    private void initGradeVariables() {
        presenterGradeMap.clear();
        reviewerGradeMap.clear();
        reviewerZScoreMap.clear();
        allGradesByPresenter.clear();
        presenterAvgGrade = 0;
        stdDev = 0;
    }

    private void saveGroupedGrades(Map<AbstractStudentRoleService, List<GradeInput>> grouped) {
        for (Map.Entry<AbstractStudentRoleService, List<GradeInput>> entry : grouped.entrySet()) {
            entry.getKey().saveAllGrades(entry.getValue());
        }
    }

    private void validateStudents(Set<String> allStudentIds, Set<String> gradedStudentIds) {
        if(allStudentIds.isEmpty()) {
            throw new InvalidSaveGradeException("請先登記完整學生名單再登記成績");
        }

        if (!allStudentIds.containsAll(gradedStudentIds)) {
            Set<String> missing = new HashSet<>(gradedStudentIds);
            missing.removeAll(allStudentIds);
            missing.remove(presenterId);

            if (!missing.isEmpty()) {
                throw new IllegalArgumentException("評分者為不存在課程名單的學號，Invalid student IDs: " + missing);
            }
        }
    }

    private double assignGradesByParticipation(AbstractStudentRoleService roleService, Map<String, Double> grades, Set<String> gradedStudentIds, String studentId) {
        boolean graded = gradedStudentIds.contains(studentId);
        return graded ? grades.get(studentId) : roleService.getBasicGrade();
    }

    private void setGroupsByRoleService(AbstractStudentRoleService roleService, String week, double grade, String studentId, Map<AbstractStudentRoleService, List<GradeInput>> grouped, String currentPresenterId) {
        if (roleService instanceof ReviewerService) {
            // 如果是 reviewer 就要補上 presenter id
            grouped.computeIfAbsent(roleService, k -> new ArrayList<>())
                    .add(new GradeInput(studentId, currentPresenterId, week, grade));

        } else if (roleService instanceof PresenterService) {
            // 記錄 presenter 成績時，studentId 就是 presenter id 了
            grouped.computeIfAbsent(roleService, k -> new ArrayList<>())
                    .add(new GradeInput(studentId, null, week, grade));
        } else {
            throw new IllegalStateException("未知角色：" + roleService.getClass());
        }
    }

    private void groupSingleStudentToRole(
            String studentId, String week, Map<String, Double> grades,
            Set<String> gradedStudentIds, Map<AbstractStudentRoleService, List<GradeInput>> grouped, String currentPresenterId
    ) {
        AbstractStudentRoleService roleService = getServiceByRole(studentId);
        if(roleService instanceof ReviewerService)  {
            double grade = assignGradesByParticipation(roleService, grades, gradedStudentIds, studentId);
            setGroupsByRoleService(roleService, week, grade, studentId, grouped, currentPresenterId);
        }
        setGroupsByRoleService(roleService, week, grades.get(this.presenterId), this.presenterId, grouped, null);
    }
}
