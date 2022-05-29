package time;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.value.ObservableValue;

import java.io.Serializable;
import java.util.Objects;

public class Timeline implements Serializable {

    private static int currTime = 0;
    private static SimpleIntegerProperty currTimePropery = new SimpleIntegerProperty(currTime);
    private int timeStamp;

    public Timeline() {
    }

    @Override
    public String toString() {
        return String.valueOf(timeStamp);
    }

    public Timeline(int timeStamp) {
        this.timeStamp = timeStamp;
    }
    public int getTimeStamp() {
        return timeStamp;
    }
    public void setTimeStamp(int timeStamp) {
        this.timeStamp = timeStamp;
    }
    public static int getCurrTime() {
        return currTime;
    }



    public static ObservableValue<? extends Number> getObservableCurrTime() {
        ObservableValue<Integer> obsInt = new ReadOnlyObjectWrapper<>(currTime);
        return obsInt;
    }

    public static IntegerProperty getCurrTimePropery() {
        return currTimePropery;
    }

    public static SimpleIntegerProperty currTimeProperyProperty() {
        return currTimePropery;
    }

    public static void promoteStaticCurrTime(){
        currTime++;
        currTimePropery.set(currTime);
    }

    public static void setCurrTime(int currTime) {
        Timeline.currTime = currTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Timeline timeline = (Timeline) o;
        return timeStamp == timeline.timeStamp;
    }

    @Override
    public int hashCode() {
        return Objects.hash(timeStamp);
    }

    public static void printStaticCurrTime(){
        System.out.println(currTime);
    }
    public static void printPreviousCurrTime(){
        if (currTime==1)
        {
            System.out.println(currTime);
        }
        else {
            System.out.println(currTime-1);
        }
    }
    public static void resetTime(){
        currTime =0;
    }
}
