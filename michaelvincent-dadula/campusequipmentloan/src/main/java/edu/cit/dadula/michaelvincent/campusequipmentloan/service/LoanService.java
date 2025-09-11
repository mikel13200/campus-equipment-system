package edu.cit.dadula.michaelvincent.campusequipmentloan.service;

import edu.cit.dadula.michaelvincent.campusequipmentloan.model.*;
import edu.cit.dadula.michaelvincent.campusequipmentloan.repository.*;
import edu.cit.dadula.michaelvincent.campusequipmentloan.penalty.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Service
public class LoanService {
    private final LoanRepository loanRepository;
    private final EquipmentRepository equipmentRepository;
    private final StudentRepository studentRepository;
    private final PenaltyStrategy penaltyStrategy;

    public LoanService(LoanRepository loanRepository,
                       EquipmentRepository equipmentRepository,
                       StudentRepository studentRepository) {
        this.loanRepository = loanRepository;
        this.equipmentRepository = equipmentRepository;
        this.studentRepository = studentRepository;
        this.penaltyStrategy = new LatePenaltyStrategy();
    }

    @Transactional
    public Loan createLoan(Long equipmentId, String studentNo) {
        Equipment equipment = equipmentRepository.findById(equipmentId)
                .orElseThrow(() -> new IllegalArgumentException("Equipment not found"));
        if (!equipment.isAvailable()) throw new IllegalStateException("Equipment not available");

        Student student = studentRepository.findByStudentNo(studentNo)
                .orElseThrow(() -> new IllegalArgumentException("Student not found"));

        long activeLoans = loanRepository.countByStudentAndStatus(student, LoanStatus.ACTIVE);
        if (activeLoans >= 2)
            throw new IllegalStateException("Student has reached max active loans");

        LocalDate start = LocalDate.now();
        LocalDate due = start.plusDays(7);

        Loan loan = new Loan();
        loan.setEquipment(equipment);
        loan.setStudent(student);
        loan.setStartDate(start);
        loan.setDueDate(due);
        loan.setStatus(LoanStatus.ACTIVE);

        equipment.setAvailable(false);
        equipmentRepository.save(equipment);

        return loanRepository.save(loan);
    }

    @Transactional
    public int returnLoan(Long loanId) {
        Loan loan = loanRepository.findById(loanId)
                .orElseThrow(() -> new IllegalArgumentException("Loan not found"));

        if (loan.getStatus() != LoanStatus.ACTIVE && loan.getStatus() != LoanStatus.OVERDUE)
            throw new IllegalStateException("Loan is not active");

        LocalDate returnDate = LocalDate.now();
        loan.setReturnDate(returnDate);

        int penalty = penaltyStrategy.calculatePenalty(loan.getDueDate(), returnDate);

        if (returnDate.isAfter(loan.getDueDate())) {
            loan.setStatus(LoanStatus.OVERDUE);
        } else {
            loan.setStatus(LoanStatus.RETURNED);
        }

        Equipment equipment = loan.getEquipment();
        equipment.setAvailable(true);
        equipmentRepository.save(equipment);

        loanRepository.save(loan);

        return penalty;
    }
}