package servletDTO;

import loan.Loan;

public class BuyLoanObj {
    private String loanCategory;//
    private String loanID;//shem mezha
    private String borrowerName;// mi shlekah et haalvaa
    private String sellerName;// mi shmelve et haalvaa
    private double loanOriginalDepth;//Schum halvaa mekori
    private int originalLoanTimeFrame;// misgeret zman halvaa
    private int paymentFrequency;
    private double interestPercentagePerTimeUnit;//
    private double totalLoanCostInterestPlusOriginalDepth;
    private double price;


    public BuyLoanObj(Loan loan, String sellerName) {
        this.price = loan.getTotalRemainingFund()*(loan.calculateClientLoanOwningPercentage(sellerName)/100);
        this.sellerName = sellerName;
        this.borrowerName = loan.getBorrowerName();
        this.loanCategory = loan.getLoanCategory();
        this.loanOriginalDepth = loan.getLoanOriginalDepth();
        this.interestPercentagePerTimeUnit = loan.getInterestPercentagePerTimeUnit();
        this.loanID = loan.getLoanID();
        this.originalLoanTimeFrame = loan.getOriginalLoanTimeFrame();
        this.paymentFrequency = loan.getPaymentFrequency();
        this.totalLoanCostInterestPlusOriginalDepth = loan.getTotalLoanCostInterestPlusOriginalDepth();
    }

    public String getLoanCategory() {
        return loanCategory;
    }

    public void setLoanCategory(String loanCategory) {
        this.loanCategory = loanCategory;
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

    public String getSellerName() {
        return sellerName;
    }

    public void setSellerName(String sellerName) {
        this.sellerName = sellerName;
    }

    public double getLoanOriginalDepth() {
        return loanOriginalDepth;
    }

    public void setLoanOriginalDepth(double loanOriginalDepth) {
        this.loanOriginalDepth = loanOriginalDepth;
    }

    public int getOriginalLoanTimeFrame() {
        return originalLoanTimeFrame;
    }

    public void setOriginalLoanTimeFrame(int originalLoanTimeFrame) {
        this.originalLoanTimeFrame = originalLoanTimeFrame;
    }

    public int getPaymentFrequency() {
        return paymentFrequency;
    }

    public void setPaymentFrequency(int paymentFrequency) {
        this.paymentFrequency = paymentFrequency;
    }

    public double getInterestPercentagePerTimeUnit() {
        return interestPercentagePerTimeUnit;
    }

    public void setInterestPercentagePerTimeUnit(double interestPercentagePerTimeUnit) {
        this.interestPercentagePerTimeUnit = interestPercentagePerTimeUnit;
    }

    public double getTotalLoanCostInterestPlusOriginalDepth() {
        return totalLoanCostInterestPlusOriginalDepth;
    }

    public void setTotalLoanCostInterestPlusOriginalDepth(double totalLoanCostInterestPlusOriginalDepth) {
        this.totalLoanCostInterestPlusOriginalDepth = totalLoanCostInterestPlusOriginalDepth;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }
}
