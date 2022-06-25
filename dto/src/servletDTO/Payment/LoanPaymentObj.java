package servletDTO.Payment;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import loan.Loan;
import loan.enums.eLoanStatus;

import java.io.Serializable;

public class LoanPaymentObj implements Serializable {
    private double nextExpectedPaymentAmountDataMember;
    private double totalRemainingLoan;
    private String loanID;
    private int nextYazToPay;
    private eLoanStatus status;
    private boolean select =false;


    public double getNextExpectedPaymentAmountDataMember() {
        return nextExpectedPaymentAmountDataMember;
    }

    public void setNextExpectedPaymentAmountDataMember(double nextExpectedPaymentAmountDataMember) {
        this.nextExpectedPaymentAmountDataMember = nextExpectedPaymentAmountDataMember;
    }

    public double getTotalRemainingLoan() {
        return totalRemainingLoan;
    }

    public void setTotalRemainingLoan(double totalRemainingLoan) {
        this.totalRemainingLoan = totalRemainingLoan;
    }

    public String getLoanID() {
        return loanID;
    }

    public void setLoanID(String loanID) {
        this.loanID = loanID;
    }

    public int getNextYazToPay() {
        return nextYazToPay;
    }

    public void setNextYazToPay(int nextYazToPay) {
        this.nextYazToPay = nextYazToPay;
    }

    public eLoanStatus getStatus() {
        return status;
    }

    public void setStatus(eLoanStatus status) {
        this.status = status;
    }

    public LoanPaymentObj(double nextExpectedPaymentAmountDataMember, double totalRemainingLoan, String loanID, int nextYazToPay, eLoanStatus status) {
        this.nextExpectedPaymentAmountDataMember = nextExpectedPaymentAmountDataMember;
        this.totalRemainingLoan = totalRemainingLoan;
        this.loanID = loanID;
        this.nextYazToPay = nextYazToPay;
        this.status = status;
    }


    public LoanPaymentObj(Loan loan) {
        this.nextExpectedPaymentAmountDataMember = loan.getNextExpectedPaymentAmountDataMember();
        this.totalRemainingLoan = loan.getTotalRemainingLoan();
        this.loanID = loan.getLoanID();
        this.nextYazToPay = loan.getNextYazToPay();
        this.status = loan.getStatus();
    }


    public boolean isSelect() {
        return select;
    }

    public void setSelect(Boolean bool){
        this.select =bool;
    }
}
