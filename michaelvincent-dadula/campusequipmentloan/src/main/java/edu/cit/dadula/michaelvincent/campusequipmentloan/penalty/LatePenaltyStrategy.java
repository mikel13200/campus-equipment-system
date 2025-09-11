package edu.cit.dadula.michaelvincent.campusequipmentloan.penalty;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class LatePenaltyStrategy implements PenaltyStrategy {
    private static final int PENALTY_PER_DAY = 50;

    @Override
    public int calculatePenalty(LocalDate dueDate, LocalDate returnDate) {
        if (returnDate == null || !returnDate.isAfter(dueDate)) return 0;
        long daysLate = ChronoUnit.DAYS.between(dueDate, returnDate);
        return (int) daysLate * PENALTY_PER_DAY;
    }
}