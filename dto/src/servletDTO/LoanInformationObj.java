package servletDTO;

import loan.enums.eLoanStatus;

public class LoanInformationObj {
    private String loanID;

    private String loanCategory;

    private eLoanStatus status;

    private String borrowerName;

    public LoanInformationObj(String loanID, String category, eLoanStatus status, String borrowerName) {
        this.loanID = loanID;
        this.loanCategory = category;
        this.status = status;
        this.borrowerName = borrowerName;
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
}
