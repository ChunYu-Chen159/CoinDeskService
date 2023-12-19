package coindesk.entity;

import javax.persistence.Entity;

public class NewCoin {
    private String coinId;
    private String coinName;
    private double rate;

    public NewCoin(String coinId, String coinName, double rate) {
        this.coinId = coinId;
        this.coinName = coinName;
        this.rate = rate;
    }

    public String getCoinId() {
        return coinId;
    }

    public void setCoinId(String coinId) {
        this.coinId = coinId;
    }

    public String getCoinName() {
        return coinName;
    }

    public void setCoinName(String coinName) {
        this.coinName = coinName;
    }

    public double getRate() {
        return rate;
    }

    public void setRate(float rate) {
        this.rate = rate;
    }

    @Override
    public String toString() {
        return "NewCoin{" +
                "coinId='" + coinId + '\'' +
                ", coinName='" + coinName + '\'' +
                ", rate=" + rate +
                '}';
    }
}