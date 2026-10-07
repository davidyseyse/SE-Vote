package com.isslab.se_form_backend.helper;

import com.isslab.se_form_backend.entity.FormScoreRecordEntity;
import com.isslab.se_form_backend.entity.FormSubmissionEntity;
import com.isslab.se_form_backend.repository.FormSubmissionRepository;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class FormSubmissionImporter {

    private final FormSubmissionRepository repository;

    public FormSubmissionImporter(FormSubmissionRepository repository) {
        this.repository = repository;
    }

    public List<FormSubmissionEntity> importSubmissionCsv(InputStream inputStream) throws IOException {
        List<FormSubmissionEntity> result = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            CSVParser csvParser = new CSVParser(reader, CSVFormat.DEFAULT)) {

            for (CSVRecord record : csvParser) {
                FormSubmissionEntity entity = new FormSubmissionEntity();
                entity.setSubmitterId(record.get(0));
                entity.setWeek(record.get(1));
                entity.setSubmitDateTime(record.get(2));
                entity.setComment(record.get(3));

                repository.save(entity);
                result.add(entity);
            }
        }

        return result;
    }

}

