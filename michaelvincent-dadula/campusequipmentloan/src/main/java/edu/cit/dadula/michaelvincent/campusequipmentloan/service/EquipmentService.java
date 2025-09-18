package edu.cit.dadula.michaelvincent.campusequipmentloan.service;

import edu.cit.dadula.michaelvincent.campusequipmentloan.model.Equipment;
import edu.cit.dadula.michaelvincent.campusequipmentloan.repository.EquipmentRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class EquipmentService {
    private final EquipmentRepository equipmentRepository;

    public EquipmentService(EquipmentRepository equipmentRepository) {
        this.equipmentRepository = equipmentRepository;
    }

    public List<Equipment> getAllEquipment() {
        return equipmentRepository.findAll();
    }

    public Equipment createEquipment(Equipment equipment) {
        return equipmentRepository.save(equipment);
    }

    public Equipment updateEquipment(Long id, Equipment updatedEquipment) {
        Equipment existing = equipmentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Equipment not found"));
        existing.setName(updatedEquipment.getName());
        existing.setType(updatedEquipment.getType());
        existing.setSerialNumber(updatedEquipment.getSerialNumber());
        existing.setAvailability(updatedEquipment.isAvailability());
        return equipmentRepository.save(existing);
    }

    public void deleteEquipment(Long id) {
        if (!equipmentRepository.existsById(id)) throw new RuntimeException("Equipment not found");
        equipmentRepository.deleteById(id);
    }

    public List<Equipment> getAvailableEquipment() {
        return equipmentRepository.findByAvailabilityTrue(); // returns only available
    }
}
