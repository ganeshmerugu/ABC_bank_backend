package com.ABC.Bank.CsvTODb;

import java.util.Optional;

public interface BureauDataService {
    String saveBureauDataRepository();

    Optional<BureauData> getBureauDataById(double existingSSNNumber);
}
