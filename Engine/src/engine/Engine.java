package engine;

import Money.operations.Transaction;
import customes.Account;
import customes.Client;
import customes.Lenders;
import data.Database;
import data.File.XmlFile;
import data.SaveSystemData;
import data.schema.generated.AbsDescriptor;
import data.schema.generated.AbsLoan;
import exceptions.BalanceException;
import exceptions.messageException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import loan.Loan;
import loan.enums.eLoanStatus;
import servletDTO.BuyLoanObj;
import time.Timeline;

import javax.xml.bind.JAXBException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static loan.enums.eLoanStatus.ACTIVE;
import static loan.enums.eLoanStatus.RISK;

public class Engine {
    private static Engine single_instance = null;
    /*
    This func gets lenders list and return thus sum of their deposit
     */
    Database database = Database.Database(); //LODGED

    public Engine() {
        database = Database.Database();
    }

    public static Engine getInstance() {
        if (single_instance == null) {
            single_instance = new Engine();
        }
        return single_instance;
    }

    public Database getDatabase() {
        return database;
    }

    public double calculateDeposit(List<Lenders> lendersList) {
        double sum = 0;
        for (Lenders lenders : lendersList)
            sum += lenders.getDeposit();

        return sum;
    }

    /**
     * func creates a transaction as needed (withdraw/deposit) , updates account's balance & transactions list
     *
     * @param money
     * @param accDestName
     */
    public void AccountTransaction(double money, String accDestName) throws BalanceException {
        Account accDest = database.getClientByname(accDestName).getMyAccount();
        double balanceAfter = accDest.getCurrBalance() + money;
        if (balanceAfter < 0) {
            throw new BalanceException("user can not be in minus");
        }
        //create a timestamp
        int timeStamp = Timeline.getCurrTime();
        //update dest account
        Transaction transaction = new Transaction(timeStamp, money, "My Account", accDest.getCurrBalance(), balanceAfter);
        accDest.getTnuaList().add(transaction);
        accDest.setCurrBalance(balanceAfter);
    }

    public void TransferMoneyBetweenAccounts(Account accSource, double money, Account accDest) {
        //create a timestamp
        int timeStamp = Timeline.getCurrTime();

        //update source account
        Transaction transactionMinus = new Transaction(timeStamp, (-money), String.valueOf(accDest.getID()), accSource.getCurrBalance(), accSource.getCurrBalance() - money);
        accSource.setCurrBalance(accSource.getCurrBalance() - money);

        // checking if there is the same transaction in the currAccount
        if (accSource.getTnuaList().contains(transactionMinus)) {
            List<Transaction> SourceTnuaList = accSource.getTnuaList();
            Transaction existingTransaction = SourceTnuaList.get(SourceTnuaList.lastIndexOf(transactionMinus));
            //if so adding to already existingTransaction the new amount
            existingTransaction.setSum(existingTransaction.getSum() + (-money));
            existingTransaction.setBalanceAfter(existingTransaction.getBalanceBefore() + existingTransaction.getSum());
        } else {
            accSource.getTnuaList().add(transactionMinus);
        }
        //update dest account
        Transaction transactionPlus = new Transaction(timeStamp, money, String.valueOf(accSource.getID()), accDest.getCurrBalance(), accDest.getCurrBalance() + money);
        if (accDest.getTnuaList().contains(transactionPlus)) {
            Transaction existingTransaction = accDest.getTnuaList().get(accDest.getTnuaList().lastIndexOf(transactionPlus));
            //if so adding to already existingTransaction the new amount
            existingTransaction.setSum(existingTransaction.getSum() + (money));
            existingTransaction.setBalanceAfter(existingTransaction.getBalanceBefore() - existingTransaction.getSum());
            //existingTransaction.setBalanceAfter(accSource.getCurrBalance()+money);
        } else {
            accDest.getTnuaList().add(transactionPlus);//SHAI: CHECK IF SUPPOSE TO BE AccDest ??
        }
        accDest.setCurrBalance(accDest.getCurrBalance() + money);

    }

    /**
     * THIS FUNCTION BUILD NEW LOAN ARRAY FROM THE CORRESPONDING INDEXES IN THE NUMBERS ARRAY
     *
     * @param loanArrayList
     * @param numbersArrayList
     * @return
     */
    public ArrayList<Loan> getResultedArray(List<Loan> loanArrayList, List<Integer> numbersArrayList) {
        ArrayList<Loan> result = new ArrayList<>();
        for (Integer integer : numbersArrayList) {
            result.add(loanArrayList.get(integer));
        }
        return result;
    }


    public boolean checkCategoryList(ObservableList<String> loanCategoryArrayList, String category) {
        boolean result = false;
        for (String s : loanCategoryArrayList) {
            if (s.equalsIgnoreCase(category)) {
                result = true;
            }
        }
        if (loanCategoryArrayList.isEmpty()) {
            result = true;
        }
        return result;
    }

    public void addLenderToLoanList(Client client, Loan loan, double amountOfMoney) {
        Lenders lender = new Lenders(client.getFullName(), amountOfMoney);
        loan.getLendersList().add(lender);

    }

    public Client returnClientByName(String name) throws IllegalArgumentException {
        Client client = database.getClientMap().get(name);
        if (client == null) {
            throw new IllegalArgumentException();
        } else {
            return client;
        }

    }

    /*    */

    /**
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
    public void filterAndHandleLoansListAfterPromote() {
        List<Loan> sortedLoanList = database.getSortedLoanList();
        for (Loan loan : sortedLoanList) {
            loan.setNextYazToPay(loan.calculateNextYazToPay());
            if ((loan.calculateNextYazToPay() == 0) && ((loan.getStatus() == ACTIVE) || (loan.getStatus() == RISK))) {
                loan.handleLoanAfterTimePromote();
            }
        }
    }

    public void paySinglePaymentForLoanList(List<Loan> loanList) throws messageException {
        for (Loan loan : loanList) {
            loan.paySingleLoanPayment();
        }
    }

    public void payEntirePaymentForLoanList(List<Loan> loanList) throws messageException {
        for (Loan loan : loanList) {
            loan.payEntireLoan();
        }
    }

    public void payPartialAmountForLoan(Loan loan, int amount) throws messageException {
        if (loan.getStatus() != RISK) {
            throw new messageException("You can only pay partial amount of money for loans that are in risk! :");
        } else
            loan.payPartialLoanPayment(amount);
    }


    /**
     * check if there is a loan category that does not exist
     */
    public boolean checkValidCategories(AbsDescriptor descriptor) {
        List<String> absCategoriesList = descriptor.getAbsCategories().getAbsCategory();
        List<AbsLoan> absLoanList = descriptor.getAbsLoans().getAbsLoan();
        boolean isCategoryExist = false;

        for (AbsLoan absLoan : absLoanList) {
            for (String s : absCategoriesList) {
                if (absLoan.getAbsCategory().equalsIgnoreCase(s)) {
                    isCategoryExist = true;
                }
            }
            if (!isCategoryExist)
                return false;
            isCategoryExist = false;
        }
        return true;
    }

    /**
     * check if there is a already loan with the same name
     */
    public boolean checkValidLoanName(AbsDescriptor descriptor) {
        List<AbsLoan> absLoanList = descriptor.getAbsLoans().getAbsLoan();
        List<String> loanList = database.getLoanNameList();
        for (AbsLoan absLoan : absLoanList) {
            if(loanList.contains(absLoan.getId()))
                return false;
        }
        return true;
    }



    /**
     * check if payment frequency is fully divided by the total time of the loan
     *
     * @param descriptor
     * @return
     */
    public boolean checkValidPaymentFrequency(AbsDescriptor descriptor) {
        List<AbsLoan> absLoanList = descriptor.getAbsLoans().getAbsLoan();
        int absTotalYazTime;
        int absPaysEveryYaz;

        for (AbsLoan absLoan : absLoanList) {
            absTotalYazTime = absLoan.getAbsTotalYazTime();
            absPaysEveryYaz = absLoan.getAbsPaysEveryYaz();
            if ((absTotalYazTime % absPaysEveryYaz) != 0)
                return false;
        }
        return true;
    }


    public void createNewLoanFromInputStream(InputStream inputStream, String name) throws JAXBException, IOException {
        XmlFile.createInputObjectFromFile2(inputStream);
        CheckInvalidFile(XmlFile.getInputObject());
        buildDataFromDescriptor(name);
    }

    public void buildDataFromDescriptor(String name) {
        AbsDescriptor descriptor = XmlFile.getInputObject();
        buildCategoriesData(descriptor.getAbsCategories().getAbsCategory());
        buildLoansData(descriptor.getAbsLoans().getAbsLoan(),name);
    }
    public void buildCategoriesData(List<String> absCategories) {
        for (String categoryName : absCategories) {
            database.addCategory(categoryName);
        }
    }

    public void buildLoansData(List<AbsLoan> absLoanList,String name ) {
        for (AbsLoan absLoan : absLoanList) {
            Loan newLoan = new Loan(absLoan.getId(),name, absLoan.getAbsCategory(), absLoan.getAbsCapital(), absLoan.getAbsTotalYazTime(), absLoan.getAbsPaysEveryYaz(), absLoan.getAbsIntristPerPayment());
            database.addLoanToLoanMap(newLoan);
        }
    }

    public void addNewLoanFromUser(Loan loan) throws IOException {
        checkValidLoanFromUser(loan);
        database.addCategory(loan.getLoanCategory());
        database.addLoanToLoanMap(loan);
    }


    private boolean checkValidLoanFromUser(Loan loan) throws IOException {
        boolean isValid = true;
        String s = new String();

        if(!checkValidLoanNameFromUser(loan.getLoanID())){
            s += "\nthere is already a loan with the same name";
            isValid = false;
        }


        if (!checkValidPaymentFrequencyFromUser(loan)) {
            s += "\npayment frequency is not fully divided by the total time of the loan";
            isValid = false;
        }

        if (!isValid) {
            throw new IOException(s);
        }
        return isValid;
    }


    private boolean checkValidLoanNameFromUser(String loanName){
        List<String> loanList = database.getLoanNameList();
            if(loanList.contains(loanName))
                return false;

        return true;
    }

    public boolean checkValidPaymentFrequencyFromUser(Loan loan){
        int totalYazTime;
        int paysEveryYaz;

            totalYazTime = loan.getOriginalLoanTimeFrame();
            paysEveryYaz = loan.getPaymentFrequency();
            if ((totalYazTime % paysEveryYaz) != 0)
                return false;
        return true;
    }


    public double getBalanceFromClientName(String name) {
        return database.getClientByname(name).getMyAccount().getCurrBalance();
    }

    public List<Transaction> getTransactionsFromClientName(String name) {
        return database.getClientByname(name).getMyAccount().getTnuaList();
    }

    public double getMinInvestment(List<Loan> loanslistToInvest) {
        //initialize minimal with first loan details
        Loan firstLoanInList = loanslistToInvest.get(0);
        double minimalInvest = firstLoanInList.getMissingMoney();
        double loanMaxInvestedForPercentage = firstLoanInList.getMaxOwnershipMoneyForPercentage();
        if (loanMaxInvestedForPercentage > 0)
            minimalInvest = Math.min(minimalInvest, loanMaxInvestedForPercentage);


        double leftFundForInvestment;
        for (Loan loan : loanslistToInvest) {
            //checks how much money is needed for loan to become active
            leftFundForInvestment = loan.getMissingMoney();
            loanMaxInvestedForPercentage = loan.getMaxOwnershipMoneyForPercentage();
            if (loanMaxInvestedForPercentage > 0)
                leftFundForInvestment = Math.min(leftFundForInvestment, loanMaxInvestedForPercentage);
            //getting minimal
            minimalInvest = Math.min(minimalInvest, leftFundForInvestment);
/*            if (leftFundForInvestment < minimalInvest)
                minimalInvest = leftFundForInvestment;*/
        }
        return minimalInvest;
    }

    /**
     * func's gets amountofmoney to invest and wanted loans to invest in , and return the amount of money to invest in each loan so the money will be splitted equaliy
     *
     * @param amountOfLoansToInvest
     * @param amountOfMoney
     * @return
     */
    public double amountOfMoneyPerLoan(int amountOfLoansToInvest, double amountOfMoney) {
        return (amountOfMoney / amountOfLoansToInvest);
    }

    /**
     * this function gets a loan and a client AS LENDER and connects the loan to the client
     *
     * @param loan
     * @param client
     */
    public void ClientToLoan(Loan loan, Client client, double investment) {
        //investing the money

        TransferMoneyBetweenAccounts(client.getMyAccount(), investment, loan.getLoanAccount());
        //checks if client is already exits in loan->lendersList
        //dummy lender to check if lender is already exists
        Lenders currLender = new Lenders(client.getFullName(), 0);
        //checks if client is already in loan's lendersList
        if (loan.getLendersList().contains(currLender)) {
            //getting ref to existing client's lender obj in loan lenders list
            Lenders refToExistingLenderFromLoanLendersList = loan.getLendersList().get(loan.getLendersList().indexOf(currLender));
            //updating deposit amount to new amount = exiting deposit + new investment
            refToExistingLenderFromLoanLendersList.setDeposit(refToExistingLenderFromLoanLendersList.getDeposit() + investment);
        }//adding lender to loans lender list
        else {
            addLenderToLoanList(client, loan, investment);
        }
        //checks if curr loan doesnt exits in client's clientAsLenderLoanList
        if (!client.getClientAsLenderLoanList().contains(loan))
        //adding loan to his Client -> clientAsLenderLoanList data member.
        {
            client.addLoanAsLender(loan);
        }
        loan.setMaxOwnershipMoneyForPercentage(loan.getMaxOwnershipMoneyForPercentage() - investment);
        //checks if loan status needs an update
        loan.UpdateLoanStatusIfNeeded();
    }

    public void updateClientStatusLoanNotification(Loan loan,String status){
        String borrowerName = loan.getBorrowerName();
        Client client=database.getClientByname(borrowerName);
        client.setNotification(client.getNotification()+"\n"+"yaz now "+Timeline.getCurrTime()+" your loan "+loan.getLoanID()+" just turned into " +status);
    }


    public int investing_according_to_agreed_risk_management_methodology(List<Loan> loanslistToInvest, double wantedInvestment, String clientName, int maxPercentage) {
        Client client = database.getClientByname(clientName);
        double amountOfMoneyPerLoan, minNeededInvestment, investment;
        int loanListSize;

        //initialize for every loan their amount of money the client need to invest in order to get to max percentage share
        if (maxPercentage != -1)
            for (Loan loan : loanslistToInvest) {
                maxPercentage = maxPercentage - loan.calculateClientLoanOwningPercentage(clientName);
                loan.editMaxOwnershipMoneyForPercentage(maxPercentage);
            }
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
            //initializing index for removal

            for (int index = 0; index < loanslistToInvest.size(); ) {
                Loan loan = loanslistToInvest.get(index);
                ClientToLoan(loan, client, investment);
                if ((loan.getStatus() == eLoanStatus.ACTIVE) || (loan.getMaxOwnershipMoneyForPercentage() == 0))
                    loanslistToInvest.remove(index);
                else //should move foward nothing was removed
                    ++index;

                double totalRaisedDeposit = loan.getLoanAccount().getCurrBalance();
                double missingMoney = loan.getLoanOriginalDepth() - totalRaisedDeposit;
                loan.setMissingMoney(missingMoney);
                loan.setTotalRaisedDeposit(totalRaisedDeposit);
            }
            loanListSize = loanslistToInvest.size();//NEWLY ADDED
            // as long as there is money left to invest , or list of optional investments is not empty
        } while (wantedInvestment != 0 && loanListSize != 0);


        //plaster!!!
        fixTransactionsList(client.getMyAccount().getTnuaList());
        for (Loan loan : loanslistToInvest) {
            fixTransactionsList(loan.getLoanAccount().getTnuaList());
        }
        return loanListSize;
    }

    public boolean CheckInvalidFile(AbsDescriptor descriptor) throws IOException {
        boolean isValid = true;
        String s = new String();

        if(!checkValidLoanName(descriptor)){
            s += "\nthere is already a loan with the same name";
            isValid = false;
        }


        if (!checkValidCategories(descriptor)) {
            s += "\nthere is loan category that does not exist";
            isValid = false;
        }

        if (!checkValidPaymentFrequency(descriptor)) {
            s += "\npayment frequency is not fully divided by the total time of the loan";
            isValid = false;
        }

        if (!isValid) {
            s = "File not valid!\n" + s;
            throw new IOException(s);
        }
        return isValid;
    }

    public ObservableList<Transaction> getClientTransactionsList(String name) {
        Client client = database.getClientByname(name);
        ObservableList<Transaction> result = FXCollections.observableArrayList();
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

    public ObservableList<Loan> O_getLoansToInvestList(String clientName, int minInterestPerYaz, int minYazTimeFrame, int maxOpenLoans, ObservableList<String> loanCategoryUserList, int maxOwnership) {
        Client client = database.getClientByname(clientName);
        ObservableList<Loan> tmp = FXCollections.observableArrayList(getDatabase().getLoanList());
        ObservableList<Loan> result = FXCollections.observableArrayList();
        int clientOpenLoansNumber = client.getOpenLoansNumber();
        int TotalInvestedPercentage, clientOwningLoanPercentage;
        for (Loan loan : tmp) {
            TotalInvestedPercentage = loan.calculateTotalOwnersPercentage();
            clientOwningLoanPercentage = loan.calculateClientLoanOwningPercentage(clientName);
            if (loan.getStatus() == eLoanStatus.NEW || loan.getStatus() == eLoanStatus.PENDING)//if the loan is new or pending
                if (!(client.getFullName().equalsIgnoreCase(loan.getBorrowerName())))//If the client's name is not the borrower
                    if ((minInterestPerYaz <= loan.getInterestPercentagePerTimeUnit()) || (minInterestPerYaz == -1))
                        if ((minYazTimeFrame <= loan.getOriginalLoanTimeFrame()) || minYazTimeFrame == -1)
                            if (checkCategoryList(loanCategoryUserList, loan.getLoanCategory()))
                                if ((clientOpenLoansNumber <= maxOpenLoans) || (maxOpenLoans == -1))
                                    if (((TotalInvestedPercentage <= maxOwnership) && (clientOwningLoanPercentage < maxOwnership)) || (maxOwnership == -1)) {
                                        result.add(loan);
                                    }
        }

        return result;
    }


    public ObservableList<Loan> merge(ObservableList<Loan> into, ObservableList<Loan>... lists) {
        final ObservableList<Loan> list = into;
        for (ObservableList<Loan> l : lists) {
            //list.addAll(l);
            l.addListener((javafx.collections.ListChangeListener.Change<? extends Loan> c) -> {
                while (c.next()) {
                    if (c.wasAdded()) {
                        list.addAll(c.getAddedSubList());
                    }
                    if (c.wasRemoved()) {
                        list.removeAll(c.getRemoved());
                    }
                }
            });
        }

        return list;
    }

    public void fixTransactionsList(List<Transaction> transactionList) {
        if (transactionList.size() > 1) {
            for (int i = 0; i < transactionList.size() - 1; i++) {
                Transaction currTransaction = transactionList.get(i);
                Transaction nextTransaction = transactionList.get(i + 1);
                double currPostBalance = currTransaction.getBalanceAfter();
                nextTransaction.setBalanceBefore(currPostBalance);
                nextTransaction.setBalanceAfter(nextTransaction.getBalanceBefore() + nextTransaction.getSum());
            }
        }
    }

    public void increaseYaz() {
        saveData(Timeline.getCurrTime(),database);
        Timeline.promoteStaticCurrTime();
        List<Loan> loanList = database.getLoanList();
        for (Loan loan : loanList) {
            loan.setNextYazToPay(loan.calculateNextYazToPay());
            if (loan.getNextYazToPay() == 0) {
                loan.getDeviation().setSkipped(true);
                //update Notification
                String borrowerName = loan.getBorrowerName();
                Client client = database.getClientByname(borrowerName);
                StringBuilder finalNotification = new StringBuilder();
                String currNotification = client.getNotification();
                finalNotification.append(currNotification).append("\n").append("time now is ").append(Timeline.getCurrTime()).append(" its time to pay for the loan ").append(loan.getLoanID());
                client.setNotification(finalNotification.toString());
            }
            eLoanStatus status = loan.getStatus();
            if ((status == ACTIVE) || (status == RISK)) {
                if ((loan.getDeviation().isSkipped()) && ((loan.getNextYazToPay() > 0) || (loan.getPaymentFrequency() == 1))) {
                    loan.setLoanToRisk();
                }
            }
        }
    }

    public double calculatePrice(Loan loan,String client){
        return loan.getTotalRemainingFund()*(loan.calculateClientLoanOwningPercentage(client)/100);
    }

    public void createLoanBuy(Loan loan,String buyer,String seller) throws BalanceException {
        int currTimeStamp = Timeline.getCurrTime();
        double price = calculatePrice(loan,seller);

        //get clients
        Client buyerClient = database.getClientByname(buyer);
        Account buyerAccount = buyerClient.getMyAccount();
        Client sellerClient = database.getClientByname(seller);
        Account sellerAccount = sellerClient.getMyAccount();

        if(buyerAccount.getCurrBalance()<price){
            throw new BalanceException("you do not have enough money");
        }


        //change client owning loans data
        sellerClient.getClientAsLenderLoanList().remove(loan);
        buyerClient.getClientAsLenderLoanList().add(loan);

        //create transaction
        Transaction transactionOfBuyer = new Transaction(currTimeStamp, -price, seller, buyerAccount.getCurrBalance(), buyerAccount.getCurrBalance() - price);
        buyerAccount.addTnuaToAccount(transactionOfBuyer);

        Transaction transactionOfSeller = new Transaction(currTimeStamp, price, buyer, buyerAccount.getCurrBalance(), buyerAccount.getCurrBalance() + price);
        sellerAccount.addTnuaToAccount(transactionOfSeller);

        //update balance
        buyerAccount.setCurrBalance(buyerAccount.getCurrBalance()-price);
        sellerAccount.setCurrBalance(sellerAccount.getCurrBalance()+price);

        //change loan owner
        Lenders SellerAslender = getLenderByName(seller,loan.getLendersList());
        Lenders BuyerAslender = new Lenders(buyer,SellerAslender.getDeposit());

        loan.getLendersList().remove(SellerAslender);
        loan.getLendersList().add(BuyerAslender);

        removeLoanOnSale(seller,loan);
        //change loan onsale
        //loan.setOnSale(false);
    }

    public void removeLoanOnSale(String seller,Loan loan){
        List<BuyLoanObj> loanObjList = database.getLoanOnSale().get(seller);
        loanObjList.removeIf(loanObj -> loanObj.getLoanID().equals(loan.getLoanID()));
    }

    private Lenders getLenderByName(String name,List<Lenders> lendersList){
        for (Lenders lenders:lendersList){
            if (lenders.getFullName().equals(name)){
                return lenders;
            }
        }
        System.out.println("didnt find the lender name in the list of loans lender list getLenderByName");
        return null;
    }

    public List<BuyLoanObj> getBuyLoanObjList(String userNameFromSession) {
        Map<String, List<BuyLoanObj>> loanOnSale = database.getLoanOnSale();
        List<BuyLoanObj> result = new ArrayList<>();
        for (List<BuyLoanObj> loanObjList : loanOnSale.values()) {
            for (BuyLoanObj buyLoanObj : loanObjList) {
                Loan loan = database.getLoanById(buyLoanObj.getLoanID());
                if ((loan.getStatus() == ACTIVE)&&(!loan.getBorrowerName().equals(userNameFromSession))&&(!buyLoanObj.getSellerName().equals(userNameFromSession))) {
                    result.add(buyLoanObj);
                }
            }
        }
        return result;
    }

    public void saveData(int yaz, Database database){
        SaveSystemData saveSystemData = new SaveSystemData(yaz,database);
        database.addSaveSystemDataToMap(yaz,saveSystemData);
    }

    public void loadRewindData(int yaz){
        database.setRewind(true);
        SaveSystemData saveSystemData = database.getSaveSystemData(yaz);
        database.setClientMap(saveSystemData.getClientMap());
        database.setLoanMapByCategory(saveSystemData.getLoanMapByCategory());
        Timeline.setCurrTime(saveSystemData.getCurrTime());
        database.setLoanOnSale(saveSystemData.getLoanOnSale());
        database.setAdminConnected(saveSystemData.isAdminConnected());
        database.setAdminSet(saveSystemData.getAdminSet());
    }
}



