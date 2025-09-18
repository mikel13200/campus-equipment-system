package edu.cit.dadula.michaelvincent.campusequipmentloan.repository;

import edu.cit.dadula.michaelvincent.campusequipmentloan.model.Equipment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;

public interface EquipmentRepository extends JpaRepository<Equipment, Long> {

    List<Equipment> findByAvailabilityTrue();

    @Query("SELECT e FROM Equipment e WHERE e.id NOT IN " +
            "(SELECT l.equipment.id FROM Loan l WHERE l.status = 'ACTIVE')")
    List<Equipment> findAvailableEquipment();
}
