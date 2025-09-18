package edu.cit.dadula.michaelvincent.campusequipmentloan.service;

import edu.cit.dadula.michaelvincent.campusequipmentloan.model.Loan;
import edu.cit.dadula.michaelvincent.campusequipmentloan.model.Equipment;
import edu.cit.dadula.michaelvincent.campusequipmentloan.model.Student;
import edu.cit.dadula.michaelvincent.campusequipmentloan.repository.LoanRepository;
import edu.cit.dadula.michaelvincent.campusequipmentloan.repository.EquipmentRepository;
import edu.cit.dadula.michaelvincent.campusequipmentloan.repository.StudentRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.HashMap;

@Service
public class LoanService {

    private final LoanRepository loanRepository;
    private final EquipmentRepository equipmentRepository;
    private final StudentRepository studentRepository;

    public LoanService(LoanRepository loanRepository,
                       EquipmentRepository equipmentRepository,
                       StudentRepository studentRepository) {
        this.loanRepository = loanRepository;
        this.equipmentRepository = equipmentRepository;
        this.studentRepository = studentRepository;
    }

    // GET all loans
    public List<Loan> getAllLoans() {
        return loanRepository.findAll();
    }

    // GET loan by ID
    public Optional<Loan> getLoanById(Long id) {
        return loanRepository.findById(id);
    }

    // CREATE new loan
    public Loan createLoan(Loan loan) {
        // Fetch full Equipment entity
        Equipment eq = equipmentRepository.findById(loan.getEquipment().getId())
                .orElseThrow(() -> new IllegalStateException("Equipment not found"));

        if (!eq.isAvailability()) throw new IllegalStateException("Equipment not available");

        // Fetch full Student entity
        Student student = studentRepository.findById(loan.getStudent().getId())
                .orElseThrow(() -> new IllegalStateException("Student not found"));

        // Check active loans
        long activeLoans = loanRepository.countByStudent_IdAndStatus(student.getId(), "ACTIVE");
        if (activeLoans >= 2) throw new IllegalStateException("Student already has 2 active loans");

        // Assign fetched entities
        loan.setEquipment(eq);
        loan.setStudent(student);
        loan.setStartDate(LocalDate.now());
        loan.setDueDate(LocalDate.now().plusDays(7));
        loan.setStatus("ACTIVE");

        // Mark equipment unavailable
        eq.setAvailability(false);
        equipmentRepository.save(eq);

        return loanRepository.save(loan);
    }

    // RETURN loan
    public Loan returnLoan(Long loanId) {
        return loanRepository.findById(loanId).map(loan -> {
            if (loan.getReturnDate() != null) throw new IllegalStateException("Loan already returned");

            loan.setReturnDate(LocalDate.now());
            loan.setStatus("RETURNED");

            // Make equipment available
            Equipment eq = loan.getEquipment();
            eq.setAvailability(true);
            equipmentRepository.save(eq);

            return loanRepository.save(loan);
        }).orElseThrow(() -> new IllegalStateException("Loan not found"));
    }

    // UPDATE loan
    public Loan updateLoan(Long id, Loan loanDetails) {
        return loanRepository.findById(id).map(loan -> {
            // Fetch full Equipment entity
            Equipment eq = equipmentRepository.findById(loanDetails.getEquipment().getId())
                    .orElseThrow(() -> new IllegalStateException("Equipment not found"));

            // Fetch full Student entity
            Student student = studentRepository.findById(loanDetails.getStudent().getId())
                    .orElseThrow(() -> new IllegalStateException("Student not found"));

            loan.setEquipment(eq);
            loan.setStudent(student);
            loan.setStartDate(loanDetails.getStartDate());
            loan.setDueDate(loanDetails.getDueDate());
            loan.setReturnDate(loanDetails.getReturnDate());
            loan.setStatus(loanDetails.getStatus());

            return loanRepository.save(loan);
        }).orElseThrow(() -> new IllegalStateException("Loan not found"));
    }

    // DELETE loan
    public void deleteLoan(Long id) {
        if (!loanRepository.existsById(id)) throw new IllegalStateException("Loan not found");
        loanRepository.deleteById(id);
    }

    // GET overdue loans + penalties for a student
    public Map<String,Object> getStudentOverdueLoans(Long studentId) {
        List<Loan> loans = loanRepository.findByStudent_Id(studentId);
        List<Map<String,Object>> overdueList = loans.stream()
                .filter(Loan::isOverdue)
                .map(loan -> {
                    Map<String,Object> map = new HashMap<>();
                    map.put("loanId", loan.getId());
                    map.put("equipmentName", loan.getEquipment().getName());
                    map.put("dueDate", loan.getDueDate());
                    map.put("daysOverdue", java.time.temporal.ChronoUnit.DAYS.between(loan.getDueDate(), LocalDate.now()));
                    map.put("penalty", loan.calculatePenalty());
                    return map;
                }).collect(Collectors.toList());

        long totalPenalty = overdueList.stream()
                .mapToLong(l -> (Long) l.get("penalty"))
                .sum();

        return Map.of("studentId", studentId, "totalPenalty", totalPenalty, "overdueLoans", overdueList);
    }
}
