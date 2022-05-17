package Money;


import java.io.Serializable;

public class Deviation implements Serializable {
    double interestDeviation=0;
    double fundDeviation=0;
    //double sumOfDeviation=0;
    int numberOfYazNotPayed=0;


    public int getNumberOfYazNotPayed() {
        return numberOfYazNotPayed;
    }
    public final double getSumOfDeviation(){
        return interestDeviation+fundDeviation;
    }

    public double getInterestDeviation() {
        return interestDeviation;
    }

    public double getFundDeviation() {
        return fundDeviation;
    }

    public void incrementTheNumberOfYazNotPayed(){this.numberOfYazNotPayed++;}
    public void increaseDeviationBy(double interest,double fund){
        interestDeviation+=interest;
        fundDeviation+=fund;
        //sumOfDeviation +=Amount;
        incrementTheNumberOfYazNotPayed();
    }

    public Deviation() {
     this.resetDeviation();
    }

    public void resetDeviation(){
        numberOfYazNotPayed=0;
        interestDeviation=0;
        fundDeviation=0;
    }
}
