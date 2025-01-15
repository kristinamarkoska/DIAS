package mk.tradesense.tradesense.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "equity_data")
public class EquityData {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long entityId;

    @Column(name = "ticker_symbol", nullable = false)
    private String tickerSymbol;

    @Column(nullable = false)
    private String recordDate;

    private String closingPrice;
    private String highestPrice;
    private String lowestPrice;
    private String averagePrice;
    private String priceChangePercentage;
    private String tradedQuantity;
    private String highestTurnover;
    private String totalTurnover;
}
