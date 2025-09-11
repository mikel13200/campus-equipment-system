package edu.cit.dadula.michaelvincent.campusequipmentloan.controller;

import edu.cit.dadula.michaelvincent.campusequipmentloan.model.Equipment;
import edu.cit.dadula.michaelvincent.campusequipmentloan.repository.EquipmentRepository;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/equipment")
public class EquipmentController {
    private final EquipmentRepository equipmentRepository;

    public EquipmentController(EquipmentRepository equipmentRepository) {
        this.equipmentRepository = equipmentRepository;
    }

    @GetMapping("/available")
    public List<Equipment> getAvailableEquipment() {
        return equipmentRepository.findByAvailableTrue();
    }

    @PostMapping
    public ResponseEntity<Equipment> addEquipment(@Valid @RequestBody Equipment equipment) {
        equipment.setAvailable(true); // Ensure new equipment is available by default
        Equipment saved = equipmentRepository.save(equipment);
        return ResponseEntity.ok(saved);
    }
}