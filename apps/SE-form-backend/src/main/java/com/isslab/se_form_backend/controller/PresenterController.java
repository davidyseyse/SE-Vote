package com.isslab.se_form_backend.controller;

import com.isslab.se_form_backend.helper.ResponseStatus;
import com.isslab.se_form_backend.model.AbsentPresentersResponse;
import com.isslab.se_form_backend.model.Presenter;
import com.isslab.se_form_backend.model.SetPresenterParticipateRequest;
import com.isslab.se_form_backend.service.impl.PresenterService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/api/presenter")
@PreAuthorize("hasRole('ADMIN')")
public class PresenterController {

    private final PresenterService presenterService;

    public PresenterController(PresenterService presenterService) {
        this.presenterService = presenterService;
    }

    @GetMapping("/")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<List<Presenter>> getPresenters() {
        String week = presenterService.getCurrentWeek();
        List<Presenter> presenters = presenterService.getPresentersByWeek(week);

        return ResponseEntity.ok(presenters);
    }

    @GetMapping("/{week}")
    public ResponseEntity<List<Presenter>> getPresentersByWeek(@PathVariable("week") String week) {
        List<Presenter> presenters = presenterService.getPresentersByWeek(week);

        return ResponseEntity.ok(presenters);
    }

    @GetMapping("/participant/{week}")
    public ResponseEntity<AbsentPresentersResponse> getParticipants(@PathVariable String week) {
        Set<Presenter> absentPresenters = presenterService.checkPresent(week);
        String msg = "本周有報告者未參加報告";

        if(absentPresenters.isEmpty()) {
            msg = "本周所有報告者皆參與報告";
        }

        return ResponseEntity.ok(new AbsentPresentersResponse(msg, absentPresenters));
    }

    @PostMapping("/")
    public ResponseEntity<Presenter> addPresenter(@RequestBody Presenter presenter) {
        presenterService.addPresenter(presenter);

        return ResponseEntity.ok(presenter);
    }

    @PostMapping("/batch")
    public ResponseEntity<List<Presenter>> addPresenterBatch(@RequestBody List<Presenter> presenters) {
        presenterService.addPresentersByWeek(presenters);

        return ResponseEntity.ok(presenters);
    }

    @PostMapping("/participants")
    public ResponseEntity<?> setParticipants(@RequestBody SetPresenterParticipateRequest request) {
        String presenterId = request.getPresenterId();
        String week = request.getWeek();
        Boolean participate = request.getParticipate();

        presenterService.setParticipate(participate, presenterId, week);

        return ResponseEntity.ok(ResponseStatus.statusOk());
    }

}
