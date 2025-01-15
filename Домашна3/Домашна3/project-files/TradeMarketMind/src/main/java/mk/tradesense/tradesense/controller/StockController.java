package mk.tradesense.tradesense.controller;

import mk.tradesense.tradesense.model.StockItem;
import mk.tradesense.tradesense.repository.StockItemRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/stocks")
public class StockController {

    private final StockItemRepository repository;

    public StockController(StockItemRepository repository) {
        this.repository = repository;
    }

    @PostMapping("/add")
    public ResponseEntity<StockItem> addStockItem(@RequestBody StockItem stockItem) {
        StockItem newStockItem = repository.save(stockItem);
        return new ResponseEntity<>(newStockItem, HttpStatus.CREATED);
    }

    @GetMapping("/all")
    public ResponseEntity<List<StockItem>> fetchAllStockItems() {
        List<StockItem> stockList = repository.findAll();
        return ResponseEntity.ok(stockList);
    }

    @GetMapping("/{stockId}")
    public ResponseEntity<StockItem> fetchStockById(@PathVariable("stockId") Long stockId) {
        return repository.findById(stockId)
                .map(ResponseEntity::ok)
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @DeleteMapping("/remove/{stockId}")
    public ResponseEntity<Void> removeStockItem(@PathVariable("stockId") Long stockId) {
        if (repository.existsById(stockId)) {
            repository.deleteById(stockId);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping("/unique-codes")
    public ResponseEntity<List<String>> fetchDistinctStockCodes() {
        List<String> codes = repository.findDistinctStockCodes();
        return ResponseEntity.ok(codes);
    }
}
