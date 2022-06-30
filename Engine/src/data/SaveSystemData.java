package data;

import customes.Client;
import engine.Engine;
import loan.Loan;
import servletDTO.BuyLoanObj;
import time.Timeline;

import java.util.*;

public class SaveSystemData {

    private Map<String, List<Loan>> loanMapByCategory =new HashMap<>();
    private Map<String, Client> clientMap = new HashMap<>();
    private int currTime;
    private Map<String,List<BuyLoanObj>> loanOnSale= new HashMap<>();
    private boolean isAdminConnected;
    private Set<String> adminSet =new HashSet<>();


    public SaveSystemData(int yaz, Database database) {
        for(Loan loan:database.getLoanList()){
            Loan newLoan = new Loan(loan);
            addLoanToLoanMap(newLoan);
        }
        for(Client client:database.getClientsList()){
            Client newClient = new Client(client);
            addClientToClientMap(newClient);
        }
        this.currTime = yaz;
        this.loanOnSale = copyLoanOnSaleMap(database.getLoanOnSale());
        this.isAdminConnected = database.isAdminConnected();
        this.adminSet =new HashSet<>(database.getAdminSet());
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


    private void addLoanToLoanMap(Loan newLoanNode){
        String category= newLoanNode.getLoanCategory();
        if(loanMapByCategory.containsKey(category))
        {
            loanMapByCategory.get(category).add(newLoanNode);
        }
        else
        {
            List<Loan> newLoanlist = new ArrayList<>();
            newLoanlist.add(newLoanNode);
            loanMapByCategory.put(category,newLoanlist);
        }
        Client LoanBorrower = clientMap.get(newLoanNode.getBorrowerName());
        LoanBorrower.addLoanAsBorrower(newLoanNode);
    }

    private void addClientToClientMap(Client newClientNode){
        clientMap.put(newClientNode.getFullName(), newClientNode);
    }

    private Map<String,List<BuyLoanObj>> copyLoanOnSaleMap(Map<String,List<BuyLoanObj>> other){
        List<BuyLoanObj> newBuyLoanObjList = new ArrayList<>();
        Map<String,List<BuyLoanObj>> result= new HashMap<>();
        for (Map.Entry<String, List<BuyLoanObj>> entry : other.entrySet()) {
            List<BuyLoanObj> BuyLoanObjList = entry.getValue();
            for(BuyLoanObj buyLoanObj:BuyLoanObjList){
                BuyLoanObj newbuyLoan = new BuyLoanObj(buyLoanObj);
                newBuyLoanObjList.add(newbuyLoan);
            }
            result.put(entry.getKey(),newBuyLoanObjList);
        }
        return result;
    }
}
