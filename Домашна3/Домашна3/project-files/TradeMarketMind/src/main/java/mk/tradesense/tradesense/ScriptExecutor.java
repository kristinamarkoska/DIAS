package mk.tradesense.tradesense;

import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.InputStreamReader;

@Component
public class ScriptExecutor {

    @PostConstruct
    public void loadData() {
        try {
            String pythonExecutablePath = System.getenv("PYTHON_EXEC_PATH");
            if (pythonExecutablePath == null) {
                throw new IllegalStateException("PYTHON_EXEC_PATH environment variable is not defined");
            }

            ProcessBuilder builder = new ProcessBuilder(pythonExecutablePath, "src/main/java/mk/tradesense/tradesense/scripts/data_collector_v6.py");
            builder.redirectErrorStream(true);
            Process process = builder.start();

            try (BufferedReader inputReader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
                String outputLine;
                while ((outputLine = inputReader.readLine()) != null) {
                    System.out.println(outputLine);
                }
            }

            int resultCode = process.waitFor();
            System.out.println("Python script completed with status: " + resultCode);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
