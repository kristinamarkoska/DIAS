package mk.tradesense.tradesense.repository;

import mk.tradesense.tradesense.model.TradingSignalID;
import mk.tradesense.tradesense.model.TradingSignalID;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TradingSignalRepository extends CrudRepository<TradingSignalID, Integer> {
    List<TradingSignalID> findSignalsByTickerSymbol(String tickerSymbol);
}
