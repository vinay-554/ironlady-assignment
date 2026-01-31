package com.ironlady.leadmanagement.controller;

import com.ironlady.leadmanagement.model.Lead;
import com.ironlady.leadmanagement.repository.LeadRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/leads")
@CrossOrigin
public class LeadController {

    @Autowired
    private LeadRepository leadRepository;

    // CREATE
    @PostMapping
    public Lead createLead(@RequestBody Lead lead) {
        lead.setStatus("NEW");
        return leadRepository.save(lead);
    }

    // READ
    @GetMapping
    public List<Lead> getAllLeads() {
        return leadRepository.findAll();
    }

    // UPDATE (only status)
    @PutMapping("/{id}")
    public Lead updateLeadStatus(@PathVariable Long id, @RequestBody Lead lead) {
        Lead existingLead = leadRepository.findById(id).orElseThrow();
        existingLead.setStatus(lead.getStatus());
        return leadRepository.save(existingLead);
    }

    // DELETE
    @DeleteMapping("/{id}")
    public void deleteLead(@PathVariable Long id) {
        leadRepository.deleteById(id);
    }
}
