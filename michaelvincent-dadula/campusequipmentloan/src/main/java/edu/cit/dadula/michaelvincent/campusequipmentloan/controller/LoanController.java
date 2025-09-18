package edu.cit.dadula.michaelvincent.campusequipmentloan.controller;

import edu.cit.dadula.michaelvincent.campusequipmentloan.model.Loan;
import edu.cit.dadula.michaelvincent.campusequipmentloan.service.LoanService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/loans")
public class LoanController {

    private final LoanService loanService;

    public LoanController(LoanService loanService) {
        this.loanService = loanService;
    }

    // GET all loans
    @GetMapping
    public List<Loan> getAllLoans() {
        return loanService.getAllLoans();
    }

    // GET loan by ID
    @GetMapping("/{id}")
    public ResponseEntity<Loan> getLoanById(@PathVariable Long id) {
        return loanService.getLoanById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // CREATE new loan
    @PostMapping
    public ResponseEntity<?> createLoan(@RequestBody Loan loan) {
        try {
            Loan savedLoan = loanService.createLoan(loan);
            return ResponseEntity.ok(savedLoan);
        } catch (IllegalStateException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // RETURN loan
    @PostMapping("/{id}/return")
    public ResponseEntity<?> returnLoan(@PathVariable Long id) {
        try {
            Loan returnedLoan = loanService.returnLoan(id);
            return ResponseEntity.ok(returnedLoan);
        } catch (IllegalStateException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // UPDATE loan
    @PutMapping("/{id}")
    public ResponseEntity<?> updateLoan(@PathVariable Long id, @RequestBody Loan loanDetails) {
        try {
            Loan updatedLoan = loanService.updateLoan(id, loanDetails);
            return ResponseEntity.ok(updatedLoan);
        } catch (IllegalStateException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // DELETE loan
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteLoan(@PathVariable Long id) {
        try {
            loanService.deleteLoan(id);
            return ResponseEntity.noContent().build();
        } catch (IllegalStateException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
