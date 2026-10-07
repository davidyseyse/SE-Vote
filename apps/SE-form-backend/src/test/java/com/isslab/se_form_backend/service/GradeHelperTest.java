//package com.isslab.se_form_backend.service;
//
//import com.isslab.se_form_backend.service.impl.GradeHelper;
//import org.junit.jupiter.api.Assertions;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.TestInstance;
//
//import java.util.Map;
//
//@TestInstance(TestInstance.Lifecycle.PER_CLASS)
//public class GradeHelperTest {
//
//    private static final Map<String, Double> SCORE_MAP = Map.of(
//            "A", 90.0,
//            "B", 80.0,
//            "C", 70.0,
//            "F", 50.0
//    );
//
//    @Test
//    public void testGetGrade() {
//        double gap = GradeHelper.getGradeGap(SCORE_MAP);
//        Assertions.assertEquals(40.0, gap);
//    }
//}
