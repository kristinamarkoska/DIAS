package mk.tradesense.tradesense.repository;

import mk.tradesense.tradesense.model.MarketSentiment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MarketSentimentRepository extends JpaRepository<MarketSentiment, Integer> {
    MarketSentiment findByTickerSymbol(String tickerSymbol);
}
