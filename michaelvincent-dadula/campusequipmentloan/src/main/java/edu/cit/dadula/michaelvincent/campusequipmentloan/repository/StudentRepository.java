package edu.cit.dadula.michaelvincent.campusequipmentloan.repository;

import edu.cit.dadula.michaelvincent.campusequipmentloan.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StudentRepository extends JpaRepository<Student, Long> {
}