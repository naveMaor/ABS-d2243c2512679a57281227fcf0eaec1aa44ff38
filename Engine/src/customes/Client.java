package customes;

import loan.Loan;
import loan.enums.eLoanStatus;
import old.LoanObj;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
public class Client implements Serializable {
    private String fullName;
    private Account myAccount;
    private List<Loan> clientAsLenderLoanList = new ArrayList<>();//
    private List<Loan> clientAsBorrowLoanList = new ArrayList<>();//




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
    public List<LoanObj> getClientAsLenderLoanObjList() {
        List<LoanObj> result = new ArrayList<>();
        for(Loan loanToCopy:clientAsLenderLoanList){
            result.add(new LoanObj(loanToCopy));
        }
        return result;
        //return clientAsLenderLoanList;
    }
    public List<LoanObj> getClientAsBorrowLoanObjList() {
        List<LoanObj> result = new ArrayList<>();
        for(Loan loanToCopy:clientAsBorrowLoanList){
            result.add(new LoanObj(loanToCopy));
        }
        return result;
        //return clientAsBorrowLoanList;
    }

    public List<Loan> getClientAsLenderLoanList() {
        return clientAsLenderLoanList;
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
/*    public void uniformsClientAsLenderLoanListBlock(){
        int listSize=this.getClientAsLenderLoanObjList().size();
        //checks if their at least two blocks to compare in list
        if(listSize>=2){
            int index= listSize-1;//last block
            while(this.getClientAsLenderLoanObjList().get(index).getLoanID()==this.getClientAsLenderLoanObjList().get(index-1).getLoanID())//compares if two last blocks have the same name
            {
                //removing unneeded block after updating prev block to new sum of investment
                this.getClientAsLenderLoanObjList().remove(index);
                //updating index to check pre compare
                index -= 1;
            }
        }
    }*/


    public int getOpenLoansNumber(){
        int result=0;
        for(Loan loan:clientAsBorrowLoanList){
            if(loan.getStatus()!= eLoanStatus.FINISHED){
                ++result;
            }
        }
        return result;
    }

}
