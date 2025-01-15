package mk.tradesense.tradesense.controller;

import mk.tradesense.tradesense.model.Sentiment;
import mk.tradesense.tradesense.model.Signal;
import mk.tradesense.tradesense.repository.SentimentRepository;
import mk.tradesense.tradesense.repository.SignalRepository;
import org.springframework.web.bind.annotation.*;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.List;

@RestController
@RequestMapping("/api/predictions")
public class AnalysisController {

    private final SignalRepository signalRepo;
    private final SentimentRepository sentimentRepo;

    public AnalysisController(SignalRepository signalRepo, SentimentRepository sentimentRepo) {
        this.signalRepo = signalRepo;
        this.sentimentRepo = sentimentRepo;
    }

    @PostMapping("/technical-analysis")
    public String executeTechnicalAnalysis(@RequestParam("stockCode") String stockCode) {
        String pythonExecutable = System.getenv("PYTHON_PATH");
        if (pythonExecutable == null || pythonExecutable.isEmpty()) {
            throw new IllegalStateException("Environment variable 'PYTHON_PATH' is not defined or empty.");
        }

        try {
            ProcessBuilder processBuilder = new ProcessBuilder(
                    pythonExecutable,
                    "src/main/java/mk/tradesense/tradesense/scripts/technical.py",
                    stockCode
            );
            Process process = processBuilder.start();

            StringBuilder output = new StringBuilder();
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    output.append(line).append("\n");
                }
            }

            process.waitFor();
            return "Technical analysis completed successfully:\n" + output;

        } catch (Exception ex) {
            ex.printStackTrace();
            return "An error occurred while executing the technical analysis: " + ex.getMessage();
        }
    }

    @GetMapping("/signals")
    public List<Signal> retrieveSignals(@RequestParam("stockCode") String stockCode) {
        return signalRepo.findSignalsByStockCode(stockCode);
    }

    @GetMapping("/sentiments/{stockCode}")
    public Sentiment fetchSentiment(@PathVariable("stockCode") String stockCode) {
        return sentimentRepo.findByStockCode(stockCode);
    }
}
