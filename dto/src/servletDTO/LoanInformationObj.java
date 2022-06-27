package servletDTO;

import customes.Client;
import loan.Loan;
import loan.enums.eLoanStatus;

import java.io.Serializable;

public class LoanInformationObj implements Serializable {
    private String loanCategory;
    private eLoanStatus status;
    private String loanID;
    private String borrowerName;
    private Double loanOriginalDepth;
    private Double totalLoanCostInterestPlusOriginalDepth;
    private Double interestPercentagePerTimeUnit;
    private boolean select;
    private Integer paymentFrequency;
    private Integer originalLoanTimeFrame;
    private double price;
    private boolean onSale;

    public void setSelect(boolean select) {
        this.select = select;
    }

    public LoanInformationObj(String loanCategory, eLoanStatus status, String loanID, String borrowerName, Double loanOriginalDepth, Double totalLoanCostInterestPlusOriginalDepth, Double interestPercentagePerTimeUnit, boolean select, Integer paymentFrequency, Integer originalLoanTimeFrame) {
        this.loanCategory = loanCategory;
        this.status = status;
        this.loanID = loanID;
        this.borrowerName = borrowerName;
        this.loanOriginalDepth = loanOriginalDepth;
        this.totalLoanCostInterestPlusOriginalDepth = totalLoanCostInterestPlusOriginalDepth;
        this.interestPercentagePerTimeUnit = interestPercentagePerTimeUnit;
        this.select = select;
        this.paymentFrequency = paymentFrequency;
        this.originalLoanTimeFrame = originalLoanTimeFrame;
    }

    public LoanInformationObj(String LoanId, String borrowerName, String loanCategory, eLoanStatus status,double price) {
        this.loanID = LoanId;
        this.borrowerName = borrowerName;
        this.loanCategory = loanCategory;
        this.status = status;
        this.price = price;
    }

    public LoanInformationObj(Loan loan, String client) {
        this.borrowerName = loan.getBorrowerName();
        this.loanCategory = loan.getLoanCategory();
        this.loanID = loan.getLoanID();
        this.status = loan.getStatus();
        //this.onSale = loan.isOnSale();
        if(loan.calculateClientLoanOwningPercentage(client)!=-2){
            this.price = loan.getTotalRemainingFund()*(loan.calculateClientLoanOwningPercentage(client)/100);
        }
    }


    public LoanInformationObj(String loanCategory, String loanID, String borrowerName, Double loanOriginalDepth, Double interestPercentagePerTimeUnit, Integer paymentFrequency, Integer originalLoanTimeFrame) {
        this.loanCategory = loanCategory;
        this.loanID = loanID;
        this.borrowerName = borrowerName;
        this.loanOriginalDepth = loanOriginalDepth;
        this.interestPercentagePerTimeUnit = interestPercentagePerTimeUnit;
        this.paymentFrequency = paymentFrequency;
        this.originalLoanTimeFrame = originalLoanTimeFrame;
    }

    public Double getLoanOriginalDepth() {
        return loanOriginalDepth;
    }

    public Double getTotalLoanCostInterestPlusOriginalDepth() {
        return totalLoanCostInterestPlusOriginalDepth;
    }

    public Double getInterestPercentagePerTimeUnit() {
        return interestPercentagePerTimeUnit;
    }

    public Boolean getSelect() {
        return select;
    }

    public Integer getPaymentFrequency() {
        return paymentFrequency;
    }

    public Integer getOriginalLoanTimeFrame() {
        return originalLoanTimeFrame;
    }

    public String getLoanID() {
        return loanID;
    }

    public void setLoanID(String loanID) {
        this.loanID = loanID;
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

    public String getBorrowerName() {
        return borrowerName;
    }

    public void setBorrowerName(String borrowerName) {
        this.borrowerName = borrowerName;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public boolean isOnSale() {
        return onSale;
    }

    public void setOnSale(boolean onSale) {
        this.onSale = onSale;
    }


}
