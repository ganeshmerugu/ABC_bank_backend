package com.ABC.Bank.CsvTODb;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.lang.NonNull;

import java.util.Date;

@Entity

@Data
@AllArgsConstructor
@NoArgsConstructor

public class BureauData {
    @Id

    @GeneratedValue(strategy = GenerationType.AUTO)
    private long idBureauData;

    private double existingSSNNumber;
    private double delinq2Yrs;
    private double inqLast6Mths;
    private double mthsSinceLastDelinq;
    private double mthsSinceLastRecord;
    private double openAcc;
    private double pubRec;
    private double revolBal;
    private double revolUtil;
    private double totalAcc;
    private String earliestCrLine;



}