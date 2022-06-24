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
}
