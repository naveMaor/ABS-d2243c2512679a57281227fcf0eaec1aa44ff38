package data;

import customes.Client;
import engine.Engine;
import loan.Loan;
import servletDTO.BuyLoanObj;
import time.Timeline;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class SaveSystemData {

    private Map<String, List<Loan>> loanMapByCategory;
    private Map<String, Client> clientMap;
    private int currTime;
    private Map<String,List<BuyLoanObj>> loanOnSale;
    private boolean isAdminConnected;
    private Set<String> adminSet =new HashSet<>();


    public SaveSystemData(int yaz, Database database) {
        this.loanMapByCategory = database.getLoanMapByCategory();
        this.clientMap = database.getClientMap();
        this.currTime = yaz;
        this.loanOnSale = database.getLoanOnSale();
        this.isAdminConnected = database.isAdminConnected();
        this.adminSet =database.getAdminSet();
    }

    public Map<String, List<Loan>> getLoanMapByCategory() {
        return loanMapByCategory;
    }

    public Map<String, Client> getClientMap() {
        return clientMap;
    }

    public int getCurrTime() {
        return currTime;
    }

    public Map<String, List<BuyLoanObj>> getLoanOnSale() {
        return loanOnSale;
    }

    public boolean isAdminConnected() {
        return isAdminConnected;
    }

    public Set<String> getAdminSet() {
        return adminSet;
    }
}
