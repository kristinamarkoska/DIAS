package mk.tradesense.tradesense.repository;

import mk.tradesense.tradesense.model.EquityData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EquityDataRepository extends JpaRepository<EquityData, Long> {

    @Query("SELECT DISTINCT e.tickerSymbol FROM EquityData e")
    List<String> findDistinctTickerSymbols();
}
