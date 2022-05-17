package customes;

import loan.Loan;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
public class Client implements Serializable {
    private String fullName;
    private Account myAccount;
    private List<Loan> clientAsLenderLoanList = new ArrayList<>();//
    private List<Loan> clientAsBorrowLoanList = new ArrayList<>();//


    public List<Loan> getClientAsLenderLoanList() {
        return clientAsLenderLoanList;
    }

// ctors
    public Client(String fullName, Account myAccount) {
        this.fullName = fullName;
        this.myAccount = myAccount;
    }
    public Client(String fullName, int balance){
        this.fullName = fullName;
        Account newAccount= new Account(Objects.hash(fullName) & 0xfffffff ,balance);
        this.myAccount = newAccount;
    }

   // setters
    public void setFullName(String fullName) {
        this.fullName = fullName;
    }
    public void setClientAsBorrowLoanList(List<Loan> clientAsBorrowLoanList) {
        this.clientAsBorrowLoanList = clientAsBorrowLoanList;
    }
//getters
    public String getFullName() {
        return fullName;
    }
    public List<Loan> getClientAsBorrowLoanList() {
        return clientAsBorrowLoanList;
    }
    public Account getMyAccount() {
        return myAccount;
    }




    public void addLoanAsLender(Loan loan) {
        //TODO: check if needed to be added in specific location
        this.clientAsLenderLoanList.add(loan);
    }

    public void addLoanAsBorrower(Loan loan) {
        //TODO: check if needed to be added in specific location
        this.clientAsBorrowLoanList.add(loan);
    }
    //MAYBE TO DELETE NOT YET
    public void uniformsClientAsLenderLoanListBlock(){
        int listSize=this.getClientAsLenderLoanList().size();
        //checks if their at least two blocks to compare in list
        if(listSize>=2){
            int index= listSize-1;//last block
            while(this.getClientAsLenderLoanList().get(index).getLoanID()==this.getClientAsLenderLoanList().get(index-1).getLoanID())//compares if two last blocks have the same name
            {
                //removing unneeded block after updating prev block to new sum of investment
                this.getClientAsLenderLoanList().remove(index);
                //updating index to check pre compare
                index -= 1;
            }
        }
    }

}
