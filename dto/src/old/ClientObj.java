package old;

import customes.Account;
import customes.Client;
import loan.enums.eLoanStatus;
import old.LoanObj;

import java.util.ArrayList;
import java.util.List;

public class ClientObj {
        private String fullName;
        private Account myAccount;
        private List<LoanObj> clientAsLenderLoanList = new ArrayList<>();//
        private List<LoanObj> clientAsBorrowLoanList = new ArrayList<>();//

        public ClientObj(Client clientToCopy){
                fullName = clientToCopy.getFullName();
                myAccount = clientToCopy.getMyAccount();
                clientAsLenderLoanList = clientToCopy.getClientAsLenderLoanObjList();
                clientAsBorrowLoanList = clientToCopy.getClientAsBorrowLoanObjList();
        }

        public String getFullName() {
                return fullName;
        }

        public Account getMyAccount() {
                return myAccount;
        }
        public List<LoanObj> getClientAsLenderLoanObjList() {
                return clientAsLenderLoanList;
        }
        public List<LoanObj> getClientAsBorrowLoanObjList() {
                return clientAsBorrowLoanList;
        }

        public int getNumberOfLoansByStatus(eLoanStatus eLoanStatus,int lenderOrBorrower){
                int lender=1;
                int borrower=0;
                int result=0;
                if(lenderOrBorrower==lender){
                        for (LoanObj loanObj:clientAsLenderLoanList) {
                                if(loanObj.getStatus()==eLoanStatus){
                                     ++result;
                                }
                        }
                }
                else{
                        for (LoanObj loanObj:clientAsBorrowLoanList) {
                                if(loanObj.getStatus()==eLoanStatus){
                                        ++result;
                                }
                        }
                }
                return result;
        }
}
