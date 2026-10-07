package com.isslab.se_form_backend.helper;

import com.isslab.se_form_backend.entity.FormScoreRecordEntity;
import com.isslab.se_form_backend.entity.FormSubmissionEntity;
import com.isslab.se_form_backend.repository.FormScoreRecordRepository;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

public class FormScoreCsvImporter {

    private final FormScoreRecordRepository repository;

    public FormScoreCsvImporter(FormScoreRecordRepository repository) {
        this.repository = repository;
    }

    public void importScoreRecordCsv(InputStream inputStream, List<FormSubmissionEntity> submissions) throws IOException {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
             CSVParser csvParser = new CSVParser(reader, CSVFormat.DEFAULT)) {

            int submissionIndex = 0;

            for (CSVRecord record : csvParser) {
                if (submissionIndex >= submissions.size()) break; // 避免越界

                FormSubmissionEntity submission = submissions.get(submissionIndex);

                FormScoreRecordEntity entity = new FormScoreRecordEntity();
                entity.setFormId(submission.getId());
                entity.setPresenterId("112552008");
                entity.setReviewerId(record.get(0));
                entity.setScore(record.get(1));

                repository.save(entity);
                submissionIndex++;
            }

        } catch (IOException e) {
            throw new RuntimeException("CSV 匯入失敗", e);
        }
    }
}

