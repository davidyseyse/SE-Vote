package com.isslab.se_form_backend.service.impl;

import com.isslab.se_form_backend.entity.PresenterGradeEntity;
import com.isslab.se_form_backend.helper.exception.InvalidGradeCalculationException;
import com.isslab.se_form_backend.model.GradeInput;
import com.isslab.se_form_backend.model.Presenter;
import com.isslab.se_form_backend.model.PresenterGrade;
import com.isslab.se_form_backend.repository.PresenterRepository;
import com.isslab.se_form_backend.service.AbstractStudentRoleService;
import com.isslab.se_form_backend.service.AbstractStudentService;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.*;

@Slf4j
public class PresenterService extends AbstractStudentRoleService {

    private final PresenterRepository repository;
    private final AbstractStudentService studentService;
    private static final double BASIC_GRADE = 0.0;

    @Value("${date.semesterStartDay}")
    private String SEMESTER_START_DAY;

    public PresenterService(PresenterRepository repository, AbstractStudentService studentService) {
        this.repository = repository;
        this.studentService = studentService;
    }

    @Override
    @Transactional
    public void saveAllGrades(List<GradeInput> gradeList) {
        // Step 1: 查出所有對應的 existing entries
        List<String> ids = gradeList.stream().map(GradeInput::getStudentId).toList();
        List<String> weeks = gradeList.stream().map(GradeInput::getWeek).toList();
        List<PresenterGradeEntity> existingEntities = repository.findAllByPresenterIdInAndWeekIn(ids, weeks);

        // Step 2: 建立查詢對照表
        Map<String, PresenterGradeEntity> existingMap = buildExistingMap(existingEntities);

        // Step 3: 建立更新清單
        List<PresenterGradeEntity> toSave = listAllPresentersToSaveGrade(gradeList, existingMap);

        repository.saveAll(toSave);
    }


    @Override
    public void saveGradeToPresenter(String studentId, String week, double grade) {
        PresenterGradeEntity presenterGradeEntity = getPresenterEntityByStudentIdAndWeek(studentId, week);
        presenterGradeEntity.setGrade(grade);

        repository.save(presenterGradeEntity);
    }

    @Override
    public void saveGradeToReviewer(String reviewerId, String presenterId, String week, double grade) {
        throw new UnsupportedOperationException("This role does not support saving reviewer grades.");
    }

    @Override
    public List<Double> getGradesByIdAndWeek(String studentId, String week) {
        return repository.getGradesByPresenterIdAndWeek(studentId, week);
    }

    @Override
    public void deleteGradeByIdAndWeek(String studentId, String week) {
        repository.deleteByPresenterIdAndWeek(studentId, week);
    }

    @Override
    public double getBasicGrade() {
        return BASIC_GRADE;
    }

    public Set<String> getPresenterIdsByWeek(String week) {
        return repository.getPresenterIdsByWeek(week);
    }

    public String getPresenterIdByWeekAndOrder(String week, int order) {
        PresenterGradeEntity presenter = repository.getByWeekAndPresentOrder(week, order);

        if(presenter == null){
            throw new InvalidGradeCalculationException("請先記錄 Presenter 再計算成績");
        }

        return presenter.getPresenterId();
    }

    public void addPresenter(Presenter presenter) {
        PresenterGradeEntity presenterGradeEntity = PresenterGradeEntity.builder()
                .presenterId(presenter.getPresenterId())
                .presentOrder(Integer.parseInt(presenter.getPresentOrder()))
                .week(presenter.getPresentWeek())
                .participate(true)
                .build();

        repository.save(presenterGradeEntity);
    }

    public void addPresentersByWeek(List<Presenter> presenters){
        for(Presenter presenter : presenters){
            addPresenter(presenter);
        }
    }

    public List<Presenter> getPresentersByWeek(String week) {
        List<PresenterGradeEntity> entities = repository.findAllByWeek(week);
        List<Presenter> presenters = new ArrayList<>();
        for(PresenterGradeEntity e : entities){
            String name = studentService.getNameByStudentId(e.getPresenterId());

            Presenter presenter = Presenter.builder()
                    .presenterId(e.getPresenterId())
                    .presentOrder(String.valueOf((e.getPresentOrder())))
                    .presentWeek(week)
                    .presenterName(name)
                    .participate(e.isParticipate())
                    .build();

            presenters.add(presenter);
        }

        return presenters;
    }

    public List<PresenterGrade> getPresenterGradesByWeek(String week) {
        List<PresenterGradeEntity> entities = repository.findAllByWeek(week);
        List<PresenterGrade> presenterGrades = new ArrayList<>();
        for(PresenterGradeEntity e : entities){
            PresenterGrade presenterGrade = PresenterGrade.builder()
                    .presenterId(e.getPresenterId())
                    .grade(e.getGrade())
                    .build();

            presenterGrades.add(presenterGrade);
        }

        return presenterGrades;
    }

    public String getCurrentWeek() {
        String startDateStr = SEMESTER_START_DAY;
        LocalDate today = LocalDate.now();
        LocalDate start = LocalDate.parse(startDateStr);

        long diffDays = ChronoUnit.DAYS.between(start, today);
        if (diffDays < 0) {
            return "1";
        }

        long week = diffDays / 7 + 1;
        return String.valueOf(week);
    }

    public boolean checkParticipated(String presenterId, String week) {
        PresenterGradeEntity presenter = getPresenterEntityByStudentIdAndWeek(presenterId, week);
        return presenter.isParticipate();
    }

    public void setParticipate(Boolean participate, String presenterId, String week) {
        PresenterGradeEntity presenter = getPresenterEntityByStudentIdAndWeek(presenterId, week);
        presenter.setParticipate(participate);
        repository.save(presenter);
    }

    public Set<Presenter> checkPresent(String week) {
        List<Presenter> presenters = getPresentersByWeek(week);

        Set<Presenter> absentPresenters = new HashSet<>();

        for(Presenter presenter : presenters) {
            if(!presenter.isParticipate()) {
                absentPresenters.add(presenter);
            }
        }

        return absentPresenters;
    }

    private PresenterGradeEntity getPresenterEntityByStudentIdAndWeek(String studentId, String week) {
        return repository.getByPresenterIdAndWeek(studentId, week);
    }

    private Map<String, PresenterGradeEntity> buildExistingMap(List<PresenterGradeEntity> existingEntities) {
        Map<String, PresenterGradeEntity> existingMap = new HashMap<>();
        for (PresenterGradeEntity entity : existingEntities) {
            String key = entity.getPresenterId() + "-" + entity.getWeek();
            existingMap.put(key, entity);
        }

        return existingMap;
    }

    private List<PresenterGradeEntity> listAllPresentersToSaveGrade(List<GradeInput> gradeList, Map<String, PresenterGradeEntity> existingMap) {
        List<PresenterGradeEntity> toSave = new ArrayList<>();

        for (GradeInput g : gradeList) {
            PresenterGradeEntity presenterGradeEntity = handlePresenterByExistingStatus(g, existingMap);
            if(presenterGradeEntity != null) toSave.add(presenterGradeEntity);
        }

        return toSave;
    }

    private PresenterGradeEntity handlePresenterByExistingStatus(GradeInput g, Map<String, PresenterGradeEntity> existingMap) {
        String key = g.getStudentId() + "-" + g.getWeek();

        if (existingMap.containsKey(key)) {
            PresenterGradeEntity entity = existingMap.get(key);
            entity.setGrade(g.getGrade()); // 更新 grade
            return entity;
        } else {
            log.info("報告者 {} 目前不需紀錄成績", g.getStudentId());
            return null;
        }
    }
}
