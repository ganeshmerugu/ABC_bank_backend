package com.ABC.Bank.CsvTODb;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;

import java.io.*;
import java.util.Iterator;
import java.util.Optional;

@Service

public class BureauDataServiceImpl implements BureauDataService  {
    private final BureauDataRepository bureauDataRepository;

    @Autowired
    public BureauDataServiceImpl(BureauDataRepository bureauDataRepository) {
        this.bureauDataRepository = bureauDataRepository;
    }

    @Override
    public String saveBureauDataRepository() {


        try {
            FileInputStream fis = new FileInputStream(new File("C:\\Users\\GaneshMerugu\\temp\\BureauData.xlsx"));
            XSSFWorkbook xssfWorkbook = new XSSFWorkbook(fis);
            XSSFSheet sheet = xssfWorkbook.getSheet("BureauData");
            int numberOfRows = sheet.getLastRowNum();
            System.out.println(numberOfRows);

            int rowNumber = 0;
            for(int i=0;i<=numberOfRows;i++) {
                System.out.println("i here =====> "+i);
                Row row = sheet.getRow(i);
                Iterator<Cell> cellsInRow = row.iterator();

                BureauData bureauData = new BureauData();

                int cellIdx = 0;
                while (cellsInRow.hasNext()) {
                    Cell currentCell = cellsInRow.next();

                    switch (cellIdx) {
                        case 0:
                            bureauData.setExistingSSNNumber(currentCell.getNumericCellValue());
                            break;

                        case 1:
                            bureauData.setDelinq2Yrs(currentCell.getNumericCellValue());
                            break;

                        case 2:
                            bureauData.setInqLast6Mths(currentCell.getNumericCellValue());
                            break;

                        case 3:
                            bureauData.setMthsSinceLastDelinq(currentCell.getNumericCellValue());
                            break;
                        case 4:
                            bureauData.setMthsSinceLastRecord(currentCell.getNumericCellValue());
                            break;
                        case 5:
                            bureauData.setOpenAcc(currentCell.getNumericCellValue());
                            break;
                        case 6:
                            bureauData.setPubRec(currentCell.getNumericCellValue());
                            break;
                        case 7:
                            bureauData.setRevolBal(currentCell.getNumericCellValue());
                            break;
                        case 8:
                            bureauData.setRevolUtil(currentCell.getNumericCellValue());
                            break;
                        case 9:
                            bureauData.setTotalAcc(currentCell.getNumericCellValue());
                            break;
                        case 10:
                            bureauData.setEarliestCrLine(currentCell.getStringCellValue());
                            break;
                        default:
                            break;
                    }
                    cellIdx++;
                }
                bureauDataRepository.save(bureauData);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "Sucess";
    }
    @Override
    public Optional<BureauData> getBureauDataById(double existingSSNNumber) {
        return bureauDataRepository.findByExistingSSNNumber(existingSSNNumber);
    }
    


}


