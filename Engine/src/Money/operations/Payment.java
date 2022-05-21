package Money.operations;

import time.Timeline;

import java.io.Serializable;

public class Payment implements Serializable {

    private double fundPortion;
    private double interestPortion;
    private int paymentYaz;
    private double fundPlusInterest;
    private boolean isPayed;

    public boolean isPayed() {
        return isPayed;
    }

    public void setPayed(boolean payed) {
        isPayed = payed;
    }
    public double getFundPlusInterest() {
        return fundPlusInterest;
    }
    public void setFundPlusInterest(int fundPlusInterest) {
        this.fundPlusInterest = fundPlusInterest;
    }
/*    public int getFundPortion() {
        return fundPortion;
    }*/
    public Payment() {
    }

    public Payment(int paymentYaz, boolean isPayed,double fundPortion,double interestPortion) {
        this.fundPortion = fundPortion;
        this.interestPortion = interestPortion;
        this.paymentYaz = paymentYaz;
        this.fundPlusInterest = fundPortion+interestPortion;
        this.isPayed = isPayed;
    }

/*
    public void setFundPortion(int fundPortion) {
        this.fundPortion = fundPortion;
    }
    public int getInterestPortion() {
        return interestPortion;
    }
    public void setInterestPortion(int interestPortion) {
        this.interestPortion = interestPortion;
    }
*/

    public int getPaymentYaz() {
        return paymentYaz;
    }
    public void setPaymentYaz(int paymentYaz) {
        this.paymentYaz = paymentYaz;
    }

    @Override
    public String toString() {
        String result = "payment made at: " + paymentYaz + " Yaz\n";
        result += "Fund Portion: " + fundPortion + "\n";
        result += "interest Portion: " + interestPortion + "\n";
        result += "total payment: " + fundPlusInterest + "\n";
        String payed = isPayed ? "" : "Not Payed!";
        result += payed;
        return result;
/*        return "{" +
                ", payment made at: " + paymentYaz + "Yaz"+
                "Fund Portion: " + fundPortion +
                ", interest Portion: " + interestPortion +
                ", total payment: " + fundPlusInterest +
                 payed +
                '}';*/
    }
}
