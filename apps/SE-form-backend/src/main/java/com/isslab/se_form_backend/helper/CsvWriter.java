package com.isslab.se_form_backend.helper;

import com.isslab.se_form_backend.model.FormScoreRecord;
import com.isslab.se_form_backend.model.FormSubmission;
import com.isslab.se_form_backend.model.PresenterGrade;
import com.isslab.se_form_backend.model.ReviewerGrade;
import lombok.extern.slf4j.Slf4j;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.time.LocalDateTime;


@Slf4j
public class CsvWriter {
    public static void writePresenterGrades(List<PresenterGrade> grades, String fileDir, String week) throws IOException {
        Path dir = Paths.get(fileDir, week);
        Files.createDirectories(dir);
        LocalDateTime now = LocalDateTime.now();
        String date = now.format(DateTimeFormatter.ofPattern("yyyyMMdd-HHmmss"));
        Path filePath = dir.resolve(String.format("presenter_%s.csv", date));

        try (BufferedWriter writer = Files.newBufferedWriter(
                filePath,
                StandardCharsets.UTF_8,
                StandardOpenOption.CREATE,
                StandardOpenOption.APPEND)) {

            for (PresenterGrade pg : grades) {
                writer.write(pg.getPresenterId() + "," + String.format("%.1f", pg.getGrade()));
                writer.newLine();
            }

            log.info("Presenter 成績 CSV 檔案已產生：{}", filePath);
        } catch (IOException e) {
            log.error("寫入 Presenter 成績 CSV 檔案失敗：{}", filePath, e);
        }
    }

    public static void writeReviewerGrades(List<ReviewerGrade> grades, String fileDir, String week) throws IOException {
        Path dir = Paths.get(fileDir, week);
        Files.createDirectories(dir);

        Map<String, List<String>> bucket = new HashMap<>();
        for (ReviewerGrade rg : grades) {
            String reviewerId = rg.getReviewerId();
            for (PresenterGrade pg : rg.getGrades()) {
                String presenterId = pg.getPresenterId();
                Double grade = pg.getGrade();
                bucket.computeIfAbsent(presenterId, k -> new ArrayList<>())
                        .add(String.format("%s,%.1f", reviewerId, grade));
            }
        }

        for (Map.Entry<String, List<String>> e : bucket.entrySet()) {
            String presenterId = e.getKey();
            List<String> lines = e.getValue();
            LocalDateTime now = LocalDateTime.now();
            String date = now.format(DateTimeFormatter.ofPattern("yyyyMMdd-HHmmss"));
            Path file = dir.resolve(String.format("reviewer_%s_%s.csv", presenterId, date));

            try (BufferedWriter w = Files.newBufferedWriter(
                    file, StandardCharsets.UTF_8,
                    StandardOpenOption.CREATE, StandardOpenOption.APPEND)) {
                for (String line : lines) {
                    w.write(line);
                    w.newLine();
                }
            }
            log.info("Reviewer 成績 CSV 產生：{}", file);
        }
    }

    public static void writeFormSubmissions(List<FormSubmission> submissions, String fileDir, String week) throws IOException {
        Path dir = Paths.get(fileDir, week);
        Files.createDirectories(dir);
        LocalDateTime now = LocalDateTime.now();
        String date = now.format(DateTimeFormatter.ofPattern("yyyyMMdd-HHmmss"));
        Path filePath = dir.resolve(String.format("form_submissions_%s.csv", date));

        try (BufferedWriter writer = Files.newBufferedWriter(
                filePath,
                StandardCharsets.UTF_8,
                StandardOpenOption.CREATE,
                StandardOpenOption.APPEND)) {

            writer.write("reviewerId,submitDateTime,comment");
            writer.newLine();

            for (FormSubmission submission : submissions) {
                writer.write(
                        submission.getSubmitterId() + "," +
                                submission.getSubmitDateTime() + "," +
                                submission.getComment()
                );
                writer.newLine();
            }

            log.info("Form submission CSV 檔案已產生：{}", filePath);
        } catch (IOException e) {
            log.error("寫入 Form submission CSV 檔案失敗：{}", filePath, e);
        }
    }

    public static void writeScoreRecords(List<FormSubmission> scoreRecords, String fileDir, String week) throws IOException {
        Path dir = Paths.get(fileDir, week);
        Files.createDirectories(dir);
        LocalDateTime now = LocalDateTime.now();
        String date = now.format(DateTimeFormatter.ofPattern("yyyyMMdd-HHmmss"));
        Path filePath = dir.resolve(String.format("score_records_%s.csv", date));

        try (BufferedWriter writer = Files.newBufferedWriter(
                filePath,
                StandardCharsets.UTF_8,
                StandardOpenOption.CREATE,
                StandardOpenOption.APPEND)) {
            writer.append("reviewerId, presenterId ,score");
            writer.newLine();

            for (FormSubmission scoreRecord : scoreRecords) {
                String reviewerId = scoreRecord.getSubmitterId();

                for(FormScoreRecord record : scoreRecord.getScores()) {
                    String presenterId = record.getPresenterId();
                    String score = record.getScore();

                    writer.write(reviewerId + "," + presenterId + "," + score);
                    writer.newLine();
                }
            }

            log.info("Score record CSV 檔案已產生：{}", filePath);
        } catch (IOException e) {
            log.error("寫入 Score record CSV 檔案失敗：{}", filePath, e);
        }
    }
}
