package edu.cit.dadula.michaelvincent.campusequipmentloan.repository;

import edu.cit.dadula.michaelvincent.campusequipmentloan.model.Loan;
import edu.cit.dadula.michaelvincent.campusequipmentloan.model.Student;
import edu.cit.dadula.michaelvincent.campusequipmentloan.model.LoanStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface LoanRepository extends JpaRepository<Loan, Long> {
    long countByStudentAndStatus(Student student, LoanStatus status);
    List<Loan> findByStudentAndStatus(Student student, LoanStatus status);
}