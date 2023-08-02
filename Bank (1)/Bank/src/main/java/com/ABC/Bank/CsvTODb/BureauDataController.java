package com.ABC.Bank.CsvTODb;


import com.ABC.Bank.Entity.LoanApplication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("")

public class BureauDataController {
    private final BureauDataService bureauDataService;
    @Autowired
    public BureauDataController(BureauDataService bureauDataService){
        this.bureauDataService=bureauDataService;
    }

    @PostMapping(path = "feedBureauData")
    public void setBureauDataInDB(@RequestBody String dummyData){
        bureauDataService.saveBureauDataRepository();
    }
    @GetMapping("BureauData/{existingSSNNumber}")
    @CrossOrigin(origins = "http://localhost:4200/preview/{applicationId}")
    public ResponseEntity<BureauData> getLoanApplicationById(@PathVariable double existingSSNNumber ) {
        Optional<BureauData> application = bureauDataService.getBureauDataById(existingSSNNumber);


        return application.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }


}
