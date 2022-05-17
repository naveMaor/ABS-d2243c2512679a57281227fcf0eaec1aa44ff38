package ui;

import data.File.DataToFileDataFromFile;
import ui.PrintFuncs;

public class Menu {
    private static boolean isLoadedFile = false;
    private static int choise =0;

    public static void welcome(){
        System.out.println("Please choose from the menu below");
    }
    public static int printMenu(){
        System.out.println("1. Load new Xml file");
        System.out.println("2. Present the information on existing loans and their status");
        System.out.println("3. Display information about all customers");
        System.out.println("4. Load money into account");
        System.out.println("5. withdraw money from account");
        System.out.println("6. place a certain loan to a client as lender");
        System.out.println("7. Promote timeline by one Yaz");
        System.out.println("8. save current state as bin");
        System.out.println("9. load last saved state from bin");
        System.out.println("10. exit");
        try {
            choise =  handleMenuChoice();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return choise;
        }
        return choise;
    }

    public static int handleMenuChoice() throws Exception {
        int choice = PrintFuncs.readIntFromUser(1,10,true);

        if (choice==1){
            User_interface.func1();
            isLoadedFile = true;
        }
        else if (choice == 10)
        {
            System.out.println("Bye, have a nice day!");
            return choice;
        }
        if (choice==9){
            try {
                DataToFileDataFromFile dataToFileDataFromFile = new DataToFileDataFromFile();
                DataToFileDataFromFile.LoadDataFromFile(dataToFileDataFromFile);
                System.out.println("data loaded successfully");
                isLoadedFile = true;
            }
            catch( Exception e) {

                System.out.println("Couldn't load date from file or file doesnt exist!");
            }
        }
        else if (!isLoadedFile){
            throw new Exception("File must be loaded first please choose ONE first!");
        }
        else
        {
            switch (choice){
                case 2:{
                    User_interface.func2AllLoansData();
                    break;
                }
                case 3:{
                    User_interface.func3();
                    break;
                }
                case 4:{
                    User_interface.func4();
                    break;
                }
                case 5:{
                    User_interface.func5();
                    break;
                }
                case 6:{
                    User_interface.func6();
                    break;
                }
                case 7:{
                    User_interface.func7();
                    break;
                }
                case 8:{
                    DataToFileDataFromFile dataToFileDataFromFile = new DataToFileDataFromFile();
                    dataToFileDataFromFile.SaveDataToFile();
                    System.out.println("data saved successfully");
                    break;
                }
            }
        }

        return choice;
    }

}
