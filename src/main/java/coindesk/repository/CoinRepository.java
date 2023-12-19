package coindesk.repository;

import coindesk.entity.Coin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CoinRepository extends JpaRepository<Coin, String> {

    Coin findByCoinId(String coinId);

    void deleteByCoinId(String coinId);
}
