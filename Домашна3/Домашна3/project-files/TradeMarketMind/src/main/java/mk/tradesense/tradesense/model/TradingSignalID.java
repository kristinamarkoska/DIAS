package mk.tradesense.tradesense.model;

import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;

@Entity
@Data
@Table(name = "trading_signals")
//@IdClass(TradingSignalId.class)
public class TradingSignalID {

    @Id
    @Column(name = "ticker_symbol")
    private String tickerSymbol;

    @Id
    @Column(name = "signal_date")
    private Date signalDate;

    private String tradingSignal;

    @Column(name = "closing_price")
    private Double closingPrice;
}
