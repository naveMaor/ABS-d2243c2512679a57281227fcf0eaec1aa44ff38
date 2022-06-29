package servletDTO;

import customes.Account;
import customes.Client;
import loan.Loan;

import java.util.ArrayList;
import java.util.List;

public class ClientDTOforServlet {

    private String fullName;
    private Account myAccount;
    private List<LoanInformationObj> clientAsLenderLoanList = new ArrayList<>();//
    private List<LoanInformationObj> clientAsBorrowLoanList = new ArrayList<>();//
    private String notification;

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public Account getMyAccount() {
        return myAccount;
    }

    public void setMyAccount(Account myAccount) {
        this.myAccount = myAccount;
    }

    public List<LoanInformationObj> getClientAsLenderLoanList() {
        return clientAsLenderLoanList;
    }

    public void setClientAsLenderLoanList(List<LoanInformationObj> clientAsLenderLoanList) {
        this.clientAsLenderLoanList = clientAsLenderLoanList;
    }

    public List<LoanInformationObj> getClientAsBorrowLoanList() {
        return clientAsBorrowLoanList;
    }

    public void setClientAsBorrowLoanList(List<LoanInformationObj> clientAsBorrowLoanList) {
        this.clientAsBorrowLoanList = clientAsBorrowLoanList;
    }

    public ClientDTOforServlet(Client client) {
        this.fullName = client.getFullName();
        this.myAccount = client.getMyAccount();
        this.notification = client.getNotification();
        for (Loan loan:client.getClientAsLenderLoanList()){
            this.clientAsLenderLoanList.add(new LoanInformationObj(loan,client.getFullName()));
        }
        for (Loan loan:client.getClientAsBorrowLoanList()){
            this.clientAsBorrowLoanList.add(new LoanInformationObj(loan,client.getFullName()));
        }
    }

    public String getNotification() {
        return notification;
    }

    public void setNotification(String notification) {
        this.notification = notification;
    }
}
