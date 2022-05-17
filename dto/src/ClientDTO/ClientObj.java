package ClientDTO;

import customes.Account;
import customes.Client;
import loan.Loan;
import loanDTO.LoanObj;

import java.io.Serializable;
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
}
