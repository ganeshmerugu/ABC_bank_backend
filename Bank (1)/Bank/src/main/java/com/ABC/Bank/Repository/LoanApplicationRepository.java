package com.ABC.Bank.Repository;

import com.ABC.Bank.Entity.LoanApplication;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository

public interface LoanApplicationRepository extends JpaRepository<LoanApplication, Long> {


    Optional<LoanApplication> findById(long applicationId);
}
