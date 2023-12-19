package coindesk.entity;

import javax.persistence.*;

@Entity
@Table(name = "coin")
public class Coin {


    @Column(name = "coin_id", length = 3, nullable = false)
    @Id
    private String coinId;

    @Column(name = "coin_name", length = 10, nullable = false)
    private String coinName;

    public Coin() {
    }

    public Coin(String coinId, String coinName) {
        this.coinId = coinId;
        this.coinName = coinName;
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

    @Override
    public String toString() {
        return "Coin{" +
                "coinId='" + coinId + '\'' +
                ", coinName='" + coinName + '\'' +
                '}';
    }
}
