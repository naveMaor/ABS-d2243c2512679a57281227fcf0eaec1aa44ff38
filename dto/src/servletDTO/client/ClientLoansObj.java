package servletDTO.client;

import customes.Client;
import loan.enums.eLoanStatus;
import old.ClientObj;

public class ClientLoansObj {
    private String Name;
    private double balance;

    private int asLenderNew;
    private int asLenderPending;
    private int asLenderActive;
    private int asLenderRisk;
    private int asLenderFinished;

    private int asBorrowerNew;
    private int asBorrowerPending;
    private int asBorrowerActive;
    private int asBorrowerRisk;
    private int asBorrowerFinished;

    public ClientLoansObj(String name, double balance, int asLenderNew, int asLenderPending, int asLenderActive, int asLenderRisk, int asLenderFinished, int asBorrowerNew, int asBorrowerPending, int asBorrowerActive, int asBorrowerRisk, int asBorrowerFinished) {
        Name = name;
        this.balance = balance;
        this.asLenderNew = asLenderNew;
        this.asLenderPending = asLenderPending;
        this.asLenderActive = asLenderActive;
        this.asLenderRisk = asLenderRisk;
        this.asLenderFinished = asLenderFinished;
        this.asBorrowerNew = asBorrowerNew;
        this.asBorrowerPending = asBorrowerPending;
        this.asBorrowerActive = asBorrowerActive;
        this.asBorrowerRisk = asBorrowerRisk;
        this.asBorrowerFinished = asBorrowerFinished;
    }

    public String getName() {
        return Name;
    }

    public double getBalance() {
        return balance;
    }

    public int getAsLenderNew() {
        return asLenderNew;
    }

    public int getAsLenderPending() {
        return asLenderPending;
    }

    public int getAsLenderActive() {
        return asLenderActive;
    }

    public int getAsLenderRisk() {
        return asLenderRisk;
    }

    public int getAsLenderFinished() {
        return asLenderFinished;
    }

    public int getAsBorrowerNew() {
        return asBorrowerNew;
    }

    public int getAsBorrowerPending() {
        return asBorrowerPending;
    }

    public int getAsBorrowerActive() {
        return asBorrowerActive;
    }

    public int getAsBorrowerRisk() {
        return asBorrowerRisk;
    }

    public int getAsBorrowerFinished() {
        return asBorrowerFinished;
    }

    public ClientLoansObj(Client clientObj) {
        int lender=1;
        int borrower=0;
        this.Name = clientObj.getFullName();
        this.balance = clientObj.getMyAccount().getCurrBalance();


        this.asLenderNew = clientObj.getNumberOfLoansByStatus(eLoanStatus.NEW,lender);
        this.asLenderPending = clientObj.getNumberOfLoansByStatus(eLoanStatus.PENDING,lender);
        this.asLenderActive = clientObj.getNumberOfLoansByStatus(eLoanStatus.ACTIVE,lender);
        this.asLenderRisk = clientObj.getNumberOfLoansByStatus(eLoanStatus.RISK,lender);
        this.asLenderFinished = clientObj.getNumberOfLoansByStatus(eLoanStatus.FINISHED,lender);

        this.asBorrowerNew = clientObj.getNumberOfLoansByStatus(eLoanStatus.NEW,borrower);;
        this.asBorrowerPending = clientObj.getNumberOfLoansByStatus(eLoanStatus.PENDING,borrower);
        this.asBorrowerActive = clientObj.getNumberOfLoansByStatus(eLoanStatus.ACTIVE,borrower);
        this.asBorrowerRisk = clientObj.getNumberOfLoansByStatus(eLoanStatus.RISK,borrower);
        this.asBorrowerFinished = clientObj.getNumberOfLoansByStatus(eLoanStatus.FINISHED,borrower);
    }

    public void setName(String name) {
        Name = name;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public void setAsLenderNew(int asLenderNew) {
        this.asLenderNew = asLenderNew;
    }

    public void setAsLenderPending(int asLenderPending) {
        this.asLenderPending = asLenderPending;
    }

    public void setAsLenderActive(int asLenderActive) {
        this.asLenderActive = asLenderActive;
    }

    public void setAsLenderRisk(int asLenderRisk) {
        this.asLenderRisk = asLenderRisk;
    }

    public void setAsLenderFinished(int asLenderFinished) {
        this.asLenderFinished = asLenderFinished;
    }

    public void setAsBorrowerNew(int asBorrowerNew) {
        this.asBorrowerNew = asBorrowerNew;
    }

    public void setAsBorrowerPending(int asBorrowerPending) {
        this.asBorrowerPending = asBorrowerPending;
    }

    public void setAsBorrowerActive(int asBorrowerActive) {
        this.asBorrowerActive = asBorrowerActive;
    }

    public void setAsBorrowerRisk(int asBorrowerRisk) {
        this.asBorrowerRisk = asBorrowerRisk;
    }

    public void setAsBorrowerFinished(int asBorrowerFinished) {
        this.asBorrowerFinished = asBorrowerFinished;
    }
}
