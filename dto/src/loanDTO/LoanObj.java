package loanDTO;

import Money.Deviation;
import Money.operations.Payment;
import customes.Account;
import customes.Lenders;
import loan.Loan;
import loan.enums.eDeviationPortion;
import loan.enums.eLoanStatus;
import time.Timeline;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class LoanObj {
    private String loanCategory;//
    private eLoanStatus status;//
    private String loanID;//shem mezha
    private String borrowerName;// mi shlekah et haalvaa

    //List data members
    private List<Lenders> lendersList = new ArrayList<>();//
    private List<Payment> paymentsList = new ArrayList<>();// borrower paying every yaz list

    //Time settings data members:
    private int originalLoanTimeFrame;// misgeret zman halvaa
    private Timeline startLoanYaz = new Timeline();
    private int paymentFrequency ;
    private Timeline endLoanYaz = new Timeline();
    private double interestPercentagePerTimeUnit ;//

    private double intristPerPayment;
    private double fundPerPayment;

    //money that raised so far and money that left to raised to make active.
    private double moneyRaisedSoFar;
    private double missingMoney;

    //Original Loan info:
    private double originalInterest;//ribit mekorit
    private double loanOriginalDepth;//Schum halvaa mekori
    private double totalLoanCostInterestPlusOriginalDepth = originalInterest + loanOriginalDepth;

    //Dynamic growing data members:
    private double payedInterest=0;//ribit shulma
    private double payedFund=0;//keren shulma
    private Deviation deviation;

    //remaining Loan data:
    private double totalRemainingLoan = totalLoanCostInterestPlusOriginalDepth;//fund+interest

    private Account loanAccount;
    public LoanObj(Loan loanToCopy){
        this.borrowerName =loanToCopy.getBorrowerName();
        this.loanCategory =loanToCopy.getLoanCategory();
        this.loanOriginalDepth =loanToCopy.getLoanOriginalDepth();
        this.originalLoanTimeFrame = loanToCopy.getOriginalLoanTimeFrame();
        this.paymentFrequency = loanToCopy.getPaymentFrequency();
        this.fundPerPayment = loanToCopy.getFundPerPayment();
        this.status = loanToCopy.getStatus();
        this.loanID = loanToCopy.getLoanID();
        this.interestPercentagePerTimeUnit = loanToCopy.getInterestPercentagePerTimeUnit();  //(100*this.originalInterest)/this.loanOriginalDepth;
        this.originalInterest = loanToCopy.getOriginalInterest();
        this.totalLoanCostInterestPlusOriginalDepth = loanToCopy.getTotalLoanCostInterestPlusOriginalDepth();
        this.totalRemainingLoan = loanToCopy.getTotalRemainingLoan();
        this.loanAccount = loanToCopy.getLoanAccount();//todo: might be a problem with copying an list in the account
        this.intristPerPayment = loanToCopy.getIntristPerPayment();
        this.deviation= loanToCopy.getDeviation();
        this.moneyRaisedSoFar=this.calculateDeposit(loanToCopy.getLendersList());
        this.missingMoney=this.LeftToMakeActive();
    }

    public String getLoanID() {
        return loanID;
    }
    public String getBorrowerName() {
        return borrowerName;
    }
    public String getLoanCategory() {
        return loanCategory;
    }
    public double getOriginalInterest() {
        return originalInterest;
    }
    public double getTotalLoanCostInterestPlusOriginalDepth() {
        return totalLoanCostInterestPlusOriginalDepth;
    }
    public int getOriginalLoanTimeFrame() {
        return originalLoanTimeFrame;
    }
    public int getPaymentFrequency() {
        return paymentFrequency;
    }
    public eLoanStatus getStatus() {
        return status;
    }
    public List<Lenders> getLendersList() {
        return lendersList;
    }
    public double getInterestPercentagePerTimeUnit() {
        return interestPercentagePerTimeUnit;
    }
    public double getLoanOriginalDepth() {
        return loanOriginalDepth;
    }
    public Timeline getStartLoanYaz() {
        return startLoanYaz;
    }
    public Timeline getEndLoanYaz() {
        return endLoanYaz;
    }
    public List<Payment> getPaymentsList() {
        return paymentsList;
    }
    public double getPayedFund() {
        return payedFund;
    }
    public double getPayedInterest() {
        return payedInterest;
    }
    public final double calculateFundDepth(){
        return loanOriginalDepth-payedFund;
    }
    public final double calculateCurrInterestDepth(){
        return originalInterest-payedInterest;
    }
    public Deviation getDeviation() {
        return deviation;
    }
    public double getIntristPerPayment() {
        return intristPerPayment;
    }

    public int nextYazToPay() {
        int currTime = Timeline.getCurrTime();
        int startLoanYaz = this.startLoanYaz.getTimeStamp();
        int paymentFrequency = this.paymentFrequency;

        return ((currTime-startLoanYaz) % paymentFrequency );
    }
    public double nextExpectedPaymentAmount(eDeviationPortion DeviationPortion) {
        //double intristPerPayment = this.originalInterest/this.originalLoanTimeFrame.getTimeStamp();
        switch (DeviationPortion)
        {
            case INTEREST:{
                if(deviation.getInterestDeviation()>0)
                    return deviation.getInterestDeviation();
                else
                    return (this.intristPerPayment);
            }
            case FUND:{
                if(deviation.getFundDeviation()>0)
                    return deviation.getFundDeviation();
                else
                    return (fundPerPayment);
            }
            case TOTAL:{
                if(deviation.getSumOfDeviation()>0)
                {
                    return deviation.getSumOfDeviation();
                }
                else
                    return (totalLoanCostInterestPlusOriginalDepth / (originalLoanTimeFrame/paymentFrequency));
            }
        }

        return -1000000000000.0;
    }
    public double calculateDeposit(List<Lenders> lendersList)
    {
        double sum=0;
        for (Lenders lenders:lendersList)
            sum+=lenders.getDeposit();

        return sum;
    }
    public double LeftToMakeActive(){
       return this.loanOriginalDepth-this.moneyRaisedSoFar;
    }

    public double getFundPerPayment() {
        return fundPerPayment;
    }

    public double getMoneyRaisedSoFar() {
        return moneyRaisedSoFar;
    }

    public double getMissingMoney() {
        return missingMoney;
    }

    public double getTotalRemainingLoan() {
        return totalRemainingLoan;
    }
}
