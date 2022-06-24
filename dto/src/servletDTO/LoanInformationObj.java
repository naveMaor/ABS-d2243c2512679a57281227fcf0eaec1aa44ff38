package servletDTO;

import loan.Loan;
import loan.enums.eLoanStatus;

import java.io.Serializable;

public class LoanInformationObj implements Serializable {
    private String loanCategory;
    private eLoanStatus status;
    private String loanID;
    private String borrowerName;

    public LoanInformationObj(String LoanId,String borrowerName, String loanCategory, eLoanStatus status) {
        this.loanID = LoanId;
        this.borrowerName = borrowerName;
        this.loanCategory = loanCategory;
        this.status = status;

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


    public LoanInformationObj(Loan loan) {
        this.borrowerName = loan.getBorrowerName();
        this.loanCategory = loan.getLoanCategory();
        this.loanID = loan.getLoanID();
        this.status = loan.getStatus();
    }
}
