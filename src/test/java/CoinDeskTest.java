import coindesk.CoinDeskApplication;
import coindesk.CoinDeskController;
import coindesk.entity.Coin;
import coindesk.entity.NewAPIResponse;
import coindesk.repository.CoinRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.web.client.RestTemplate;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes= CoinDeskApplication.class)
public class CoinDeskTest {
    @Autowired
    private CoinDeskController coinDeskController;

    @MockBean
    private CoinRepository coinRepository;


    @Test
    public void testIndex() {
        assertEquals( coinDeskController.index(), "success");
    }

    @Test
    public void testGetCoins() {
        // 模擬 coinRepository 的 findAll 方法
        when(coinRepository.findAll()).thenReturn(Arrays.asList(new Coin("NTD", "新台幣")));

        List<Coin> coins = coinDeskController.getCoins();

        // 驗證返回的 coins 是否符合預期
        assertEquals(1, coins.size());
        assertEquals("NTD", coins.get(0).getCoinId());
        assertEquals("新台幣", coins.get(0).getCoinName());

        System.out.println(coins);
    }

    @Test
    public void testAddCoin() {
        Coin newCoin = new Coin("NTD", "新台幣");

        // 模擬 coinRepository 的 save 方法
        when(coinRepository.save(any(Coin.class))).thenReturn(newCoin);

        // 測試新增方法是否正確執行
        coinDeskController.addCoin(newCoin);

        // 驗證 addCoin 方法是否被呼叫
        verify(coinRepository, times(1)).save(newCoin);
    }

    @Test
    public void testUpdateCoin() {
        String coinId = "NTD";
        Coin coinRequest = new Coin("NTD", "更新的新台幣");
        Coin existingCoin = new Coin("NTD", "新台幣");

        // 模擬 coinRepository 的 findByCoinId 和 save 方法
        when(coinRepository.findByCoinId(coinId)).thenReturn(existingCoin);
        when(coinRepository.save(existingCoin)).thenReturn(existingCoin);

        ResponseEntity<?> responseEntity = coinDeskController.updateCoin(coinId, coinRequest);

        // 驗證回應是否符合預期 (status code, response body)
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(existingCoin, responseEntity.getBody());

        System.out.println(responseEntity.getBody());
    }

    @Test
    public void testDeleteCoin() {
        String coinId = "NTD";

        // 模擬 coinRepository 的 deleteByCoinId 方法
        doNothing().when(coinRepository).deleteByCoinId(coinId);

        // 測試 刪除方法 是否正確執行
        coinDeskController.deleteCoin(coinId);

        // 驗證 deleteByCoinId 方法是否被呼叫
        verify(coinRepository, times(1)).deleteByCoinId(coinId);
    }

    @Test
    public void testCoinDeskApiCall() {
        // 呼叫CoinDesk API
        String coindeskApiUrl = "https://api.coindesk.com/v1/bpi/currentprice.json";
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = restTemplate.getForEntity(coindeskApiUrl, String.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        System.out.println(response);

    }

    @Test
    public void testNewApiCall() {

        NewAPIResponse newAPIResponse = coinDeskController.newCoinDesk();

        // 測試時間格式
        assertTrue(isValidTimeFormat(newAPIResponse.getUpdateTime()));
        // 測試有無Coin內容
        assertNotNull(newAPIResponse.getNewCoins());

        System.out.println(newAPIResponse);

    }


    public static boolean isValidTimeFormat(String timeString) {
        // 定義要測試的時間格式
        SimpleDateFormat expectedDateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");

        try {
            // 測試將字串轉為 時間格式
            expectedDateFormat.parse(timeString);
            return true; // 可以則為成功
        } catch (ParseException e) {
            return false; // 不行則失敗
        }
    }

}
