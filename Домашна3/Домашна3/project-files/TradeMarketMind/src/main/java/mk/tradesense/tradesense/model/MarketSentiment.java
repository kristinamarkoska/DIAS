package mk.tradesense.tradesense.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Data
@Table(name = "market_sentiments")
public class MarketSentiment {

    @Id
    @Column(name = "ticker")
    private String stockTicker;

    @Column(name = "sentiment_value")
    private String sentimentValue;
}
