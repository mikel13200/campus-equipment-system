package edu.cit.dadula.michaelvincent.campusequipmentloan.penalty;

import java.time.LocalDate;

public interface PenaltyStrategy {
    int calculatePenalty(LocalDate dueDate, LocalDate returnDate);
}