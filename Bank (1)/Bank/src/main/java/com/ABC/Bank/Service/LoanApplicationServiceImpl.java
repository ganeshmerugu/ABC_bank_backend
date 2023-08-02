package com.ABC.Bank.Service;

import com.ABC.Bank.CsvTODb.BureauData;
import com.ABC.Bank.CsvTODb.BureauDataRepository;
import com.ABC.Bank.Data.ViewLoanApplicationsDTO;
import com.ABC.Bank.Entity.LoanApplication;
import com.ABC.Bank.Repository.LoanApplicationRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class LoanApplicationServiceImpl implements LoanApplicationService {
    private LoanApplicationRepository loanApplicationRepository;
    private BureauDataRepository bureauDataRepository;
    @Autowired
    public LoanApplicationServiceImpl(LoanApplicationRepository loanApplicationRepository,BureauDataRepository bureauDataRepository) {
        this.loanApplicationRepository = loanApplicationRepository;
        this.bureauDataRepository=bureauDataRepository;
    }

    @Override
    public LoanApplication saveApplication(LoanApplication loanApplication) {
        return loanApplicationRepository.save(loanApplication);
    }

    @Override
    public List<LoanApplication> getAllLoanApplications() {
        return loanApplicationRepository.findAll();
    }


    @Override
    public Optional<LoanApplication> getLoanApplicationById(long applicationId) {
        return loanApplicationRepository.findById(applicationId);
    }

    public List<ViewLoanApplicationsDTO> getAllLoanApplicationsInfo() {
        List<LoanApplication> loanApplications = loanApplicationRepository.findAll();

        // Convert LoanApplication to ViewLoanApplicationsDTO
        return loanApplications.stream()
                .map(application -> {
                    ViewLoanApplicationsDTO viewLoanApplicationsDTO = new ViewLoanApplicationsDTO();
                    BeanUtils.copyProperties(application, viewLoanApplicationsDTO);
                    return viewLoanApplicationsDTO;
                })
                .collect(Collectors.toList());
    }

    public double finalDescriptionRiskCoefficient(long applicationId) {
        Optional<LoanApplication> application = loanApplicationRepository.findById(applicationId);
        LoanApplication loanApplication = application.get();
        String description = loanApplication.getDescription();

        // Trimming the input string
        String trimmedDescription = description.trim().toLowerCase();

        // Elements to check for
        String[] elementsToCheck = {
                "bill", "card", "consolid", "credit", "current", "debt", "get", "help",
                "interest", "job", "loan", "make", "month", "monthly", "need", "one",
                "pay", "payment", "rate", "thank", "time", "use", "work", "would", "year"
        };

        // Variables to store the found string and flag
        String foundString = null;
        boolean isElementFound = false;

        // Check for elements in the input string
        for (String element : elementsToCheck) {
            if (trimmedDescription.contains(element)) {
                foundString = element;
                isElementFound = true;
                break; // Break the loop once an element is found
            }
        }


        // HashMap with key-value pairs
        Map<String, Double> elementMap = new HashMap<>();
        elementMap.put("bill", -0.229);
        elementMap.put("card", 0.0144);
        elementMap.put("consolid", -0.0334);
        elementMap.put("credit", 0.1844);
        elementMap.put("current", 0.0927);
        elementMap.put("debt", -0.0053);
        elementMap.put("get", 0.047);
        elementMap.put("help", -0.1862);
        elementMap.put("interest", 0.0293);
        elementMap.put("job", 0.0533);
        elementMap.put("loan", 0.0511);
        elementMap.put("make", -0.0073);
        elementMap.put("month", 0.0526);
        elementMap.put("monthly", -0.077);
        elementMap.put("need", -0.1143);
        elementMap.put("one", -0.1339);
        elementMap.put("pay", 0.0663);
        elementMap.put("payment", 0.0718);
        elementMap.put("rate", 0.0303);
        elementMap.put("thank", -0.1228);
        elementMap.put("time", 0.0234);
        elementMap.put("use", -0.1413);
        elementMap.put("work", -0.1397);
        elementMap.put("would", -0.1439);
        elementMap.put("year", 0.0535);

        double intercept = 1.789;
        double finalDescriptionRiskCoefficient = 0;

        if (isElementFound == true) {
            double descriptionRiskCoefficient = elementMap.get(foundString);
            finalDescriptionRiskCoefficient = intercept + descriptionRiskCoefficient;
        } else {
            finalDescriptionRiskCoefficient = intercept;
        }
        double value = 1/(1+Math.pow( Math.E,-finalDescriptionRiskCoefficient));
        System.out.println(value);
        return value;
    }
    public double purposeCoefficient(long applicationId) {
        double finalPurposeCoefficient=0;
        Optional<LoanApplication> application = loanApplicationRepository.findById(applicationId);
        LoanApplication loanApplication = application.get();

        Map<String, Double> elementMap = new HashMap<>();
        elementMap.put("personal", 0.1356);
        elementMap.put("home", 0.1157);
        elementMap.put("educational", 0.2213);
        elementMap.put("debt", 0.1367);
        for (String key : elementMap.keySet()) {
            if(key.equals(loanApplication.getLoanPurpose())){
                finalPurposeCoefficient=elementMap.get(key);
            }
        }
        return finalPurposeCoefficient;
    }
    public HashMap<String,Double>  finalLoanCoefficient(long applicationId,double existingSSNNumber) {
        Optional<LoanApplication> application = loanApplicationRepository.findById(applicationId);
        LoanApplication loanApplication = application.get();
        Optional<BureauData> bureauData = bureauDataRepository.findByExistingSSNNumber(existingSSNNumber);
        BureauData bureauDataBySSNNumber = bureauData.get();

        HashMap<String, Double> coefficientMap = new HashMap<>();
        HashMap<String, Double> meanMap = new HashMap<>();
        HashMap<String, Double> stdMap = new HashMap<>();
        double finalLoanCoefficient = 0.00;

        //from loanApplication
        double loanAmount = (loanApplication.getLoanAmount()).doubleValue();
        double annualSalary = (loanApplication.getAnnualSalary()).doubleValue();
        double employeeLength = Integer.parseInt(loanApplication.getWorkExperienceYear() + (Integer.parseInt(loanApplication.getWorkExperienceMonth())) / 12);
        double delinq2Yrs = 0;
        double inqLast6Mths = 0;
        double mthsSinceLastDelinq = 0;
        double mthsSinceLastRecord = 0;
        double openAcc = 0;
        double pubRec = 0;
        double revolBal = 0;
        double revolUtil = 0;
        double totalAcc = 0;
        if (Integer.parseInt(loanApplication.getSsnNumber()) == Double.valueOf(bureauDataBySSNNumber.getExistingSSNNumber()).intValue()) {
            //from bureauDataBySSNNumber
            delinq2Yrs = bureauDataBySSNNumber.getDelinq2Yrs();
            inqLast6Mths = bureauDataBySSNNumber.getInqLast6Mths();
            mthsSinceLastDelinq = bureauDataBySSNNumber.getMthsSinceLastDelinq();
            mthsSinceLastRecord = bureauDataBySSNNumber.getMthsSinceLastRecord();
            openAcc = bureauDataBySSNNumber.getOpenAcc();
            pubRec = bureauDataBySSNNumber.getPubRec();
            revolBal = bureauDataBySSNNumber.getRevolBal();
            revolUtil = bureauDataBySSNNumber.getRevolUtil();
            totalAcc = bureauDataBySSNNumber.getTotalAcc();
        }

        //for purpose risk
        double purposeCoefficient = purposeCoefficient(applicationId);
        //for credit age
        String earliestCrLine = bureauDataBySSNNumber.getEarliestCrLine();
        String dateString = earliestCrLine;
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy HH:mm");
        double creditAge=0;

        try {
            // Parse the string into a Date object
            Date date = dateFormat.parse(dateString);
            Calendar cal1 = Calendar.getInstance();
            Calendar cal2 = Calendar.getInstance();
            cal2.setTime(date);

            int yearDiff = cal1.get(Calendar.YEAR) - cal2.get(Calendar.YEAR);
            int monthDiff = cal1.get(Calendar.MONTH) - cal2.get(Calendar.MONTH);

              creditAge = yearDiff * 12 + monthDiff;
        } catch (ParseException e) {
            e.printStackTrace();
        }

                //for description
                double finalDescriptionRiskCoefficient = finalDescriptionRiskCoefficient(applicationId);
                //for intercept
                double intercept = 1;
                // Data
                String[] variables = {
                        "loan_amnt", "emp_length", "annual_inc", "delinq_2yrs", "inq_last_6mths",
                        "mths_since_last_delinq", "mths_since_last_record", "open_acc", "pub_rec",
                        "revol_bal", "revol_util", "total_acc", "purpose_risk", "credit_age",
                        "debt_to_income", "rev_bal_to_income", "delinq_rate_to_total",
                        "delinq_rate_to_open", "open_to_total", "inq_rate_to_total",
                        "inq_rate_to_open", "pub_rec_rate", "desc", "intercept"
                };
                Double[] coefficients = {
                        -0.1598, -0.0006, 0.2503, -0.3392, -0.1729, 0.0106, 0.0015, -0.0896,
                        -0.3786, 0.0383, -0.4426, 0.0433, -0.0843, 0.0154, -0.1194, 0.039,
                        0.0194, -0.0835, -0.0673, -0.1402, 0.0274, 0.3429, -0.3324, 0.19506077
                };
                Double[] means = {
                        11190.2667, 5.0207, 68005.4886, 0.1424, 0.867, 90.541, 125.8744, 9.313,
                        0.0544, 13524.3477, 0.4883, 22.2489, 0.135, 310.3275, 0.1894, 0.2061,
                        0.0076, 0.02, 0.4691, 0.0504, 0.1136, 0.0032, 0.1402, 0.00
                };
                Double[] stds = {
                        7404.5539, 3.348, 50677.936, 0.4729, 1.0562, 42.2793, 19.1641, 4.4642,
                        0.2366, 16382.5773, 0.2825, 11.6163, 0.0096, 79.4755, 0.1185, 0.1928,
                        0.0286, 0.0828, 0.1874, 0.0825, 0.1692, 0.0167, 0.0369, 1.00
                };

                // Populating the HashMaps
                for (int i = 0; i < variables.length; i++) {
                    coefficientMap.put(variables[i], coefficients[i]);
                    meanMap.put(variables[i], means[i]);
                    stdMap.put(variables[i], stds[i]);
                }
                //for debt to income
                double debToIncome = (loanAmount / (annualSalary));
                //for rev_bal to income
                double revBalToIncome = revolBal / annualSalary;
                //for delinq_rate_to_total
                double delinqRateToTotal = delinq2Yrs / totalAcc;
                //for delinq_rate_to_open
                double delinqRateToOpen = delinq2Yrs / openAcc;
                //for open_total
                double openTotal = openAcc / totalAcc;
                 System.out.println("openTotal:"+openTotal );
                //for inq_rate_to_total
                double inqRateToTotal = inqLast6Mths / totalAcc;
                //for inq_rate_to_open
                double inqRateToOpen = inqLast6Mths / openAcc;
                //for pub_rec_rate
                double pubRecRate = pubRec / totalAcc;

        Double[] preCoeff={loanAmount,employeeLength,annualSalary,delinq2Yrs,inqLast6Mths,mthsSinceLastDelinq,mthsSinceLastRecord,openAcc,pubRec,revolBal,revolUtil,totalAcc,purposeCoefficient,creditAge,debToIncome,revBalToIncome,delinqRateToTotal,delinqRateToOpen, openTotal, inqRateToTotal,inqRateToOpen,pubRecRate,finalDescriptionRiskCoefficient,intercept};

                    HashMap finalLoanCoefficientMap = new HashMap<>();


                for (int i = 0; i < variables.length; i++) {
                    double finalCoeff = (((preCoeff[i] - means[i]) / stds[i]) * coefficients[i]);
                    System.out.println("finalCoeff" + finalCoeff);
                    finalLoanCoefficientMap.put(variables[i], finalCoeff);
                }


                return finalLoanCoefficientMap;
            }

    public HashMap<String,Double>  finalLoanCoefficientNewUser(long applicationId) {
        Optional<LoanApplication> application = loanApplicationRepository.findById(applicationId);
        LoanApplication loanApplication = application.get();
        HashMap<String, Double> coefficientMap = new HashMap<>();
        HashMap<String, Double> meanMap = new HashMap<>();
        HashMap<String, Double> stdMap = new HashMap<>();
        double finalLoanCoefficient=0.00;

        //from loanApplication
        double loanAmount=(loanApplication.getLoanAmount()).doubleValue();
        System.out.println(loanAmount);
        double annualSalary=(loanApplication.getAnnualSalary()).doubleValue();
        System.out.println(annualSalary);
        double employeeLength=(double) Integer.parseInt(loanApplication.getWorkExperienceYear())+(double)(Integer.parseInt(loanApplication.getWorkExperienceMonth()))/12;
        System.out.println(employeeLength);
        double delinq2Yrs = 0;
        double inqLast6Mths =0;
        double mthsSinceLastDelinq=0;
        double mthsSinceLastRecord=0;
        double openAcc=0;
        double pubRec=0;
        double revolBal=0;
        double revolUtil=0;
        double totalAcc=0;

        //for purpose risk
        double purposeCoefficient=purposeCoefficient(applicationId);
        System.out.println("purpose Coeff: "+ purposeCoefficient);

        //for credit age
        double earliestCrLineinMonths = 0;

        //for description
        double finalDescriptionRiskCoefficient=finalDescriptionRiskCoefficient(applicationId);
        System.out.println(" finalDescriptionRiskCoefficient: "+ finalDescriptionRiskCoefficient);
        //for intercept
        double intercept=1;

        // Data
        String[] variables = {
                "loan_amnt", "emp_length", "annual_inc", "delinq_2yrs", "inq_last_6mths",
                "mths_since_last_delinq", "mths_since_last_record", "open_acc", "pub_rec",
                "revol_bal", "revol_util", "total_acc", "purpose_risk", "credit_age",
                "debt_to_income", "rev_bal_to_income", "delinq_rate_to_total",
                "delinq_rate_to_open", "open_to_total", "inq_rate_to_total",
                "inq_rate_to_open", "pub_rec_rate", "desc", "intercept"
        };
        Double[] coefficients = {
                -0.1598, -0.0006, 0.2503, -0.3392, -0.1729, 0.0106, 0.0015, -0.0896,
                -0.3786, 0.0383, -0.4426, 0.0433, -0.0843, 0.0154, -0.1194, 0.039,
                0.0194, -0.0835, -0.0673, -0.1402, 0.0274, 0.3429, -0.3324, 0.19506077
        };
        Double[] means = {
                11190.2667, 5.0207, 68005.4886, 0.1424, 0.867, 90.541, 125.8744, 9.313,
                0.0544, 13524.3477, 0.4883, 22.2489, 0.135, 310.3275, 0.1894, 0.2061,
                0.0076, 0.02, 0.4691, 0.0504, 0.1136, 0.0032, 0.1402, 0.00
        };
        Double[] stds = {
                7404.5539, 3.348, 50677.936, 0.4729, 1.0562, 42.2793, 19.1641, 4.4642,
                0.2366, 16382.5773, 0.2825, 11.6163, 0.0096, 79.4755, 0.1185, 0.1928,
                0.0286, 0.0828, 0.1874, 0.0825, 0.1692, 0.0167, 0.0369, 1.00
        };

        // Populating the HashMaps
        for (int i = 0; i < variables.length; i++) {
            coefficientMap.put(variables[i], coefficients[i]);
            meanMap.put(variables[i], means[i]);
            stdMap.put(variables[i], stds[i]);
        }
        //for debt to income
        double debToIncome=(loanAmount/(annualSalary));
        //for rev_bal to income
        double revBalToIncome= revolBal/annualSalary;
        //for delinq_rate_to_total
        double delinqRateToTotal= 0;
        //for delinq_rate_to_open
        double delinqRateToOpen = 0;
        //for open_total
        double openTotal= 0;
        //for inq_rate_to_total
        double inqRateToTotal = 0;
        //for inq_rate_to_open
        double inqRateToOpen = 0;
        //for pub_rec_rate
        double pubRecRate = 0;

        Double[] preCoeff={loanAmount,employeeLength,annualSalary,delinq2Yrs,inqLast6Mths,mthsSinceLastDelinq,mthsSinceLastRecord,openAcc,pubRec,revolBal,revolUtil,totalAcc,purposeCoefficient,earliestCrLineinMonths,debToIncome,revBalToIncome,delinqRateToTotal,delinqRateToOpen, openTotal, inqRateToTotal,inqRateToOpen,pubRecRate,finalDescriptionRiskCoefficient,intercept};

        HashMap finalLoanCoefficientMap = new HashMap<>();
        for (int i = 0; i < variables.length; i++) {
            double finalCoeff = (((preCoeff[i]-means[i]) / stds[i]) * coefficients[i]);
            System.out.println("finalCoeff:"+i+"----->>>"+finalCoeff);
            finalLoanCoefficientMap.put(variables[i], finalCoeff);
        }
        return finalLoanCoefficientMap;
    }

    @Override
    public Optional<BureauData> getBureauDataById(double existingSSNNumber) {
        return bureauDataRepository.findByExistingSSNNumber(existingSSNNumber);
    }


    @Override
    public final double CreditScore(long applicationId,double existingSSNNumber){
        Optional<LoanApplication> application = loanApplicationRepository.findById(applicationId);
        LoanApplication loanApplication = application.get();
        Optional<BureauData> bureauData = bureauDataRepository.findByExistingSSNNumber(existingSSNNumber);
        BureauData bureauDataBySSNNumber = bureauData.get();

        HashMap<String,Double> finalLoanCoefficient = finalLoanCoefficient(applicationId,existingSSNNumber);
        double sum=0;
        double score=0;
        for (Double value : finalLoanCoefficient.values()) {
            sum  += value;
        }
        return score= 300+600*(1/(1+Math.pow(Math.E,-sum)));
    }
    @Override
    public  String newUserCheckStatus(long applicationId){
        Optional<LoanApplication> application = loanApplicationRepository.findById(applicationId);
        LoanApplication loanApplication = application.get();

        double getScore =newUserCreditScore( applicationId);
        if(getScore>510){
            return "Approved";

        }
        return "Rejected";
    }
    @Override
    public  String checkReasonForNewUser(long applicationId){
        Optional<LoanApplication> application = loanApplicationRepository.findById(applicationId);
        LoanApplication loanApplication = application.get();

        double getScore =newUserCreditScore( applicationId);
        if(getScore>510){
            return "Score is Sufficient";

        }
        return "Score is not Sufficient";
    }
    @Override
    public final double newUserCreditScore(long applicationId){
        Optional<LoanApplication> application = loanApplicationRepository.findById(applicationId);
        LoanApplication loanApplication = application.get();
            HashMap<String,Double> finalLoanCoefficient = finalLoanCoefficientNewUser(applicationId);
            double sum=0;
            double score=0;+





            for (Double value : finalLoanCoefficient.values()) {
                sum  += value;
                System.out.println(sum);
            }

        score= 300+600*(1/(1+Math.pow(Math.E,-sum)));
        System.out.println("score:"+ score);
            return score;

    }

    @Override
    public String checkStatus(long applicationId, double existingSSNNumber){
            Optional<LoanApplication> application = loanApplicationRepository.findById(applicationId);
            LoanApplication loanApplication = application.get();
            Optional<BureauData> bureauData = bureauDataRepository.findByExistingSSNNumber(existingSSNNumber);
            BureauData bureauDataApplication= bureauData.get();

        double getScore =CreditScore( applicationId, existingSSNNumber);
        if(getScore>510){
            return "Approved";

        }
       return "Rejected";
        }
        @Override
    public String checkReasonForExistingUser(long applicationId, double existingSSNNumber){
        Optional<LoanApplication> application = loanApplicationRepository.findById(applicationId);
        LoanApplication loanApplication = application.get();
        Optional<BureauData> bureauData = bureauDataRepository.findByExistingSSNNumber(existingSSNNumber);
        BureauData bureauDataApplication= bureauData.get();

        double getScore =CreditScore( applicationId, existingSSNNumber);
        if(getScore>510){
            return "Score is Sufficient";

        }
        return "Score is not Sufficient";
    }

    }


//    @Override
//    public Optional<LoanApplication> getAllLoanApplicationsByUserName(String userName){
//        return loanApplicationRepository.findByUsername(userName);
//    }


//    public List<ViewLoanApplicationsDTO> getAllLoanApplicationsInfo() {
//        List<LoanApplication> loanApplications = loanApplicationRepository.findAll();
//
//        // Convert LoanApplication to ViewLoanApplicationsDTO
//        return loanApplications.stream()
//                .map(application -> {
//                    ViewLoanApplicationsDTO viewLoanApplicationsDTO = new ViewLoanApplicationsDTO();
//                    viewLoanApplicationsDTO.setApplicationId(application.getApplicationId()); // Set applicationId explicitly
//                    viewLoanApplicationsDTO.setLoanPurpose(application.getLoanPurpose());
////                    viewLoanApplicationsDTO.setSubmittedDate( application.getSubmissionDate());
//                    viewLoanApplicationsDTO.setStatus(application.getStatus());
//                    return viewLoanApplicationsDTO;
//                })
//                .collect(Collectors.toList());
//    }
//public List<ViewLoanApplicationsDTO> getAllLoanApplicationsInfo() {
//    List<LoanApplication> loanApplications = loanApplicationRepository.findAll();
//
//    // Convert LoanApplication to ViewLoanApplicationsDTO
//    return loanApplications.stream()
//            .map(application -> {
//                ViewLoanApplicationsDTO viewLoanApplicationsDTO = new ViewLoanApplicationsDTO();
//                viewLoanApplicationsDTO.setApplicationId(application.getApplicationId());
//                viewLoanApplicationsDTO.setLoanPurpose(application.getLoanPurpose());
//
//                viewLoanApplicationsDTO.setStatus(application.getStatus());
//                return viewLoanApplicationsDTO;
//            })
//            .collect(Collectors.toList());
//}








