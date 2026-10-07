package com.isslab.se_form_backend.service.util;


import com.isslab.se_form_backend.entity.FormScoreRecordEntity;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

public class FormScoreRecordLoader {

    /**
     * 從 CSV 檔案載入測試資料
     * @param resourcePath 資源路徑，例如 "/test-data/score-records.csv"
     * @return FormScoreRecordEntity 列表
     */
    public static List<FormScoreRecordEntity> loadFromCsv(String resourcePath) throws IOException {
        List<FormScoreRecordEntity> records = new ArrayList<>();

        // 從類路徑資源中獲取CSV檔案
        InputStream inputStream = FormScoreRecordLoader.class.getResourceAsStream(resourcePath);
        if (inputStream == null) {
            throw new IOException("找不到資源: " + resourcePath);
        }

        try (Reader reader = new InputStreamReader(inputStream);
             CSVParser csvParser = new CSVParser(reader, CSVFormat.DEFAULT)) {

            for (CSVRecord csvRecord : csvParser) {
                String studentId = csvRecord.get(0);
                String score = csvRecord.get(1);

                records.add(new FormScoreRecordEntity(1L, 1L, score, studentId, "113500001"));
            }
        }

        return records;
    }

    /**
     * 獲取指定分數的記錄
     * @param records 所有記錄
     * @param score 指定分數 (A, B, 或 C)
     * @return 符合條件的記錄列表
     */
    public static List<FormScoreRecordEntity> filterByScore(List<FormScoreRecordEntity> records, String score) {
        List<FormScoreRecordEntity> filtered = new ArrayList<>();

        for (FormScoreRecordEntity record : records) {
            if (score.equals(record.getScore())) {
                filtered.add(record);
            }
        }

        return filtered;
    }
}
