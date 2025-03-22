public class Price {
    private String currency;
    private double amount;

    public Price(String currency, double amount) {
        this.currency = currency;
        this.amount = amount;
    }


    @Override
    public String toString() {
        return amount + " " + currency;
    }
}
