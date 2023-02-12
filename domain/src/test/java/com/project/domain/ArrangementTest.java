package com.project.domain;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ArrangementTest {

    @Test
    void testGetDatesBetween() {
        Arrangement arrangement = new Arrangement();
        arrangement.setFrom_date(LocalDate.of(2022, 1, 1));
        arrangement.setTo_date(LocalDate.of(2022, 1, 5));

        List<LocalDate> expectedDates = Arrays.asList(
                LocalDate.of(2022, 1, 2),
                LocalDate.of(2022, 1, 3),
                LocalDate.of(2022, 1, 4)
        );
        List<LocalDate> actualDates = arrangement.getDatesBetween();

        assertEquals(expectedDates, actualDates);
    }

}