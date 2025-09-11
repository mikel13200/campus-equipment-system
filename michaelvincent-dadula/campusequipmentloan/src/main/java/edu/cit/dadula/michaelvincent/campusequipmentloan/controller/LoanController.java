package edu.cit.dadula.michaelvincent.campusequipmentloan.controller;

import edu.cit.dadula.michaelvincent.campusequipmentloan.model.Loan;
import edu.cit.dadula.michaelvincent.campusequipmentloan.service.LoanService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/loans")
public class LoanController {
    private final LoanService loanService;

    public LoanController(LoanService loanService) {
        this.loanService = loanService;
    }

    @PostMapping
    public ResponseEntity<Loan> createLoan(@RequestParam Long equipmentId, @RequestParam String studentNo) {
        Loan loan = loanService.createLoan(equipmentId, studentNo);
        return ResponseEntity.ok(loan);
    }

    @PostMapping("/{id}/return")
    public ResponseEntity<?> returnLoan(@PathVariable Long id) {
        int penalty = loanService.returnLoan(id);
        return ResponseEntity.ok().body("Penalty: ₱" + penalty);
    }
}