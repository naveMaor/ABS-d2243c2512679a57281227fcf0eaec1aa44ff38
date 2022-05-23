package utills;

import customes.Account;
import customes.Client;
import customes.Lenders;
import data.Database;
import data.File.XmlFile;
import data.schema.generated.AbsCustomer;
import data.schema.generated.AbsDescriptor;
import data.schema.generated.AbsLoan;
//
import exceptions.BalanceException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ListView;
import loan.Loan;
import Money.operations.Transaction;
import loan.enums.eLoanStatus;
import time.Timeline;

import java.util.ArrayList;
import java.util.List;

import static loan.enums.eLoanStatus.ACTIVE;
import static loan.enums.eLoanStatus.RISK;

public class Engine {
/*
This func gets lenders list and return thus sum of their deposit
 */
    Database database = Database.Database();

    public Database getDatabase() {
        return database;
    }

    private static Engine single_instance = null;
    private Engine() {
    }
    public static Engine getInstance()
    {
        if (single_instance == null)
            single_instance = new Engine();

        return single_instance;
    }

    public double calculateDeposit(List<Lenders> lendersList)
    {
        double sum=0;
        for (Lenders lenders:lendersList)
            sum+=lenders.getDeposit();

        return sum;
    }

    /**
     * func creates a transaction as needed (withdraw/deposit) , updates account's balance & transactions list
     * @param money
     * @param accDestName
     */
    public void AccountTransaction(double money, String accDestName) throws BalanceException {
        Account accDest = database.getClientByname(accDestName).getMyAccount();
        double balanceAfter= accDest.getCurrBalance()+money;
        if (balanceAfter<0){
            throw new BalanceException("user can not be in minus");
        }
        //create a timestamp
        int timeStamp = Timeline.getCurrTime();
        //update dest account
        Transaction transaction = new Transaction(timeStamp,money,"My Account",accDest.getCurrBalance(),balanceAfter);
        accDest.getTnuaList().add(transaction);
        accDest.setCurrBalance(balanceAfter);
    }

    public void TransferMoneyBetweenAccounts(Account accSource, double money, Account accDest)
    {
        //create a timestamp
        int timeStamp = Timeline.getCurrTime();

        //update source account
        Transaction transactionMinus = new Transaction(timeStamp,(-money),String.valueOf(accDest.getID()),accSource.getCurrBalance(),accSource.getCurrBalance()-money);
        accSource.setCurrBalance(accSource.getCurrBalance()-money);

        // checking if there is the same transaction in the currAccount
        if(accSource.getTnuaList().contains(transactionMinus)){
            Transaction existingTransaction = accSource.getTnuaList().get(accSource.getTnuaList().lastIndexOf(transactionMinus));
           //if so adding to already existingTransaction the new amount
            existingTransaction.setSum(existingTransaction.getSum()+(-money));
            existingTransaction.setBalanceAfter(existingTransaction.getBalanceBefore()+existingTransaction.getSum());

        }
        else {
            accSource.getTnuaList().add(transactionMinus);
        }
        //update dest account
        Transaction transactionPlus = new Transaction(timeStamp,money,String.valueOf(accSource.getID()),accDest.getCurrBalance(),accDest.getCurrBalance()+money);
        if(accDest.getTnuaList().contains(transactionPlus)){
            Transaction existingTransaction = accDest.getTnuaList().get(accDest.getTnuaList().lastIndexOf(transactionPlus));
            //if so adding to already existingTransaction the new amount
            existingTransaction.setSum(existingTransaction.getSum()+(money));
            existingTransaction.setBalanceAfter(existingTransaction.getBalanceBefore()-existingTransaction.getSum());
            //existingTransaction.setBalanceAfter(accSource.getCurrBalance()+money);
        }
        else {
            accDest.getTnuaList().add(transactionPlus);//SHAI: CHECK IF SUPPOSE TO BE AccDest ??
        }
        accDest.setCurrBalance(accDest.getCurrBalance()+money);

    }

    /**
     * THIS FUNCTION BUILD NEW LOAN ARRAY FROM THE CORRESPONDING INDEXES IN THE NUMBERS ARRAY
     * @param loanArrayList
     * @param numbersArrayList
     * @return
     */
    public ArrayList<Loan> getResultedArray(List<Loan> loanArrayList, List<Integer> numbersArrayList){
        ArrayList<Loan> result = new ArrayList<>();
        for (Integer integer:numbersArrayList)
        {
            result.add(loanArrayList.get(integer));
        }
        return result;
    }

    //NIKOL: this should probably be part of one of the classes.
    //NIKOL: what are you doing here? why do you need a list where all the values are the same?
    //SHAI: in what context this function is used? check if a certain category is in sent Arraylist ?
    public boolean checkCategoryList(ObservableList<String> loanCategoryArrayList, String category) {
        boolean result=false;
        for(String s:loanCategoryArrayList){
            if(s.equalsIgnoreCase(category)){
                result=true;
            }
        }
        return result;
    }
    public void addLenderToLoanList(Client client, Loan loan, double amountOfMoney) {
        Lenders lender = new Lenders(client.getFullName(),amountOfMoney);
        loan.getLendersList().add(lender);

    }
    public Client returnClientByName(String name) throws IllegalArgumentException{
        Client client= database.getClientMap().get(name);
        if (client == null)
        {
            throw new IllegalArgumentException();
        }
        else
        {
            return  client;
        }

    }

/*    *//**
     * this fun get loan list and order it by two parameters:
     * getStartLoanYaz and then nextExpectedPaymentAmount
     *//*
    public void orderLoanList(List<Loan> LoanList) {

        Collections.sort(LoanList, new Comparator() {

            public int compare(Object o1, Object o2) {

                Integer x1 = ((Loan) o1).getStartLoanYaz().getTimeStamp();
                Integer x2 = ((Loan) o2).getStartLoanYaz().getTimeStamp();
                int sComp = x1.compareTo(x2);

                if (sComp != 0) {
                    return sComp;
                }

                 Double x3 = ((Loan) o1).nextExpectedPaymentAmount(eDeviationPortion.TOTAL);
                 Double x4 = ((Loan) o2).nextExpectedPaymentAmount(eDeviationPortion.TOTAL);
                return x3.compareTo(x4);
            }});
}*/

    public void filterAndHandleLoansListAfterPromote(){
        List<Loan> sortedLoanList = database.getSortedLoanList();
        for (Loan loan:sortedLoanList){
            loan.setNextYazToPay(loan.nextYazToPay());
            if((loan.nextYazToPay() == 0)&&((loan.getStatus()== ACTIVE)|| (loan.getStatus()== RISK))){
                loan.handleLoanAfterTimePromote();
            }
        }
    }

    /**
     *     check if there is a loan category that does not exist
     */
    public boolean checkValidCategories(AbsDescriptor descriptor){
        List<String> absCategoriesList = descriptor.getAbsCategories().getAbsCategory();
        List<AbsLoan> absLoanList = descriptor.getAbsLoans().getAbsLoan();
        boolean isCategoryExist = false;

        for (AbsLoan absLoan:absLoanList){
            for(String s:absCategoriesList){
                if(absLoan.getAbsCategory().equalsIgnoreCase(s)) {
                    isCategoryExist=true;
                }
            }
            if(!isCategoryExist)
                return false;
            isCategoryExist = false;
        }
        return true;
    }

    /**
     *     check if there is a loan owner that does not exist
     */
    public boolean checkValidLoanOwner(AbsDescriptor descriptor){
        List<AbsCustomer> absCustomerList = descriptor.getAbsCustomers().getAbsCustomer();
        List<AbsLoan> absLoanList = descriptor.getAbsLoans().getAbsLoan();
        boolean isCustomerExist = false;

        for (AbsLoan absLoan:absLoanList){
            for(AbsCustomer absCustomer:absCustomerList){
                String customerName = absCustomer.getName();
                if(absLoan.getAbsOwner().equalsIgnoreCase(customerName))
                {
                    isCustomerExist=true;
                }
            }
            if(!isCustomerExist)
            {
                return false;
            }
            isCustomerExist = false;
        }
        return true;
    }

    /**
     * check if payment frequency is fully divided by the total time of the loan
     * @param descriptor
     * @return
     */
    public boolean checkValidPaymentFrequency(AbsDescriptor descriptor){
        List<AbsLoan> absLoanList = descriptor.getAbsLoans().getAbsLoan();
        int absTotalYazTime;
        int absPaysEveryYaz;

        for (AbsLoan absLoan:absLoanList){
            absTotalYazTime=absLoan.getAbsTotalYazTime();
            absPaysEveryYaz = absLoan.getAbsPaysEveryYaz();
            if((absTotalYazTime%absPaysEveryYaz) !=0)
                return false;
        }
        return true;
    }

    /**
     *  check if there is two customers with the same name and if there are it will return false
     * @param descriptor
     * @return
     */
    public boolean checkValidCustomersList(AbsDescriptor descriptor){
        List<AbsCustomer> absCustomerList = descriptor.getAbsCustomers().getAbsCustomer();
        List<String> customersName = new ArrayList<>();
        for (AbsCustomer absCustomer:absCustomerList) {
            if(customersName.contains(absCustomer.getName().toLowerCase()))
                return false;
            customersName.add(absCustomer.getName().toLowerCase());
        }
/*        for (String customersName:customersName){
            if(customersName.contains(absCustomer.getName()) )
                return false;
        }*/
        return true;
    }

    public void buildDataFromDescriptor(){
        database.clearAll();
        AbsDescriptor descriptor = XmlFile.getInputObject();
        buildCustomersData(descriptor.getAbsCustomers().getAbsCustomer());
        buildCategoriesData(descriptor.getAbsCategories().getAbsCategory());
        buildLoansData(descriptor.getAbsLoans().getAbsLoan());
    }

    public void buildCustomersData(List<AbsCustomer> absCustomerList){
        for (AbsCustomer absCustomer:absCustomerList){
            Client newClient = new Client(absCustomer.getName(),absCustomer.getAbsBalance());
            database.addClientToClientMap(newClient);
        }
    }
    public void buildCategoriesData(List<String> absCategories){
        for (String categoryName:absCategories){
            database.addCategory(categoryName);
        }
    }
    public void buildLoansData(List<AbsLoan> absLoanList){
       for (AbsLoan absLoan:absLoanList){
           Loan newLoan = new Loan(absLoan.getId(),absLoan.getAbsOwner(),absLoan.getAbsCategory(),absLoan.getAbsCapital(),absLoan.getAbsTotalYazTime(),absLoan.getAbsPaysEveryYaz(),absLoan.getAbsIntristPerPayment());
           database.addLoanToLoanMap(newLoan);
       }
    }

    public double getBalanceFromClientName(String name){
        return database.getClientByname(name).getMyAccount().getCurrBalance();
    }

    public List<Transaction> getTransactionsFromClientName(String name){
        return database.getClientByname(name).getMyAccount().getTnuaList();
    }

    public double getMinInvestment(List<Loan> loanslistToInvest){
        //initialize  minimal with first loan details
        double minimalInvest = (loanslistToInvest.get(0).getLoanOriginalDepth()-loanslistToInvest.get(0).getLoanAccount().getCurrBalance());
        double leftForInvestment;
        for (Loan loan : loanslistToInvest) {
            //checks how much money is needed for loan to become active
            leftForInvestment = loan.getLoanOriginalDepth() - loan.getLoanAccount().getCurrBalance();
            //getting minimal
            if (leftForInvestment < minimalInvest)
                minimalInvest = leftForInvestment;
        }
        return minimalInvest;
    }
    /**
     *  func's gets amountofmoney to invest and wanted loans to invest in , and return the amount of money to invest in each loan so the money will be splitted equaliy
     * @param amountOfLoansToInvest
     * @param amountOfMoney
     * @return
     */
    public double amountOfMoneyPerLoan(int amountOfLoansToInvest,double amountOfMoney) {
        return (amountOfMoney/amountOfLoansToInvest);
    }
    /**
     * this function gets a loan and a client AS LENDER and connects the loan to the client
     * @param loan
     * @param client
     */
    public void ClientToLoan(Loan loan,Client client,double investment){
        //investing the money

        TransferMoneyBetweenAccounts(client.getMyAccount(),investment,loan.getLoanAccount());
        //checks if client is already exits in loan->lendersList
        //dummy lender to check if lender is already exists
        Lenders currLender = new Lenders(client.getFullName(),0 );
        //checks if client is already in loan's lendersList
        if(loan.getLendersList().contains(currLender)) {
            //getting ref to existing client's lender obj in loan lenders list
            Lenders refToExistingLenderFromLoanLendersList = loan.getLendersList().get(loan.getLendersList().indexOf(currLender));
            //updating deposit amount to new amount = exiting deposit + new investment
            refToExistingLenderFromLoanLendersList.setDeposit(refToExistingLenderFromLoanLendersList.getDeposit()+investment);
        }//adding lender to loans lender list
        else {
            addLenderToLoanList(client, loan, investment);
        }
        //checks if curr loan doesnt exits in client's clientAsLenderLoanList
        if(!client.getClientAsLenderLoanList().contains(loan))
        //adding loan to his Client -> clientAsLenderLoanList data member.
        {
            client.addLoanAsLender(loan);
        }
        //checks if loan status needs an update
        loan.UpdateLoanStatusIfNeeded();
    }

    public int investing_according_to_agreed_risk_management_methodology(List<Loan> loanslistToInvest,double wantedInvestment,String clientName){
        Client client = database.getClientByname(clientName);
        double amountOfMoneyPerLoan,minNeededInvestment,investment;
        int loanListSize;
        do {

            //getting updated list size
            loanListSize = loanslistToInvest.size();
            //getting the amount of money wanted to invest equally for each loan from loan list
            amountOfMoneyPerLoan = amountOfMoneyPerLoan(loanListSize, wantedInvestment);
            //getting minimal investment needed
            minNeededInvestment = getMinInvestment(loanslistToInvest);
            //chosen way of payment
            investment = Math.min(amountOfMoneyPerLoan, minNeededInvestment);
            //reducing upcoming investments from wantedInvestment
            wantedInvestment -= investment * loanListSize;
            //TO DO: MAYBE TAKE LINES 111-119 TO A FUNC
            //initializing index for removal

            for (int index=0;index<loanslistToInvest.size();) {
                Loan loan = loanslistToInvest.get(index);
                ClientToLoan(loan, client, investment);
                if(loan.getStatus() == eLoanStatus.ACTIVE)
                    loanslistToInvest.remove(index);
                else //should move foward nothing was removed
                    ++index;
                double totalRaisedDeposit = calculateDeposit(loan.getLendersList());
                double missingMoney = loan.getLoanOriginalDepth() - totalRaisedDeposit;
                loan.setMissingMoney(missingMoney);
                loan.setTotalRaisedDeposit(totalRaisedDeposit);
            }
            loanListSize=loanslistToInvest.size();//NEWLY ADDED
            // as long as there is money left to invest , or list of optional investments is not empty
        } while (wantedInvestment != 0 && loanListSize != 0);

  return loanListSize;
}

    public boolean CheckInvalidFile(AbsDescriptor descriptor) throws Exception {
        boolean isValid =true;
        String s = new String();

        if(!checkValidCategories(descriptor)){
            s+= "\nthere is loan category that does not exist";
            isValid = false;
        }
        if(!checkValidCustomersList(descriptor)){
            s+="\nthere are two customers with the same name";
            isValid =false;
        }
        if(!checkValidLoanOwner(descriptor)){
            s+="\nthere is a loan with a loan owner name that does not exist";
            isValid = false;
        }
        if(!checkValidPaymentFrequency(descriptor)){
            s+="\npayment frequency is not fully divided by the total time of the loan";
            isValid = false;
        }

        if(!isValid){
            s="File not valid!\n" +s;
            throw new Exception(s);
        }
        return isValid;
    }

    public ObservableList<Transaction> getClientTransactionsList(String name){
        Client client = database.getClientByname(name);
        ObservableList <Transaction> result =  FXCollections.observableArrayList();
        Account account = client.getMyAccount();
        List<Transaction> transactionList = account.getTnuaList();

/*        if(!transactionList.isEmpty()) {
            double lastBalance = transactionList.get(0).getBalanceBefore();

            for (Transaction transaction : transactionList) {
                transaction.setBalanceBefore(lastBalance);
                transaction.setBalanceAfter(lastBalance + transaction.getSum());
            }
        }*/
        result.addAll(transactionList);
        return result;
    }

    public ObservableList<Loan> O_getLoansToInvestList(String clientName, int minInterestPerYaz, int minYazTimeFrame, int maxOpenLoans, ObservableList<String> loanCategoryUserList){
        Client client = database.getClientByname(clientName);
        ObservableList<Loan> tmp = FXCollections.observableArrayList(getDatabase().getLoanList());
        ObservableList<Loan> result = FXCollections.observableArrayList();
        int clientOpenLoansNumber= client.getOpenLoansNumber();
        for (Loan loan : tmp) {
            if (loan.getStatus() == eLoanStatus.NEW || loan.getStatus() == eLoanStatus.PENDING)//if the loan is new or pending
                //todo: notice here!!
                if (!(client.getFullName().equalsIgnoreCase(loan.getBorrowerName()) ))//If the client's name is not the borrower
                    if (minInterestPerYaz <= loan.getInterestPercentagePerTimeUnit())
                        if (minYazTimeFrame <= loan.getOriginalLoanTimeFrame())
                            if (checkCategoryList(loanCategoryUserList, loan.getLoanCategory()))
                                if(clientOpenLoansNumber<=maxOpenLoans)
                                    result.add(loan);
        }

        return result;
    }
}



