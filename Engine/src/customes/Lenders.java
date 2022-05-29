package customes;


import java.io.Serializable;
import java.util.Objects;
public class Lenders implements Serializable {

    private String fullName;
    private double deposit;

    public String getFullName() {
        return fullName;
    }




    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public double getDeposit() {
        return deposit;
    }

    public Lenders(String fullName, double deposit) {
        this.fullName = fullName;
        this.deposit = deposit;
    }

    public void setDeposit(double deposit) {
        this.deposit = deposit;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Lenders lenders = (Lenders) o;
        return fullName.equals(lenders.fullName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(fullName);
    }

    @Override
    public String toString() {
        return "Lenders{" +
                "fullName: " + fullName + '\'' +
                ", deposit: " + deposit +
                '}';
    }



}

