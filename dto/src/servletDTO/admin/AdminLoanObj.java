package servletDTO.admin;

import loan.Loan;
import loan.enums.eLoanStatus;

public class AdminLoanObj {
    private double loanOriginalDepth;//Schum halvaa mekori
    private double interestPercentagePerTimeUnit;//
    private String loanCategory;//
    private eLoanStatus status;//
    private String loanID;//shem mezha
    private String borrowerName;// mi shlekah et haalvaa
    private int paymentFrequency;
    private int originalLoanTimeFrame;// misgeret zman halvaa
    private int startLoanYaz;
    private int nextYazToPay;
    private int endLoanYaz;
    private double totalLoanCostInterestPlusOriginalDepth;


    public double getLoanOriginalDepth() {
        return loanOriginalDepth;
    }

    public void setLoanOriginalDepth(double loanOriginalDepth) {
        this.loanOriginalDepth = loanOriginalDepth;
    }

    public double getInterestPercentagePerTimeUnit() {
        return interestPercentagePerTimeUnit;
    }

    public void setInterestPercentagePerTimeUnit(double interestPercentagePerTimeUnit) {
        this.interestPercentagePerTimeUnit = interestPercentagePerTimeUnit;
    }

    public String getLoanCategory() {
        return loanCategory;
    }

    public void setLoanCategory(String loanCategory) {
        this.loanCategory = loanCategory;
    }

    public eLoanStatus getStatus() {
        return status;
    }

    public void setStatus(eLoanStatus status) {
        this.status = status;
    }

    public String getLoanID() {
        return loanID;
    }

    public void setLoanID(String loanID) {
        this.loanID = loanID;
    }

    public String getBorrowerName() {
        return borrowerName;
    }

    public void setBorrowerName(String borrowerName) {
        this.borrowerName = borrowerName;
    }

    public int getPaymentFrequency() {
        return paymentFrequency;
    }

    public void setPaymentFrequency(int paymentFrequency) {
        this.paymentFrequency = paymentFrequency;
    }

    public int getOriginalLoanTimeFrame() {
        return originalLoanTimeFrame;
    }

    public void setOriginalLoanTimeFrame(int originalLoanTimeFrame) {
        this.originalLoanTimeFrame = originalLoanTimeFrame;
    }

    public int getStartLoanYaz() {
        return startLoanYaz;
    }

    public void setStartLoanYaz(int startLoanYaz) {
        this.startLoanYaz = startLoanYaz;
    }

    public int getNextYazToPay() {
        return nextYazToPay;
    }

    public void setNextYazToPay(int nextYazToPay) {
        this.nextYazToPay = nextYazToPay;
    }

    public int getEndLoanYaz() {
        return endLoanYaz;
    }

    public void setEndLoanYaz(int endLoanYaz) {
        this.endLoanYaz = endLoanYaz;
    }

    public double getTotalLoanCostInterestPlusOriginalDepth() {
        return totalLoanCostInterestPlusOriginalDepth;
    }

    public void setTotalLoanCostInterestPlusOriginalDepth(double totalLoanCostInterestPlusOriginalDepth) {
        this.totalLoanCostInterestPlusOriginalDepth = totalLoanCostInterestPlusOriginalDepth;
    }


    public AdminLoanObj(Loan loan) {
        this.loanOriginalDepth = loan.getLoanOriginalDepth();
        this.interestPercentagePerTimeUnit = loan.getInterestPercentagePerTimeUnit();
        this.loanCategory = loan.getLoanCategory();
        this.status = loan.getStatus();
        this.loanID = loan.getLoanID();
        this.borrowerName = loan.getBorrowerName();
        this.paymentFrequency = loan.getPaymentFrequency();
        this.originalLoanTimeFrame = loan.getOriginalLoanTimeFrame();
        this.startLoanYaz = loan.getStartLoanYaz();
        this.nextYazToPay = loan.getNextYazToPay();
        this.endLoanYaz = loan.getEndLoanYaz();
        this.totalLoanCostInterestPlusOriginalDepth = loan.getTotalLoanCostInterestPlusOriginalDepth();
    }
}
