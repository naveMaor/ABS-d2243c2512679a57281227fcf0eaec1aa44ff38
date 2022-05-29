package Money;


import java.io.Serializable;

public class Deviation implements Serializable {
    double interestDeviation=0;
    double fundDeviation=0;
    //double sumOfDeviation=0;
    int numberOfYazNotPayed=0;
    boolean skipped = false;

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

    public boolean isSkipped() {
        return skipped;
    }

    public void setSkipped(boolean skipped) {
        this.skipped = skipped;
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
        skipped = false;
    }


}
