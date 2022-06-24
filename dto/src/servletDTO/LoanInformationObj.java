package servletDTO;

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

    public String getLoanCategory() {
        return loanCategory;
    }

    public eLoanStatus getStatus() {
        return status;
    }

    public String getLoanID() {
        return loanID;
    }

    public String getBorrowerName() {
        return borrowerName;
    }
}
