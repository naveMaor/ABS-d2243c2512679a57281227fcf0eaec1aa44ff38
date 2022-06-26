package admin.sub.adminClientTable;


import loan.enums.eLoanStatus;
import old.ClientObj;

public class ClientLoans {
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

    public ClientLoans(ClientObj clientObj) {
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
}
