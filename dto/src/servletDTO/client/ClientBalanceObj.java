package servletDTO.client;

public class ClientBalanceObj {


    private String fullName;
    private double clientBalance =0;

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public void setClientBalance(double clientBalance) {
        this.clientBalance = clientBalance;
    }

    public String getFullName() {
        return fullName;
    }

    public double getClientBalance() {
        return clientBalance;
    }

    public ClientBalanceObj(String fullName, double clientBalance) {
        this.fullName = fullName;
        this.clientBalance = clientBalance;
    }
}
