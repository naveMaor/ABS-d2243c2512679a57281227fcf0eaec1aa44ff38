package data;

import old.ClientObj;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import loan.Loan;
import customes.Client;
import loan.enums.eDeviationPortion;
import loan.enums.eLoanStatus;
import old.LoanObj;
import servletDTO.BuyLoanObj;
import time.Timeline;

import java.io.IOException;
import java.io.Serializable;
import java.util.*;

public class Database implements Serializable {

    private static Database single_instance = null;
    public static Database Database()
    {
        // To ensure only one instance is created
        if (single_instance == null) {
            single_instance = new Database();
        }
        return single_instance;
    }

    private Map <String, List<Loan>> loanMapByCategory = new HashMap<>();
    private Map<String, Client> clientMap =new HashMap<>();
    //key of map is client name, value is list map of loan buy object
    private Map<String,List<BuyLoanObj>> loanOnSale= new HashMap<>();

    public void setLoanMapByCategory(Map<String, List<Loan>> loanMapByCategory) {
        loanMapByCategory = loanMapByCategory;
    }
    public void setClientMap(Map<String, Client> clientMap) {
        clientMap = clientMap;
    }


    public Map<String, List<Loan>> getLoanMapByCategory() {
        return loanMapByCategory;
    }

    public List<LoanObj> getLoanObjList() {
        List<LoanObj> result = new ArrayList<>();
        for (List<Loan> loanList: loanMapByCategory.values()) {
            for(Loan newloan: loanList){
                result.add(new LoanObj(newloan));
            }
            //result.addAll(loanList);
        }
        return result;
    }
    public List<Loan> getLoanList() {
        List<Loan> result = new ArrayList<>();
        for (List<Loan> loanList: loanMapByCategory.values()) {
            result.addAll(loanList);
        }
        return result;

    }

    public Loan getLoanById(String loanId) {
        List<Loan> loansList = new ArrayList<>(getLoanList());
        for(Loan loan: loansList)
        {
            if(loanId.equals(loan.getLoanID()))
            {
                return loan;
            }
        }
        return null;

    }

    public void addLoanToLoanMap(Loan newLoanNode){
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
    public List<Client> getClientsList() {
        return new ArrayList<>(clientMap.values());
    }
    public List<ClientObj> getClientsObjList() {
        List<ClientObj> result = new ArrayList<>();
        for (Client clientToCopy: clientMap.values()) {
                result.add(new ClientObj(clientToCopy));
            }
        return result;
    }
//TO ASK: NOT SUPPOSE TO BE ADD TO MAP ? ADD TO DATABASE ?
    public void addClientToClientMap(Client newClientNode){
        clientMap.put(newClientNode.getFullName(), newClientNode);
    }
    public List<Loan> getSortedLoanList(){
        List <Loan> result = getLoanList();
        orderLoanList(result);
        return result;
    }
    public Map<String, Client> getClientMap() {
        return clientMap;
    }
    public void addCategory (String category){
        if (!loanMapByCategory.containsKey(category))
        {
            loanMapByCategory.put(category,new ArrayList<Loan>());
        }
    }
    public List<String> getAllCategories() {
        List<String> result = new ArrayList<>();
        for (String category:loanMapByCategory.keySet()) {
            result.add(category);
        }
        return result;
    }



    public Client getClientByname(String name){
        return clientMap.get(name);
    }

    public void clearAll(){
        loanMapByCategory.clear();
        clientMap.clear();
        Timeline.resetTime();
        //resetFileData();
    }

    /**
     * this fun get loan list and order it by two parameters:
     * getStartLoanYaz and then nextExpectedPaymentAmount
     * @param LoanList
     */
    public void orderLoanList(List<Loan> LoanList) {

        Collections.sort(LoanList, new Comparator() {

            public int compare(Object o1, Object o2) {

                Integer x1 = ((Loan) o1).getStartLoanYaz();
                Integer x2 = ((Loan) o2).getStartLoanYaz();
                int sComp = x1.compareTo(x2);

                if (sComp != 0) {
                    return sComp;
                }

                Double x3 = ((Loan) o1).calculateNextExpectedPaymentAmount(eDeviationPortion.TOTAL);
                Double x4 = ((Loan) o2).calculateNextExpectedPaymentAmount(eDeviationPortion.TOTAL);
                return x3.compareTo(x4);
            }});
    }

    public ObservableList<String> o_getAllClientNames(){
        List<String> clientNameList = new ArrayList<>(clientMap.keySet());
        ObservableList<String> result = FXCollections.observableArrayList(clientNameList);

        return result;
    }

    public ObservableList<Loan> o_getAllLoansByStatus(eLoanStatus status){
        List<Loan> loanList = getLoanList();
        ObservableList <Loan> result =  FXCollections.observableArrayList();
        for (Loan loan:loanList){
            if(loan.getStatus().equals(status)){
                result.add(loan);
            }
        }
        return result;
    }
    public ObservableList<Loan> o_getAllLoansByClientName(String name){
        List<Loan> loanList = getLoanList();
        ObservableList <Loan> result =  FXCollections.observableArrayList();
        for (Loan loan:loanList){
            if(loan.getBorrowerName().equals(name)){
                result.add(loan);
            }
        }
        return result;
    }

    public boolean isUserExists(String usernameFromParameter){
        return clientMap.containsKey(usernameFromParameter);
    }

    public void addUser(String usernameFromParameter){
        Client newClient = new Client(usernameFromParameter,0);
        addClientToClientMap(newClient);
    }

    public List<String> getLoanNameList(){
        List<Loan> loanList = getLoanList();
        List<String> result = new ArrayList<>();
        for (Loan loan:loanList){
            result.add(loan.getLoanID());
        }
        return result;
    }

    public Map<String,List<BuyLoanObj>> getLoanOnSale() {
        return loanOnSale;
    }

    public void putLoanOnSale(String clientName, BuyLoanObj buyLoanObj) throws IOException {
        if(loanOnSale.containsKey(clientName))
        {
            List<BuyLoanObj> buyLoanObjList= loanOnSale.get(clientName);
            List<String> tmpList = new ArrayList<>();
            for (BuyLoanObj buyLoanObjForname:buyLoanObjList){
                tmpList.add(buyLoanObjForname.getLoanID());
            }
            if(!tmpList.contains(buyLoanObj.getLoanID()))
                buyLoanObjList.add(buyLoanObj);
            else
                throw new IOException("LOAN IS ALREADY ON SALE");
        }
        else
        {
            List<BuyLoanObj> newSellLoanlist = new ArrayList<>();
            newSellLoanlist.add(buyLoanObj);
            loanOnSale.put(clientName,newSellLoanlist);
        }
        //getLoanById(buyLoanObj.getLoanID()).setOnSale(true);
    }

}
