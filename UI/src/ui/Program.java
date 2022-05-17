package ui;
import customes.Account;
import customes.Client;
import data.Database;
import loan.Loan;
import loan.enums.eLoanStatus;
import time.Timeline;

public class Program {
    public static void main (String[] args){
        int choice=0;
        Menu menu = new Menu();
        menu.welcome();
        while (choice!=10){
            choice = menu.printMenu();
        }
    }

}
