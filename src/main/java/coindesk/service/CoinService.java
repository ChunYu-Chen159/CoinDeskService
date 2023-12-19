package coindesk.service;

import coindesk.entity.Coin;
import coindesk.entity.NewAPIResponse;
import coindesk.entity.NewCoin;
import coindesk.repository.CoinRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service
public class CoinService {

    private final CoinRepository coinRepository;
    public CoinService(CoinRepository coinRepository) {
        this.coinRepository = coinRepository;
    }

    public NewAPIResponse processBpiJson(String jsonResponse) {
        NewAPIResponse newAPIResponse = new NewAPIResponse();
        List<NewCoin> newCoins = new ArrayList<>();

        try {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonNode = objectMapper.readTree(jsonResponse);

            // 取得更新時間
            String updatedISO = jsonNode.get("time").get("updatedISO").asText();

            // 轉換日期格式
            DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
            ZonedDateTime updatedISODate = ZonedDateTime.parse(updatedISO, DateTimeFormatter.ISO_OFFSET_DATE_TIME);
            String outputDate = updatedISODate.format(outputFormatter);

            System.out.println("outputDate: " + outputDate);

            newAPIResponse.setUpdateTime(outputDate);

            // 取得幣別
            JsonNode bpiNode = jsonNode.path("bpi");
            if (bpiNode.isObject()) {
                bpiNode.fields().forEachRemaining(entry -> {
                    String coinId = entry.getKey();

                    // 根據coinId查詢資料庫，獲取中文名稱
                    Coin coin = coinRepository.findByCoinId(coinId);
                    String coinName = coin != null ? coin.getCoinName() : "Unknown";

                    newCoins.add( new NewCoin(coinId, coinName, entry.getValue().path("rate_float").asDouble()) );
                });

                newAPIResponse.setNewCoins(newCoins);
            }

            return newAPIResponse;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return new NewAPIResponse();
    }


    public String extractUpdateTimeFromJson(String jsonResponse) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonNode = objectMapper.readTree(jsonResponse);

            // 取得更新時間
            JsonNode timeNode = jsonNode.path("time");
            String updateTime = timeNode.path("updated").asText();

            return updateTime;
        } catch (Exception e) {
            e.printStackTrace();
            return "Error extracting update time";
        }
    }
}
