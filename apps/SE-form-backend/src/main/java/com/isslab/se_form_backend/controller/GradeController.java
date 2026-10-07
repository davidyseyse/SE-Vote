package com.isslab.se_form_backend.controller;

import com.isslab.se_form_backend.helper.ResponseStatus;
import com.isslab.se_form_backend.model.GradeInput;
import com.isslab.se_form_backend.model.SinglePresenterCalculationRequest;
import com.isslab.se_form_backend.model.Status;
import com.isslab.se_form_backend.model.UpdatePresenterGrade;
import com.isslab.se_form_backend.service.AbstractGradeService;
import com.isslab.se_form_backend.service.impl.PresenterService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Set;

@RestController
@RequestMapping("/api/grade")
@PreAuthorize("hasRole('ADMIN')")
public class GradeController {

    private final AbstractGradeService gradeService;
    private final PresenterService presenterService;

    public GradeController(AbstractGradeService gradeService, PresenterService presenterService) {
        this.gradeService = gradeService;
        this.presenterService = presenterService;
    }

//    @GetMapping("{id}/{week}")
//    public ResponseEntity<List<Double>> getGradeByIdAndWeek(@PathVariable String id, @PathVariable String week){
//        List<Double> grade = gradeService.getGradesByIdAndWeek(id, week);
//        return ResponseEntity.ok(grade);
//    }

    @PostMapping("/save")
    public ResponseEntity<Status> saveAllGradesByWeekAndPresenter(@RequestBody SinglePresenterCalculationRequest body){
        int presenterOrder = Integer.parseInt(body.getPresenterOrder());
        String week = body.getWeek();
        Map<String, Double> gradesMap = gradeService.calculateGradeBySinglePresenter(week, presenterOrder);
        gradeService.saveAllGradesByWeekAndPresenter(week, gradesMap);
        return ResponseEntity.ok(ResponseStatus.statusOk());
    }

    @PostMapping("/save/batch/{week}")
    public ResponseEntity<Status> saveAllGradesByWeek(@PathVariable String week){
        gradeService.calculateGradesByWeek(week);
        Set<String> presenterIds = presenterService.getPresenterIdsByWeek(week);
        gradeService.saveAllGradesByWeek(week, presenterIds);
        return ResponseEntity.ok(ResponseStatus.statusOk());
    }

    @PutMapping("/update/reviewer")
    public ResponseEntity<Status> setReviewerGradeByIdAndWeek(@RequestBody GradeInput body){
        gradeService.saveGradeToReviewer(body.getStudentId(), body.getPresenterId(), body.getWeek(), body.getGrade());
        return ResponseEntity.ok(ResponseStatus.statusOk());
    }

    @PutMapping("/update/presenter")
    public ResponseEntity<Status> setPresenterGradeByIdAndWeek(@RequestBody UpdatePresenterGrade body){
        gradeService.saveGradeToPresenter(body.getPresenterId(), body.getWeek(), body.getGrade());
        return ResponseEntity.ok(ResponseStatus.statusOk());
    }

//
//    @DeleteMapping("/delete")
//    public ResponseEntity<Status> deleteGradeByIdAndWeek(@RequestBody Map<String, String> body){
//        gradeService.deleteGradeByIdAndWeek(body.get("id"), body.get("week"));
//        return ResponseEntity.ok(ResponseStatus.statusOk());
//    }
}
