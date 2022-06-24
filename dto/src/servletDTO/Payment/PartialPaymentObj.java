package servletDTO.Payment;

import java.util.List;

public class PartialPaymentObj {
    String[] LoanNameList;
    int Amount;

    public PartialPaymentObj(String[] loanNameList, int amount) {
        LoanNameList = loanNameList;
        Amount = amount;
    }

    public String[] getLoanNameList() {
        return LoanNameList;
    }

    public int getAmount() {
        return Amount;
    }
}
