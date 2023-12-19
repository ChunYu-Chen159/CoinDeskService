package coindesk;

import coindesk.entity.Coin;
import coindesk.entity.NewAPIResponse;
import coindesk.service.CoinService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import coindesk.repository.CoinRepository;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@RestController
public class CoinDeskController {

    private final String coindeskApiUrl = "https://api.coindesk.com/v1/bpi/currentprice.json";

    @Autowired
    private CoinRepository coinRepository;

    @Autowired
    private CoinService coinService;

    // 根測試
    @GetMapping(value = "/")
    public String index() {return "success";}

    // New API
    @GetMapping(value = "/newCoinDesk")
    public NewAPIResponse newCoinDesk() {
        // 呼叫CoinDesk API
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = restTemplate.getForEntity(coindeskApiUrl, String.class);

        // parse json內容，並組成新API回傳
        return coinService.processBpiJson(response.getBody());
    }

    // 查詢資料
    @GetMapping(value = "/coin")
    public List<Coin> getCoins() {
        return coinRepository.findAll();
    }

    // 新增資料
    @PostMapping(value = "/coin")
    public void addCoin(@RequestBody Coin newCoin) {
        coinRepository.save(newCoin);
    }

    // 更新資料
    @PutMapping(value = "/coin/{coinId}")
    public ResponseEntity<?> updateCoin(@PathVariable String coinId, @RequestBody Coin coinRequest) {
        Coin existingCoin = coinRepository.findByCoinId(coinId);

        if (existingCoin != null) {
            // 更新相應的欄位
            existingCoin.setCoinName(coinRequest.getCoinName());
            // 呼叫 save 方法儲存修改內容，並返回修改後的內容作為回應
            Coin updatedCoin = coinRepository.save(existingCoin);
            return ResponseEntity.ok(updatedCoin);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Coin not found");
        }
    }

    // 刪除資料
    @Transactional
    @DeleteMapping(value = "/coin/{coinId}")
    public void deleteCoin(@PathVariable String coinId) {
        coinRepository.deleteByCoinId(coinId);
    }

}
