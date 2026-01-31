package com.ironlady.leadmanagement.repository;

import com.ironlady.leadmanagement.model.Lead;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LeadRepository extends JpaRepository<Lead, Long> {
}
