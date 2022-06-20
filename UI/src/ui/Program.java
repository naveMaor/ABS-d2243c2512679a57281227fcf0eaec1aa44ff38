package ui;

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
