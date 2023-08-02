package com.ABC.Bank.Service;

import com.ABC.Bank.CsvTODb.BureauData;
import com.ABC.Bank.Data.ViewLoanApplicationsDTO;
import com.ABC.Bank.Entity.LoanApplication;

import java.util.List;
import java.util.Optional;
import java.util.UUID;


public interface LoanApplicationService {
    public LoanApplication saveApplication(LoanApplication loanApplication);

    public List<LoanApplication> getAllLoanApplications();

//    Optional<LoanApplication> getAllLoanApplicationsByUserName(String userName);


    Optional<LoanApplication> getLoanApplicationById(long applicationId);

    List<ViewLoanApplicationsDTO> getAllLoanApplicationsInfo();

    Optional<BureauData> getBureauDataById(double existingSSNNumber);


    double CreditScore(long applicationId, double existingSSNNumber);

    String newUserCheckStatus(long applicationId);

    String checkReasonForNewUser(long applicationId);

    double newUserCreditScore(long applicationId);

    String checkStatus(long applicationId, double existingSSNNumber);

    String checkReasonForExistingUser(long applicationId, double existingSSNNumber);
}
