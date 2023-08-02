package com.ABC.Bank.Controller;

import com.ABC.Bank.CsvTODb.BureauData;
import com.ABC.Bank.CsvTODb.BureauDataService;
import com.ABC.Bank.Data.ViewLoanApplicationsDTO;
import com.ABC.Bank.Entity.LoanApplication;
import com.ABC.Bank.Service.LoanApplicationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/loanApplicationForm")

public class LoanApplicationController {

    private LoanApplicationService loanApplicationService;
    private BureauDataService bureauDataService;

    @Autowired
    public LoanApplicationController(LoanApplicationService  loanApplicationService,BureauDataService bureauDataService){
        this.loanApplicationService=loanApplicationService;
        this.bureauDataService=bureauDataService;
    }
@PostMapping("")
public ResponseEntity<LoanApplication> submitLoanApplication(@RequestBody @Validated LoanApplication loanApplication) {
    // Call the service layer to submit the loan application
    LoanApplication submittedApplication = loanApplicationService.saveApplication(loanApplication);
    return new ResponseEntity<>(submittedApplication, HttpStatus.CREATED);
}

    @GetMapping("viewAllApplications")
    @CrossOrigin(origins = "http://localhost:4200/viewapplications")
    public ResponseEntity<List<LoanApplication>> getAllLoanApplications() {
        List<LoanApplication> allApplications = loanApplicationService.getAllLoanApplications();
        if (allApplications.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT); // If no applications found, return 204 No Content.
        }
        return new ResponseEntity<>(allApplications, HttpStatus.OK);

    }


    @GetMapping("/viewApplications")
    @CrossOrigin(origins = "http://localhost:4200/preview/loanapplication?applicationId={applicationId}&ssnNumber={ssnNumber}")
    public ResponseEntity<LoanApplication> getLoanApplicationById(@RequestParam("applicationId") long applicationId, @RequestParam("ssnNumber") double ssnNumber) {
        Optional<LoanApplication> application = loanApplicationService.getLoanApplicationById(applicationId);
        LoanApplication loanApplication= application.get();
        Optional<BureauData> bureauData = loanApplicationService.getBureauDataById(ssnNumber);
        BureauData bureauDataBySSNNumber = bureauData.isPresent() ? bureauData.get() : null;

        if ((bureauDataBySSNNumber!=null)&& (Integer.parseInt(loanApplication.getSsnNumber()) == Double.valueOf(bureauDataBySSNNumber.getExistingSSNNumber()).intValue())) {
            double existingUserScore = loanApplicationService.CreditScore(applicationId,ssnNumber);
            String existingUserStatus = loanApplicationService.checkStatus(applicationId,ssnNumber);
            String existingUserReason = loanApplicationService.checkReasonForExistingUser(applicationId,ssnNumber);
            LoanApplication existingUser = application.get();
            existingUser.setScore((int) existingUserScore);
            existingUser.setStatus(existingUserStatus);
            existingUser.setDeclineReasons(existingUserReason);

        }
        else  {
            double newUserScore = loanApplicationService.newUserCreditScore(applicationId);
            String newUserStatus = loanApplicationService.newUserCheckStatus(applicationId);
            String newUserReason = loanApplicationService.checkReasonForNewUser(applicationId);

            LoanApplication newUSerApplication = application.get();
            newUSerApplication.setScore((int) newUserScore);
            newUSerApplication.setStatus(newUserStatus);
            newUSerApplication.setDeclineReasons(newUserReason);
        }


        return application.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }


//     @GetMapping("{userName}")
//    public ResponseEntity<LoanApplication> getLoanApplicationByUserName(@PathVariable String userName){
//        Optional<LoanApplication> application= loanApplicationService.getAllLoanApplicationsByUserName(userName);
//        return application.map(ResponseEntity::ok)
//                .orElse(ResponseEntity.notFound().build());
//     }


    @GetMapping("allApplicationsInfo")
    public ResponseEntity<List<ViewLoanApplicationsDTO>> getAllLoanApplicationsInfo() {
        List<ViewLoanApplicationsDTO> allApplicationsInfo = loanApplicationService.getAllLoanApplicationsInfo();
        if (allApplicationsInfo.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(allApplicationsInfo, HttpStatus.OK);
    }




}
