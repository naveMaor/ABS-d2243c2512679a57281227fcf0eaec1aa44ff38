package exceptions;

public class BalanceException extends Exception{

    public BalanceException(String str){
        //String str = "Customer can not be in minus";
        super(str);
    }
}
