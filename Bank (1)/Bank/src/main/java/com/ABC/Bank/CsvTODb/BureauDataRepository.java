package com.ABC.Bank.CsvTODb;
import com.ABC.Bank.Entity.LoanApplication;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository

public interface  BureauDataRepository extends CrudRepository<BureauData,Double> {
    Optional<BureauData> findByExistingSSNNumber(double existingSSNNumber);


}
