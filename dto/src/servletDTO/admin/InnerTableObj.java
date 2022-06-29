package servletDTO.admin;

import Money.operations.Payment;
import customes.Lenders;
import loan.Loan;

import java.util.List;

public class InnerTableObj {
    List<Lenders> lendersList;
    List<Payment> paymentList;

    public List<Lenders> getLendersList() {
        return lendersList;
    }


    public List<Payment> getPaymentList() {
        return paymentList;
    }


    public InnerTableObj(Loan loan) {
        lendersList = loan.getLendersList();
        paymentList = loan.getPaymentsList();
    }


}
