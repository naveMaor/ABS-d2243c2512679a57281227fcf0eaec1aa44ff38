package customes;

import Money.operations.Transaction;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;
public class Account implements Serializable {
    private int ID;
    private double currBalance =0;
    private List<Transaction> transactionList = new ArrayList<>();
//ctors
    public Account(int ID, double currBalance) {
        this.ID = ID;
        this.currBalance = currBalance;
    }
    //NEED TO SECURE ONLY ONE ID PER ACCOUNT IN PROGRAM




    @Override
    public String toString() {
        return "Account{" +
                "ID=" + ID +
                ", currBalance=" + currBalance +
                ", transactionList=" + transactionList +
                '}';
    }

    //getters and setters:
    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public double getCurrBalance() {
        return currBalance;
    }

    public void setCurrBalance(double currBalance) {
        this.currBalance = currBalance;
    }

    public List<Transaction> getTnuaList() {
        return transactionList;
    }
/*    public void setTnuaList(List<Tnua> argsList) {
        this.tnuaList = argsList;
    }*/

    public void addTnuaToAccount(Transaction transaction) {

        //TODO: check if needed to be added in specific location
        this.transactionList.add(transaction);
    }

}
