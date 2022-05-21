package ui;

import ClientDTO.ClientObj;
import customes.Account;
import customes.Client;
import customes.Lenders;
import data.Database;
import data.schema.generated.AbsDescriptor;
import loan.Loan;
import loan.enums.eDeviationPortion;
import loan.enums.eLoanFilters;
import loan.enums.eLoanStatus;
import Money.operations.Payment;
import Money.operations.Transaction;
import loanDTO.LoanObj;
import time.Timeline;
import utills.Engine;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import static utills.Engine.*;

public class PrintFuncs {

    Engine engine = Engine.getInstance();

    private final static String BETWEEN = "Please enter an Integer number between: ";
    private final static String ZERO_OR_ONE = "please enter 1 or 0 only";

    // func2 helpers:
    public static void printLenderList(List<Lenders> lendersList) {
        if (!lendersList.isEmpty())
            System.out.println("Lenders:");
        int index = 1;
        for (Lenders lender:lendersList)
        {
            System.out.println(index + ". Name: " + lender.getFullName() + " , deposit: " +lender.getDeposit());
            //System.out.println(lender);
            index++;
        }
    }
    public static void printACTIVEstatus(LoanObj currLoan) {
        Timeline startLoanYaz = currLoan.getStartLoanYaz();
        List<Payment> paymentsList = currLoan.getPaymentsList();
        double payedFund =currLoan.getPayedFund();
        double payedInterest = currLoan.getPayedInterest();
        double currFundDepth = currLoan.calculateFundDepth();
        double currInterestDepth =currLoan.calculateCurrInterestDepth();
        int index = 1;
        System.out.println("\n This Loan started at: " + startLoanYaz.getTimeStamp()+" Yaz");
        if (currLoan.nextYazToPay() ==0)
            System.out.println("next payment is in: " + currLoan.getPaymentFrequency() +" Yazes\n");
        else
            System.out.println("next payment is in: " + currLoan.nextYazToPay() +" Yazes\n");
        if (!paymentsList.isEmpty()){
            System.out.println("Here are all payments that "+currLoan.getBorrowerName() +  "has made:");
            for(Payment pay:paymentsList)
            {
                System.out.println(index +". " +pay.toString());
                index ++;
            }
        }

        System.out.println("total payed fund: " + payedFund);
        System.out.println("remaining fund: " + currFundDepth);
        System.out.println("total payed interest: " + payedInterest);
        System.out.println("remaining interest: " + currInterestDepth);
    }
    public static void printRISKstatus(LoanObj currLoan){
        System.out.println("num of delayed payments: " + currLoan.getDeviation().getNumberOfYazNotPayed());
        System.out.println("sum of deviation: " + currLoan.getDeviation().getSumOfDeviation());
    }
    public static void printFINISHEDstatus(Loan currLoan){
        int startLoanYaz = currLoan.getStartLoanYaz();
        int endLoanYaz = currLoan.getEndLoanYaz();
        List<Payment> paymentsList = currLoan.getPaymentsList();

        System.out.println("start loan yaz: "+startLoanYaz);
        System.out.println("end loan yaz: " + endLoanYaz);
        for(Payment pay:paymentsList)
        {
            System.out.println(pay.toString());
        }
    }
    //func3 helpers
    public static void printAccountInfo(ClientObj client) {
        int index = 1;
        Account account = client.getMyAccount();
        List<Transaction> transactionList = account.getTnuaList();
        System.out.println("Account Balance: " + account.getCurrBalance());
        if(!transactionList.isEmpty()) {
            System.out.println("Transactions:");

/*        double beforeBalance=account.getCurrBalance();
        double afterBalance=account.getCurrBalance();*/

            double lastBalance = transactionList.get(0).getBalanceBefore();

            for (Transaction transaction : transactionList) {
                System.out.println(index + ".");
                System.out.println("yaz of tnua: " + transaction.getTimeOfMovement() + "yazes");
                if (transaction.getSum() > 0) {
                    System.out.println("schum tnua: +" + transaction.getSum());
                } else {
                    System.out.println("schum tnua: " + transaction.getSum());
                }
                //System.out.println("Transaction to_from:"+transaction.getTo_from());
                //afterBalance += transaction.getSum();
                transaction.setBalanceBefore(lastBalance);
                transaction.setBalanceAfter(lastBalance + transaction.getSum());
                lastBalance = transaction.getBalanceAfter();
                System.out.println("balance before the tnua: " + transaction.getBalanceBefore());
                System.out.println("balance after the tnua: " + transaction.getBalanceAfter());
                //beforeBalance=afterBalance;
                System.out.println("@@@@@@@@@@@@@@@@@@@@@");
                index++;
            }
        }
    }
    public void printConnectedLoans(ClientObj client) {
        String name = client.getFullName();
        List<LoanObj> lenderLoanList = client.getClientAsLenderLoanObjList();
        List<LoanObj> borrowLoanList = client.getClientAsBorrowLoanObjList();

        int index =1;

        if(!lenderLoanList.isEmpty()) {
            System.out.println("those are the Loans that " + name + " is a lender:");
            for (LoanObj loan:lenderLoanList)
            {
                System.out.println(index + ". ");
                printLoanInfo(loan);
                if(loan.getStatus().equals(eLoanStatus.NEW))
                    System.out.println("********************************");
                index++;
            }
        }
        else{
            System.out.println("there are no Loans that " + name + " is a lender");
        }
        index = 1;
        if(!borrowLoanList.isEmpty()) {
            System.out.println("those are the Loans that " + name + " is a borrower:");
            for (LoanObj loan:borrowLoanList)
            {
                System.out.println(index + ". ");
                printLoanInfo(loan);
                if(loan.getStatus().equals(eLoanStatus.NEW))
                    System.out.println("********************************");
                index++;
            }
        }
        else{
            System.out.println("there are no Loans that " + name + " is a borrower");
        }
        System.out.println("________________________________");
    }
    public void PrintStatusConnectedLoans(LoanObj loan) {
        eLoanStatus status=loan.getStatus();
        switch (status)
        {
            case PENDING:
            {
                double missingMoney = loan.getLoanOriginalDepth() - engine.calculateDeposit(loan.getLendersList());
                System.out.println(missingMoney + " is missing in order to turn this loan active");
                break;
            }
            case ACTIVE:
            {
                System.out.print("next payment is in " );
                if (loan.nextYazToPay()==0){
                    System.out.println(loan.getPaymentFrequency() + " yazes");
                }
                else {
                    System.out.println(loan.nextYazToPay() + " yazes");
                }
                //System.out.println("next payment is in " + loan.nextYazToPay() + " yazes");
                System.out.println("borrower will pay in the next payment: " + loan.nextExpectedPaymentAmount(eDeviationPortion.TOTAL));
                break;
            }
            case RISK:
            {
                printRISKstatus(loan);
                break;
            }
            case FINISHED:
            {
                System.out.println("start loan yaz: "+loan.getStartLoanYaz());
                System.out.println("end loan yaz: " + loan.getEndLoanYaz());
                break;
            }
            default:
                break;
        }
    }

    public void printLoanInfo2(LoanObj loan){
        System.out.println("Loan Id: " + loan.getLoanID());
        System.out.println("Loan owner: " + loan.getBorrowerName());
        System.out.println("Loan category: " + loan.getLoanCategory());
        System.out.println("loan original fund: " + loan.getLoanOriginalDepth());
        System.out.println("loan original interest: " + loan.getOriginalInterest());
        System.out.println("total Loan Cost, Interest Plus Original Depth: " + loan.getTotalLoanCostInterestPlusOriginalDepth());
        System.out.println("loan total original time frame: " + loan.getOriginalLoanTimeFrame()+ " yazes");
        System.out.println("loan payment Frequency: every " + loan.getPaymentFrequency() + " yazes");
        System.out.println("loan status: " + loan.getStatus());
        PrintFuncs.printLenderList(loan.getLendersList());//showing lenders list PENDING
        PrintStatusConnectedLoans2(loan);
    }

    public void PrintStatusConnectedLoans2(LoanObj loan) {
        eLoanStatus status=loan.getStatus();
        switch (status)
        {
            case PENDING:
            {
                double missingMoney = loan.getLoanOriginalDepth() - engine.calculateDeposit(loan.getLendersList());
                System.out.println("Total deposit: " + engine.calculateDeposit(loan.getLendersList()));
                System.out.println(missingMoney + " is missing in order to turn this loan active");
                break;
            }
            case ACTIVE:
            {
                printACTIVEstatus(loan);
                break;
            }
            case RISK:
            {
                printACTIVEstatus(loan);
                printRISKstatus(loan);
                break;
            }
            case FINISHED:
            {
                System.out.println("start loan yaz: "+loan.getStartLoanYaz());
                System.out.println("end loan yaz: " + loan.getEndLoanYaz());
                break;
            }
            default:
                break;
        }
    }


    public void printLoanInfo(LoanObj loan){
        System.out.println("Loan Id: " + loan.getLoanID());
        System.out.println("Loan owner: " + loan.getBorrowerName());
        System.out.println("Loan category: " + loan.getLoanCategory());

        System.out.println("loan original fund: " + loan.getLoanOriginalDepth());
        System.out.println("loan original interest: " + loan.getOriginalInterest());
        System.out.println("total Loan Cost, Interest Plus Original Depth: " + loan.getTotalLoanCostInterestPlusOriginalDepth());
        System.out.println("interest needed to pay every payment is: " + loan.getIntristPerPayment());

        System.out.println("loan total original time frame: " + loan.getOriginalLoanTimeFrame()+ " yazes");
        System.out.println("loan payment Frequency: every " + loan.getPaymentFrequency() + " yazes");
        System.out.println("loan status: " + loan.getStatus());
        PrintStatusConnectedLoans(loan);
    }

    //func4 helpers
    /**
     * prints all clients in database to UI with index attached
     */
    public void printAllClientsFromDatabase() {
        //creating index i , and printing all existing clients in database
        int i = 1;
        for (Client client : engine.getDatabase().getClientMap().values()) {
            System.out.println(i + ". " + client.getFullName());
            i++;
        }
    }
    /**
     * asking user and getting wanted deposit amount
     * @param full_name
     * @return
     */
    public  static int getDepositAmount(String full_name){

        System.out.println("How much would you like to to deposit into "+full_name+"'s account ?");
        System.out.println("(please enter a positive integer number)");
        int deposit = readIntFromUser(1,Integer.MAX_VALUE,true);
        return deposit;
    }
    public String ChooseClientFromDatabase () {
        //asking user to choose a client from database ,and getting input value of wanted client index
        List<ClientObj> clientsList = engine.getDatabase().getClientsObjList();
        int clientListSize =clientsList.size();
        System.out.println("Please enter wanted client index for deposit");
        int userClientIndexChoice = PrintFuncs.readIntFromUser(1,clientListSize,true);
        //getting client
        return clientsList.get(userClientIndexChoice-1).getFullName();
    }

    //func5 helpers
    public int getWithdrawalAmount(String full_name){
        //asking user and getting wanted deposit amount // S
        System.out.println("How much would you like to withdraw from "+full_name+"'s account ?");
        System.out.println("(please enter a positive integer number)");
        int withdraw = -(readIntFromUser(0,(int)engine.getDatabase().getClientMap().get(full_name).getMyAccount().getCurrBalance(),true));
        return withdraw;
    }
    public void printTransactionsFromClientName(String clientFullName){
        int index =1;
        for (Transaction transaction: engine.getTransactionsFromClientName(clientFullName)){
            System.out.println(index + ". time transaction made:" + transaction.getTimeOfMovement());
            System.out.println("transaction amount: " + transaction.getSum());
            index++;
        }
    }

    //func6 helpers
    /**
     * THIS FUNC PRINTS ALL THE CLIENTS IN THE SYSTEM AND ASK THE USER TO CHOOSE ONE, IT RETURNS THE CLIENT USER CHOSE
     * @return
     */
    public String printAndChooseClientsInTheSystem(){
        List<ClientObj> v = engine.getDatabase().getClientsObjList();
        int i=1;
        for(ClientObj client: v) {
            System.out.println(i + ". " + client.getFullName());
            System.out.println("current balance: " + client.getMyAccount().getCurrBalance());
            //v.add(client);
            ++i;
        }
        i =readIntFromUser(1,engine.getDatabase().getClientMap().size(),true);

        return v.get(i-1).getFullName();//todo might be i instead of i-1 becasue array starts from 0?
    }

    /**
     * THIS FUNC INITIALLIZE THE CLIENT MENU
     * @return
     */
    public  String customersMenu(){
        Scanner sc = new Scanner(System.in);
        System.out.println("please choose a customer to invest with");
        return printAndChooseClientsInTheSystem();
    }
    /**
     * THIS FUNC GETS A CLIENT AND THEN ASK THE USER WHAT LOANS DO THEY WANT THE CLIENT TO PARTICIPATE ACCORDING TO PARAMETERS
     * @param client
     * @return ArrayList <Loan>
     */
    public List<Loan> loanToInvest(Client client) {
        List<Loan> result = new ArrayList<>();
        List<String> loanCategoryUserList = new ArrayList<>();
        double balance = client.getMyAccount().getCurrBalance();
        List<Integer> loanFilters;
        Double minInterestPerYaz = Double.valueOf(0);
        int minYazTimeFrame = 0;
        //part 2 in word document

        loanFilters = getLoanFilters();
        if (loanFilters.get(eLoanFilters.LOAN_CATEGORY.ordinal()) == 1) {
            loanCategoryUserList = chooseCategoryToInvest();
        }
        else
            loanCategoryUserList = engine.getDatabase().getAllCategories();
        if (loanFilters.get(eLoanFilters.MINIMUM_INTEREST_PER_YAZ.ordinal()) == 1) {
            System.out.println("Please choose the minimum interest percentage per yaz ");
            minInterestPerYaz = readDoubleFromUser(0, Integer.MAX_VALUE);
            //todo: ask the metargel what do you expect
        }
        if (loanFilters.get(eLoanFilters.MINIMUM_YAZ_TIME_FRAME.ordinal()) == 1) {
            System.out.println("Please choose the minimum yaz time frame ");
            minYazTimeFrame = readIntFromUser(0, Integer.MAX_VALUE,true);
        }
        //part 3 in word document:
        for (Loan loan : engine.getDatabase().getLoanList()) {
            if (loan.getStatus() == eLoanStatus.NEW || loan.getStatus() == eLoanStatus.PENDING)//if the loan is new or pending
            //todo: notice here!!
                if (!(client.getFullName().equalsIgnoreCase(loan.getBorrowerName()) ))//If the client's name is not the borrower
                        if (minInterestPerYaz <= loan.getInterestPercentagePerTimeUnit())
                            if (minYazTimeFrame <= loan.getOriginalLoanTimeFrame())
                                if (engine.checkCategoryList(loanCategoryUserList, loan.getLoanCategory()))
                                    result.add(loan);
        }
        return result;
    }
    /**
     * this func gets client and ASK THE USER WHAT LOANS IT WILL BE PARTICIPATE and returns list of the filtered loans that the user chose
     * @param clientName
     */
    public List<Loan> ChooseLoans(String clientName) {
        Client client = engine.getDatabase().getClientByname(clientName);
        int  index = 1;;
        List<Integer> chosenLoansNumb = new ArrayList<>();
        List<Loan> Loanslist = loanToInvest(client);
        List<Loan> result = new ArrayList<>();
        if (Loanslist.isEmpty()){
            System.out.println("There are no loans to show!");
            return result;
        }

        for (Loan loan : Loanslist) {
            System.out.println(index+". ");
            printLoanInfo(new LoanObj(loan));
            //System.out.println(index + ". " + loan.toString());
            ++index;
        }
        boolean valid =true;
        do {
            System.out.println("please choose loans that the client would like to invest in: \n" +
                    "\"(Your answer must be returned in the above format: \"Desired loan number\", \"Desired loan number\", etc.)\"");
            System.out.println("if you don't like to invest in any of those, press 0");
            Scanner br = new Scanner(System.in);
            String lines = br.nextLine();
            String[] userInputs = lines.trim().split(",");
            for (String userInput : userInputs) {
                try {
                    if(Integer.parseInt(userInput)==0){
                        return result;
                    }
                    if(Integer.parseInt(userInput)>Loanslist.size() || Integer.parseInt(userInput)<1) {
                        System.out.println("Please enter only valid inputs: (inputs must be numbers only!)");
                        valid = false;
                    }
                    else{
                        chosenLoansNumb.add(Integer.parseInt(userInput)-1);
                        valid =true;
                    }
                } catch (NumberFormatException exception) {
                    System.out.println("Please enter only valid inputs: (inputs must be numbers only!)");
                    chosenLoansNumb.clear();
                    valid=false;
                }
            }
        }while(!valid);
        result = engine.getResultedArray(Loanslist,chosenLoansNumb);// RETURNS new array that is the user's chosen loans.
        return result;
    }
    public static ArrayList<Integer> getLoanFilters (){
        Scanner sc = new Scanner(System.in);
        ArrayList<Integer> result = new ArrayList<>();
        System.out.println("Would you like to filter by Loan category? ");
        result.add(readIntFromUser(0,1,false));
        System.out.println("Thank you, would you like to filter by minimum interest per yaz? ");
        result.add(readIntFromUser(0,1,false));
        System.out.println("Thank you, would you like to filter by minimum yaz time frame? ");
        result.add(readIntFromUser(0,1,false));
        System.out.println("Thank you");
        return result;
    }
    public ArrayList<String> chooseCategoryToInvest() {
        boolean valid = true;
        ArrayList<String> userSelectedCategories = new ArrayList<>();
        List <String> allCategoryList = engine.getDatabase().getAllCategories();
        do {
            System.out.println("Please select from the following list of options, the desired categories for investment:\n" +
                    "(Your answer must be returned in the above format: \"Desired category number\", \"Desired category number\", etc.)\n" );
            int index=1;
            for (String category : allCategoryList) {
                System.out.println(index+". "+category);
                ++index;
            }
            Scanner br = new Scanner(System.in);
            String lines = br.nextLine();
            String[] userInputs = lines.trim().split(",");
                for (String userInput : userInputs) {

                    try {
                        if(Integer.parseInt(userInput)>allCategoryList.size() || Integer.parseInt(userInput)<1) {
                        System.out.println("Please enter only valid inputs: (inputs must be numbers only!)");
                        valid = false;
                    }
                    else{
                        userSelectedCategories.add(allCategoryList.get(Integer.parseInt(userInput) - 1));
                        valid =true;
                    }
                    } catch (NumberFormatException exception) {
                        System.out.println("Please enter only valid inputs: (inputs must be numbers only!)");
                        userSelectedCategories.clear();
                        valid = false;
                    }
                }


        }while(!valid);

        return userSelectedCategories;

    }

/*
    public static void ClientToLoan(Loan loan,Client client,double investment){
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
    }*/
/*   public static double amountOfMoneyPerLoan(int amountOfLoansToInvest,double amountOfMoney) {
        return (amountOfMoney/amountOfLoansToInvest);
    }*/
    public static void printEmptyListNotification(double remainingInvestment){
        System.out.println("Invested in all chosen loans the maximum optional investment.\n there are no loans left to invest from selected loans");
        //System.out.println("remaining money left from original sum of investment is: "+remainingInvestment);
        System.out.println("you can choose to re-filter to continue investing");
    }
    public double getWantedInvestment(String clientName) {
        Client client = engine.getDatabase().getClientByname(clientName);
        double amountOfMoney = 0, balance = client.getMyAccount().getCurrBalance();
        System.out.println("Please enter the amount you would like the client to invest,\n (must a number between 1 and " + balance+")");
        amountOfMoney = readDoubleFromUser(1, balance);
        return amountOfMoney;
    }

    //func7 helpers
    public static void printYazAfterPromote(){
        System.out.println("Yaz was: " );
        Timeline.printPreviousCurrTime();
        System.out.println("Yaz now: " );
        Timeline.printStaticCurrTime();
    }
    //func1 helpers
    public boolean CheckAndPrintInvalidFile(AbsDescriptor descriptor) throws Exception {
        boolean isValid =true;
        String s = new String();

        if(!engine.checkValidCategories(descriptor)){
            s+= "\nthere is loan category that does not exist";
            isValid = false;
        }
        if(!engine.checkValidCustomersList(descriptor)){
            s+="\nthere are two customers with the same name";
            isValid =false;
        }
        if(!engine.checkValidLoanOwner(descriptor)){
            s+="\nthere is a loan with a loan owner name that does not exist";
            isValid = false;
        }
        if(!engine.checkValidPaymentFrequency(descriptor)){
            s+="\npayment frequency is not fully divided by the total time of the loan";
            isValid = false;
        }

        if(!isValid){
            s="File not valid!\n" +s;
            throw new Exception(s);
        }
        return isValid;
    }


    //general
    public static int readIntFromUser(int min, int max, boolean zero_or_realNumber){
        Scanner sc = new Scanner(System.in);
        int number;
        do {
            if(zero_or_realNumber)
                System.out.println("Please enter an Integer number between: " + min + " - " + max);
            else
                System.out.println("Please enter 0 for NO or 1 for YES");
            while (!sc.hasNextInt()) {
                System.out.println("Input is not valid, please enter a valid Input!");
                sc.next(); // this is important!
            }
            number = sc.nextInt();
        } while (number < min || number > max);
        return number;
    }
    public static double readDoubleFromUser(double min, double max){
        Scanner sc = new Scanner(System.in);
        Double number;
        do {
            System.out.println("Please enter a number between " + min + " and " + max);
            while (!sc.hasNextInt()) {
                System.out.println("Please enter a number!");
                sc.next(); // this is important!
            }
            number = sc.nextDouble();
        } while (number < min || number > max);
        return number;
    }



}