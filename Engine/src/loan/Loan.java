package loan;

import customes.Account;
import customes.Client;
import customes.Lenders;
import exceptions.messageException;
import loan.enums.eDeviationPortion;
import loan.enums.eLoanStatus;
import Money.operations.Payment;
import Money.operations.Transaction;
import time.Timeline;
import engine.Engine;
import Money.*;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
public class Loan implements Serializable {
    //Engine engine = Engine.getInstance();
    private Engine engine;

    //Identification data members:
    private String loanCategory;//
    private eLoanStatus status;//
    private String loanID;//shem mezha
    private String borrowerName;// mi shlekah et haalvaa

    //List data members
    private List<Lenders> lendersList = new ArrayList<>();//
    private List<Payment> paymentsList = new ArrayList<>();// borrower paying every yaz list

    //Time settings data members:
    private int originalLoanTimeFrame;// misgeret zman halvaa
    private int startLoanYaz;
    private int paymentFrequency;
    private int endLoanYaz;
    private double interestPercentagePerTimeUnit;//

    private double intristPerPayment;
    private double fundPerPayment;

    //Original Loan info:
    private double originalInterest;//ribit mekorit
    private double loanOriginalDepth;//Schum halvaa mekori
    private double totalLoanCostInterestPlusOriginalDepth = originalInterest + loanOriginalDepth;

    //Dynamic growing data members:
    private double payedInterest = 0;//ribit shulma
    private double payedFund = 0;//keren shulma
    private Deviation deviation;
    private double missingMoney = loanOriginalDepth; //money missing in order to set this loan active
    private double totalRaisedDeposit;
    private int nextYazToPay;
    private double maxOwnershipMoneyForPercentage = 0; // this data member only usable for calculating and remembering lenders investment according to max percentage given in scramble
    private double nextExpectedPaymentAmountDataMember = -1;

    //remaining Loan data:
    private double totalRemainingLoan = totalLoanCostInterestPlusOriginalDepth;//fund+interest

    private Account loanAccount;
    private boolean select = false;

/*    private CheckBox select;
    private Button infoButton;*/

    //constructors
    public Loan(String LoanId,String borrowerName, String loanCategory, double loanOriginalDepth, int originalLoanTimeFrame, int paymentFrequency, int intristPerPayment) {
        engine = new Engine();
        this.borrowerName = borrowerName;
        this.loanCategory = loanCategory;
        this.loanOriginalDepth = loanOriginalDepth;
        //Timeline newOriginalLoanTimeFrame = new Timeline(originalLoanTimeFrame);
        this.originalLoanTimeFrame = originalLoanTimeFrame;
        //Timeline newPaymentFrequency = new Timeline(paymentFrequency);
        this.paymentFrequency = paymentFrequency;
        this.fundPerPayment = this.loanOriginalDepth / (this.originalLoanTimeFrame / this.paymentFrequency);
        this.status = eLoanStatus.NEW;
        this.loanID = LoanId;
        //this.loanID = Objects.hash(this.loanCategory, this.originalLoanTimeFrame, startLoanYaz) & 0xfffffff;
        this.interestPercentagePerTimeUnit = intristPerPayment;  //(100*this.originalInterest)/this.loanOriginalDepth;
        this.originalInterest = calculateInterest();
        this.totalLoanCostInterestPlusOriginalDepth = this.originalInterest + this.loanOriginalDepth;
        this.totalRemainingLoan = this.totalLoanCostInterestPlusOriginalDepth;
        this.loanAccount = new Account(Objects.hash(this.loanID) & 0xfffffff, 0);
        this.intristPerPayment = calculateInristPerPayment();
        this.deviation = new Deviation();
/*        this.select = new CheckBox();
        this.infoButton = new Button();*/
        this.missingMoney = this.loanOriginalDepth;
        this.maxOwnershipMoneyForPercentage = 0;
        this.nextExpectedPaymentAmountDataMember = calculateNextExpectedPaymentAmount(eDeviationPortion.TOTAL);
        this.nextYazToPay = paymentFrequency;
    }


    public final double calculateCurrInterestDepth() {
        return originalInterest - payedInterest;
    }

    public final double calculateFundDepth() {
        return loanOriginalDepth - payedFund;
    }


    //getter and setters:
/*    public void generateLoanID() {
        this.loanID = Objects.hash(loanCategory, originalLoanTimeFrame, startLoanYaz) & 0xfffffff;
    }*/
    public String getLoanID() {
        return loanID;
    }

    public String getBorrowerName() {
        return borrowerName;
    }

    public void setBorrowerName(String borrowerName) {
        this.borrowerName = borrowerName;
    }

    public List<Payment> getPaymentsList() {
        return paymentsList;
    }

    public void setPaymentsList(List<Payment> paymentsList) {
        this.paymentsList = paymentsList;
    }

    public int getOriginalLoanTimeFrame() {
        return originalLoanTimeFrame;
    }

    public int getPaymentFrequency() {
        return paymentFrequency;
    }

    public int getEndLoanYaz() {
        return endLoanYaz;
    }

    public double getInterestPercentagePerTimeUnit() {
        return interestPercentagePerTimeUnit;
    }

    public double getOriginalInterest() {
        return originalInterest;
    }

    public double calculateInterest() {
        return this.loanOriginalDepth * (this.interestPercentagePerTimeUnit / 100.0);
    }

    public double getLoanOriginalDepth() {
        return loanOriginalDepth;
    }

    public double getPayedInterest() {
        return payedInterest;
    }

    public double getPayedFund() {
        return payedFund;
    }

    public double getTotalLoanCostInterestPlusOriginalDepth() {
        return totalLoanCostInterestPlusOriginalDepth;
    }

    public String getLoanCategory() {
        return loanCategory;
    }

    public eLoanStatus getStatus() {
        return status;
    }

    public void setStatus(eLoanStatus status) {
        this.status = status;
    }

    public List<Lenders> getLendersList() {
        return lendersList;
    }

    public int getStartLoanYaz() {
        return startLoanYaz;
    }

    public void setStartLoanYaz(int startLoanYaz) {
        this.startLoanYaz = startLoanYaz;
    }

    public void setLendersList(List<Lenders> lendersList) {
        this.lendersList = lendersList;
    }

    public Account getLoanAccount() {
        return loanAccount;
    }

    public double getFundPerPayment() {
        return fundPerPayment;
    }

    public double getTotalRemainingLoan() {
        return totalRemainingLoan;
    }

    public double getIntristPerPayment() {
        return intristPerPayment;
    }

    public double calculateInristPerPayment() {
        return this.originalInterest / (originalLoanTimeFrame / paymentFrequency);
    }

    public Deviation getDeviation() {
        return deviation;
    }

    public void setMissingMoney(double missingMoney) {
        this.missingMoney = missingMoney;
    }

    public void setTotalRaisedDeposit(double totalRaisedDeposit) {
        this.totalRaisedDeposit = totalRaisedDeposit;
    }

    public void setNextYazToPay(int nextYazToPay) {
        this.nextYazToPay = nextYazToPay;
    }

    public double getMissingMoney() {
        return missingMoney;
    }

    public double getTotalRaisedDeposit() {
        return totalRaisedDeposit;
    }

    public int getNextYazToPay() {
        return nextYazToPay;
    }

/*    public CheckBox getSelect() {
        return select;
    }*/

    public boolean getSelect() {
        return select;
    }

/*    public void setSelect(Boolean select) {
        this.select.setSelected(select);
    }*/

    public void setSelect(Boolean select) {
        this.select=select;
    }

/*    public Button getInfoButton() {
        return infoButton;
    }*/

    public double getMaxOwnershipMoneyForPercentage() {
        return maxOwnershipMoneyForPercentage;
    }

    public void editMaxOwnershipMoneyForPercentage(int percentage) {
        this.maxOwnershipMoneyForPercentage = loanOriginalDepth * (double) percentage / 100;
    }

    public void setMaxOwnershipMoneyForPercentage(double maxOwnershipMoneyForPercentage) {
        this.maxOwnershipMoneyForPercentage = maxOwnershipMoneyForPercentage;
    }

    public double getNextExpectedPaymentAmountDataMember() {
        return nextExpectedPaymentAmountDataMember;
    }

    @Override
    public String toString() {
        return
                "Loan ID:" + loanID + "\n" +
                        "status: " + status + "\n" +
                        "borrower's Name: " + borrowerName + "\n" +
                        "loan Category: " + loanCategory + "\n" +
                        "Requested Time Frame For Loan: " + originalLoanTimeFrame + "\n" +
                        "Frequency of loan repayment requested: " + paymentFrequency + "\n" +
                        "Loan interest Percentage: " + interestPercentagePerTimeUnit + " %" + "\n" +
                        "Requested loan amount: " + loanOriginalDepth + "\n" + "\n";
    }

    /**
     * this func calculates how much yaz needs to pass for the next payment to be paid
     *
     * @return
     */
    public int calculateNextYazToPay() {
        int currTime = Timeline.getCurrTime();
        int startLoanYaz = this.startLoanYaz;
        int paymentFrequency = this.paymentFrequency;

        if (paymentFrequency == 1) {
            return 0;
        }

        if (currTime == 0) {
            return paymentFrequency;
        }

        return ((paymentFrequency - ((currTime - startLoanYaz) % paymentFrequency)) % paymentFrequency);
    }

    /**
     * this func returns the amount of money that is expected to be paid in the next yaz
     *
     * @return
     */


    public double calculateNextExpectedPaymentAmount(eDeviationPortion DeviationPortion) {
        //double intristPerPayment = this.originalInterest/this.originalLoanTimeFrame.getTimeStamp();
        switch (DeviationPortion) {
            case INTEREST: {
                if (deviation.getInterestDeviation() > 0)
                    return deviation.getInterestDeviation();
                else
                    return (this.intristPerPayment);
            }
            case FUND: {
                if (deviation.getFundDeviation() > 0)
                    return deviation.getFundDeviation();
                else
                    return (fundPerPayment);
            }
            case TOTAL: {
                if (deviation.getSumOfDeviation() > 0) {
                    return deviation.getSumOfDeviation();
                } else
                    return (totalLoanCostInterestPlusOriginalDepth / (double) (originalLoanTimeFrame / paymentFrequency));
            }
        }

        return -1000000000000.0;
    }


    /**
     * update the status of the loan from new or from pending or from activate. if changed to activate it starts up the loan
     */
    public void UpdateLoanStatusIfNeeded() {
        if ((!lendersList.isEmpty()) && (status == eLoanStatus.NEW)) {
            setStatus(eLoanStatus.PENDING);
        }
        if (loanAccount.getCurrBalance() == getLoanOriginalDepth()) {
            setStatus(eLoanStatus.ACTIVE);
            activateLoan();
        }
    }

    /**
     * starts up the loan to activate
     */
    public void activateLoan() {

        Client borrower = engine.returnClientByName(this.getBorrowerName());
        engine.TransferMoneyBetweenAccounts(loanAccount, loanOriginalDepth, borrower.getMyAccount());
        loanAccount.setCurrBalance(0);
        //Timeline startingLoanTimeStamp = new Timeline (Timeline.getCurrTime());
        startLoanYaz = Timeline.getCurrTime();
    }

    public void updateDynamicDataMembersAfterPayment(double interest, double fund) {
        totalRemainingLoan -= (interest + fund);
        payedInterest += interest;
        payedFund += fund;
    }

    /**
     * this func checks if the borrower can pay the next Expected Payment Amount and update the loan accordinly
     */
    public void handleLoanAfterTimePromote() {
        Client borrowerAsClient = engine.getDatabase().getClientMap().get(borrowerName);
        Account borrowerAccount = borrowerAsClient.getMyAccount();
        int currTimeStamp = Timeline.getCurrTime();
        Double nextExpectedPaymentAmount = calculateNextExpectedPaymentAmount(eDeviationPortion.TOTAL);
        Double nextExpectedInterest = calculateNextExpectedPaymentAmount(eDeviationPortion.INTEREST);
        Double nextExpectedFund = calculateNextExpectedPaymentAmount(eDeviationPortion.FUND);
        //if the borrower have the money for paying this loan at the time of the yaz
        if (borrowerAccount.getCurrBalance() >= nextExpectedPaymentAmount) {
            //add new payment to the loan payment list
            Payment BorrowPayment = new Payment(currTimeStamp, true, nextExpectedFund, nextExpectedInterest);
            paymentsList.add(BorrowPayment);
            //add the transaction stamp to the borrower transaction list
            Transaction transaction = new Transaction(currTimeStamp, -nextExpectedPaymentAmount, String.valueOf(this.loanID), borrowerAccount.getCurrBalance(), borrowerAccount.getCurrBalance() - nextExpectedPaymentAmount);
            borrowerAccount.addTnuaToAccount(transaction);
            //update loan money info
            loanAccount.setCurrBalance(loanAccount.getCurrBalance() + nextExpectedPaymentAmount);
            borrowerAccount.setCurrBalance(borrowerAccount.getCurrBalance() - nextExpectedPaymentAmount);

            updateDynamicDataMembersAfterPayment(nextExpectedInterest, nextExpectedFund);
            deviation.resetDeviation();
            //update loan status
            if (totalRemainingLoan == 0) {
                status = eLoanStatus.FINISHED;
                endLoanYaz = currTimeStamp;
                payLoanDividendsToLenders();
            } else if (status == eLoanStatus.RISK) {
                status = eLoanStatus.ACTIVE;
            }
        }
        //if the borrower does not have the money for paying this loan at the time of the yaz
        else {
            status = eLoanStatus.RISK;
            //add new payment to the loan payment list with false
            Payment BorrowPayment = new Payment(currTimeStamp, false, nextExpectedFund, nextExpectedInterest);
            paymentsList.add(BorrowPayment);
            //enlarge the deviation
            deviation.increaseDeviationBy(intristPerPayment, fundPerPayment);
        }
        nextExpectedPaymentAmountDataMember = calculateNextExpectedPaymentAmount(eDeviationPortion.TOTAL);
    }


    /**
     * function in charge of paying each lender is partial share of his investment in the loan.
     * DONT FORGET TO CHECK IF LOAN IS IN FINISHED STATUS BEFORE ENTERING FUNC
     */
    public void payLoanDividendsToLenders() {
        double amountToPayLender;
        //need to docu this variable -//DOCU calc the multiplier for getting the  amount of interest Slender should be payed
        double coefficientOfMultiplicationInterest = this.interestPercentagePerTimeUnit / 100.0;
        for (Lenders itr : this.lendersList) {
            //calc amount of money specific lender suppose to get after loan is in "FINISHED" status
            amountToPayLender = itr.getDeposit() + itr.getDeposit() * coefficientOfMultiplicationInterest;
            //getting curr lender to pay name
            String lendersNameToPay = itr.getFullName();
            //getting clients account
            Account accToPay = engine.getDatabase().getClientMap().get(lendersNameToPay).getMyAccount();
            //getting current timeStamp for transaction.
            int currTimeStamp = Timeline.getCurrTime();
            //updating lenders balance
            double updatedLenderBalance = accToPay.getCurrBalance() + amountToPayLender;
            //creating a transaction
            Transaction lenderPaymentTransAction = new Transaction(currTimeStamp, amountToPayLender, String.valueOf(this.loanID), accToPay.getCurrBalance(), updatedLenderBalance);
            //adding transaction to lenders account transactioList
            accToPay.getTnuaList().add(lenderPaymentTransAction);

            accToPay.setCurrBalance(updatedLenderBalance);

        }
    }


    public void uniformsNeededBlocksInLenderList() {
        int listSize = this.lendersList.size();
        //checks if their at least two blocks to compare in list
        if (listSize >= 2) {

            int index = listSize - 1;//last block
            while (this.lendersList.get(index).getFullName() == this.lendersList.get(index - 1).getFullName())//compares if two last blocks have the same name
            {
                //updates index-1 block deposit to be the unified sum of investment
                this.lendersList.get(index - 1).setDeposit(this.lendersList.get(index - 1).getDeposit() + this.lendersList.get(index).getDeposit());
                //removing unneeded block after updating prev block to new sum of inves
                this.lendersList.remove(index);
                //updating index to check pre compare
                index -= 1;
            }
        }
    }//MAYBE TO DELETE NOT YET

    public int calculateTotalOwnersPercentage() {
        totalRaisedDeposit = loanAccount.getCurrBalance();
        return (int) ((totalRaisedDeposit / loanOriginalDepth) * 100);
    }

    public int calculateClientLoanOwningPercentage(Client client) {
        double clientOwningSum = -1;
        for (Lenders lender : lendersList) {
            if (lender.getFullName().equals(client.getFullName())) {
                clientOwningSum = lender.getDeposit();
            }
        }
        return (int) ((double) (clientOwningSum / loanOriginalDepth) * 100);
    }


    public void paySingleLoanPayment() throws messageException {
        if (nextExpectedPaymentAmountDataMember == 0) {
            return;
        }
        Client borrowerAsClient = engine.getDatabase().getClientMap().get(borrowerName);
        Account borrowerAccount = borrowerAsClient.getMyAccount();
        int currTimeStamp = Timeline.getCurrTime();
        Double nextExpectedPaymentAmount = calculateNextExpectedPaymentAmount(eDeviationPortion.TOTAL);
        Double nextExpectedInterest = calculateNextExpectedPaymentAmount(eDeviationPortion.INTEREST);
        Double nextExpectedFund = calculateNextExpectedPaymentAmount(eDeviationPortion.FUND);
        //if the borrower have the money for paying this loan at the time of the yaz
        if (borrowerAccount.getCurrBalance() >= nextExpectedPaymentAmount) {
            //add new payment to the loan payment list
            Payment BorrowPayment = new Payment(currTimeStamp, true, nextExpectedFund, nextExpectedInterest);
            paymentsList.add(BorrowPayment);
            //add the transaction stamp to the borrower transaction list
            Transaction transaction = new Transaction(currTimeStamp, -nextExpectedPaymentAmount, String.valueOf(this.loanID), borrowerAccount.getCurrBalance(), borrowerAccount.getCurrBalance() - nextExpectedPaymentAmount);
            borrowerAccount.addTnuaToAccount(transaction);
            //update loan money info
            loanAccount.setCurrBalance(loanAccount.getCurrBalance() + nextExpectedPaymentAmount);
            borrowerAccount.setCurrBalance(borrowerAccount.getCurrBalance() - nextExpectedPaymentAmount);

            updateDynamicDataMembersAfterPayment(nextExpectedInterest, nextExpectedFund);
            deviation.resetDeviation();
            //update loan status
            if (totalRemainingLoan == 0) {
                status = eLoanStatus.FINISHED;
                endLoanYaz = currTimeStamp;
                payLoanDividendsToLenders();
            } else if (status == eLoanStatus.RISK) {
                status = eLoanStatus.ACTIVE;
            }
            nextExpectedPaymentAmountDataMember = 0;
        } else {
            throw new messageException("You do not have enough money to pay for :" + loanID);
        }
        //nextExpectedPaymentAmountDataMember = calculateNextExpectedPaymentAmount(eDeviationPortion.TOTAL);

    }

    public void setLoanToRisk() {
        Client borrowerAsClient = engine.getDatabase().getClientMap().get(borrowerName);
        Account borrowerAccount = borrowerAsClient.getMyAccount();
        int currTimeStamp = Timeline.getCurrTime();
        if (currTimeStamp == (paymentFrequency + 1)) {
            //enlarge the deviation
            deviation.increaseDeviationBy(intristPerPayment, fundPerPayment);
        }
        Double nextExpectedPaymentAmount = calculateNextExpectedPaymentAmount(eDeviationPortion.TOTAL);
        Double nextExpectedInterest = calculateNextExpectedPaymentAmount(eDeviationPortion.INTEREST);
        Double nextExpectedFund = calculateNextExpectedPaymentAmount(eDeviationPortion.FUND);


        status = eLoanStatus.RISK;
        //add new payment to the loan payment list with false
        Payment BorrowPayment = new Payment(currTimeStamp, false, nextExpectedFund, nextExpectedInterest);
        paymentsList.add(BorrowPayment);
        //enlarge the deviation
        deviation.increaseDeviationBy(intristPerPayment, fundPerPayment);
        //update data member
        nextExpectedPaymentAmountDataMember = calculateNextExpectedPaymentAmount(eDeviationPortion.TOTAL);
    }

    public void payEntireLoan() throws messageException {
        Client borrowerAsClient = engine.getDatabase().getClientMap().get(borrowerName);
        Account borrowerAccount = borrowerAsClient.getMyAccount();
        int currTimeStamp = Timeline.getCurrTime();
        Double nextExpectedPaymentAmount = totalRemainingLoan;
        Double nextExpectedInterest = originalInterest - payedInterest;
        Double nextExpectedFund = loanOriginalDepth - payedFund;

        //if the borrower have the money for paying this loan at the time of the yaz
        if (borrowerAccount.getCurrBalance() >= nextExpectedPaymentAmount) {
            //add new payment to the loan payment list
            Payment BorrowPayment = new Payment(currTimeStamp, true, nextExpectedFund, nextExpectedInterest);
            paymentsList.add(BorrowPayment);
            //add the transaction stamp to the borrower transaction list
            Transaction transaction = new Transaction(currTimeStamp, -nextExpectedPaymentAmount, String.valueOf(this.loanID), borrowerAccount.getCurrBalance(), borrowerAccount.getCurrBalance() - nextExpectedPaymentAmount);
            borrowerAccount.addTnuaToAccount(transaction);
            //update loan money info
            loanAccount.setCurrBalance(loanAccount.getCurrBalance() + nextExpectedPaymentAmount);
            borrowerAccount.setCurrBalance(borrowerAccount.getCurrBalance() - nextExpectedPaymentAmount);

            updateDynamicDataMembersAfterPayment(nextExpectedInterest, nextExpectedFund);
            deviation.resetDeviation();
            //update loan status
            if (totalRemainingLoan == 0) {
                status = eLoanStatus.FINISHED;
                endLoanYaz = currTimeStamp;
                payLoanDividendsToLenders();
            } else if (status == eLoanStatus.RISK) {
                status = eLoanStatus.ACTIVE;
            }
            nextExpectedPaymentAmountDataMember = 0;
        } else {
            throw new messageException("You do not have enough money to pay for :" + loanID);
        }
        //nextExpectedPaymentAmountDataMember = calculateNextExpectedPaymentAmount(eDeviationPortion.TOTAL);

    }


    public void payPartialLoanPayment(int amount) throws messageException {
        double loanAmount=calculateNextExpectedPaymentAmount(eDeviationPortion.TOTAL);
        if(amount==loanAmount){
            paySingleLoanPayment();
            return;
        }
        if (amount>loanAmount){
            throw new messageException("You can only pay for the deviation or less! :\n" + loanAmount);
        }
        Client borrowerAsClient = engine.getDatabase().getClientMap().get(borrowerName);
        Account borrowerAccount = borrowerAsClient.getMyAccount();
        int currTimeStamp = Timeline.getCurrTime();
        Double nextExpectedPaymentAmount = Double.valueOf(amount);
        Double nextExpectedInterest = ((double)originalInterest/totalLoanCostInterestPlusOriginalDepth)*amount;
        Double nextExpectedFund =((double)loanOriginalDepth/totalLoanCostInterestPlusOriginalDepth)*amount;

        //if the borrower have the money for paying this loan at the time of the yaz
        if (borrowerAccount.getCurrBalance() >= nextExpectedPaymentAmount) {
            //add new payment to the loan payment list
            Payment BorrowPayment = new Payment(currTimeStamp, true, nextExpectedFund, nextExpectedInterest);
            paymentsList.add(BorrowPayment);
            //add the transaction stamp to the borrower transaction list
            Transaction transaction = new Transaction(currTimeStamp, -nextExpectedPaymentAmount, String.valueOf(this.loanID), borrowerAccount.getCurrBalance(), borrowerAccount.getCurrBalance() - nextExpectedPaymentAmount);
            borrowerAccount.addTnuaToAccount(transaction);
            //update loan money info
            loanAccount.setCurrBalance(loanAccount.getCurrBalance() + nextExpectedPaymentAmount);
            borrowerAccount.setCurrBalance(borrowerAccount.getCurrBalance() - nextExpectedPaymentAmount);

            updateDynamicDataMembersAfterPayment(nextExpectedInterest, nextExpectedFund);
            //todo: be carefull here:
            //deviation.resetDeviation();
            deviation.changeDeviation(nextExpectedInterest,nextExpectedFund);
            //update loan status
            if (totalRemainingLoan == 0) {
                status = eLoanStatus.FINISHED;
                endLoanYaz = currTimeStamp;
                payLoanDividendsToLenders();
            }
            nextExpectedPaymentAmountDataMember-=amount;
        } else {
            throw new messageException("You do not have enough money to pay for :" + loanID);
        }
    }
}