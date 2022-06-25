package servletDTO;

import javafx.collections.ObservableList;
import loan.Loan;

import java.util.List;

public class ScrambleRequestObj {
   private List<LoanInformationObj> loansListToInvest;
    private double wantedInvestment;
    private String clientName;
    private int maxPercentage;

    public ScrambleRequestObj(ObservableList<LoanInformationObj> loanslistToInvest, double wantedInvestment, String clientName, int maxPercentage) {
        this.loansListToInvest = loanslistToInvest;
        this.wantedInvestment = wantedInvestment;
        this.clientName = clientName;
        this.maxPercentage = maxPercentage;
    }

    public List<LoanInformationObj> getLoansListToInvest() {
        return loansListToInvest;
    }

    public double getWantedInvestment() {
        return wantedInvestment;
    }

    public String getClientName() {
        return clientName;
    }

    public int getMaxPercentage() {
        return maxPercentage;
    }
}