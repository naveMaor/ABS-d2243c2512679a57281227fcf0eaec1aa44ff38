package Money.operations;

import java.io.Serializable;
import java.util.Objects;
public class Transaction implements Serializable {

    private int timeOfMovement;// time of the movemonet was occured
    private double sum;// schum tnua
    private String to_from;
    private  char sign;
    private double balanceBefore;
    private double balanceAfter;

    public Transaction(int timeOfMovement, double sum, String to_from, double balanceBefore,double balanceAfter) {
        this.timeOfMovement = timeOfMovement;
        this.sum = sum;
        this.to_from=to_from;
        if(this.sum>0)
            this.sign ='+';
        else
            this.sign ='-';
        this.balanceBefore= balanceBefore;
        this.balanceAfter=balanceAfter;
    }

    public void setBalanceAfter(double balanceAfter) {
        this.balanceAfter = balanceAfter;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Transaction that = (Transaction) o;
       boolean shai = (this.sign == that.sign && timeOfMovement==that.timeOfMovement && to_from.equals(that.to_from));

        return shai ;
    }           // same "Sign" of transaction +/-

    @Override
    public int hashCode() {
        return Objects.hash(timeOfMovement, to_from);
    }

    public String getTo_from() {
        return to_from;
    }

    @Override
    public String toString() {
        String result = "time transaction made:" + timeOfMovement + "\n";
        result += "transaction amount: " + sum + "\n";

        return result;
/*        return "Transaction{" +
                "timeOfMovement=" + timeOfMovement +
                ", sum=" + sum +
                ", to_from='" + to_from + '\'' +
                '}';*/
    }


    public int getTimeOfMovement() {
        return timeOfMovement;
    }

    public void setBalanceBefore(double balanceBefore) {
        this.balanceBefore = balanceBefore;
    }

    public double getBalanceBefore() {
        return balanceBefore;
    }

    public double getBalanceAfter() {
        return balanceAfter;
    }

    public void setTimeOfMovement(int timeOfMovement) {
        this.timeOfMovement = timeOfMovement;
    }

    public double getSum() {
        return sum;
    }

    public void setSum(double sum) {
        this.sum = sum;
    }



}
