package coindesk.entity;

import java.util.List;

public class NewAPIResponse {
    private String updateTime;

    private List<NewCoin> newCoins;

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    public List<NewCoin> getNewCoins() {
        return newCoins;
    }

    public void setNewCoins(List<NewCoin> newCoins) {
        this.newCoins = newCoins;
    }

    @Override
    public String toString() {
        return "NewAPIResponse{" +
                "updateTime=" + updateTime +
                ", newCoins=" + newCoins +
                '}';
    }
}
