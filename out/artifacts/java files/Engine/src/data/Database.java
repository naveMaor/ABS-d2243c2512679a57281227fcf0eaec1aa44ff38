package data;

import loan.Loan;
import customes.Client;
import time.Timeline;
import utills.BackgroundFunc;

import java.io.Serializable;
import java.util.*;

import static data.File.XmlFile.*;
public class Database implements Serializable {
    private static Map <String, List<Loan>> loanMapByCategory = new HashMap<>();
    private static Map<String, Client> clientMap =new HashMap<>();

    public static void setLoanMapByCategory(Map<String, List<Loan>> loanMapByCategory) {
        Database.loanMapByCategory = loanMapByCategory;
    }

    public static void setClientMap(Map<String, Client> clientMap) {
        Database.clientMap = clientMap;
    }

    public static Map<String, List<Loan>> getLoanMapByCategory() {
        return loanMapByCategory;
    }

    public static List<Loan> getLoanList() {
        List<Loan> result = new ArrayList<>();
        for (List<Loan> loanList: loanMapByCategory.values()) {
            result.addAll(loanList);
        }
        return result;
        //return new ArrayList<>(loanMap.values());
    }
    public static void addLoanToLoanMap(Loan newLoanNode){
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
    public static List<Client> getClientsList() {
        return new ArrayList<>(clientMap.values());
    }
//TO ASK: NOT SUPPOSE TO BE ADD TO MAP ? ADD TO DATABASE ?
    public static void addClientToClientMap(Client newClientNode){
        clientMap.put(newClientNode.getFullName(), newClientNode);
    }
    public static List<Loan> getSortedLoanList(){
        List <Loan> result = getLoanList();
        BackgroundFunc.orderLoanList(result);
        return result;
    }
    public static Map<String, Client> getClientMap() {
        return clientMap;
    }
    public static void addCategory (String category){
        if (!loanMapByCategory.containsKey(category))
        {
            loanMapByCategory.put(category,new ArrayList<Loan>());
        }
    }

    public static List<String> getAllCategories() {
        List<String> result = new ArrayList<>();
        for (String category:loanMapByCategory.keySet()) {
            result.add(category);
        }
        return result;
    }

    public static void clearAll(){
        loanMapByCategory.clear();
        clientMap.clear();
        Timeline.resetTime();
        //resetFileData();
    }

}
