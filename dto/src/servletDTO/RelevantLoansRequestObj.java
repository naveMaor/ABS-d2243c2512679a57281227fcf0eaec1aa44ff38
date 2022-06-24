package servletDTO;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import loan.Loan;

import java.util.List;

public class RelevantLoansRequestObj {

    private String clientName;
    private List<String> ChosenCategories;
    private int minInterest;
    private int minYaz;
    private int maxOpenLoans;
    private int maxOwnership;

    public RelevantLoansRequestObj(String clientName, List<String> chosenCategories, int minInterest, int minYaz, int maxOpenLoans, int maxOwnership) {
        this.clientName = clientName;
        this.ChosenCategories = chosenCategories;
        this.minInterest = minInterest;
        this.minYaz = minYaz;
        this.maxOpenLoans = maxOpenLoans;
        this.maxOwnership = maxOwnership;
    }


    public String getClientName() {
        return clientName;
    }

    public ObservableList<String> getChosenCategories() {
        ObservableList<String> observableList = FXCollections.observableArrayList();
        observableList.addAll(ChosenCategories);
        return observableList;

    }

    public int getMinInterest() {
        return minInterest;
    }

    public int getMinYaz() {
        return minYaz;
    }

    public int getMaxOpenLoans() {
        return maxOpenLoans;
    }

    public int getMaxOwnership() {
        return maxOwnership;
    }


}
