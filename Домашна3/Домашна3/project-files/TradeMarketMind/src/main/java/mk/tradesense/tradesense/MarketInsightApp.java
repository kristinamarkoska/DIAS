package mk.tradesense.tradesense;

import lombok.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class MarketInsightApp {
	private String appName;

	public String getAppName() {
		return appName;
	}

	public void setAppName(String appName) {
		this.appName = appName;
	}

	public static void main(String[] args) {
		MarketInsightApp app = new MarketInsightApp();
		app.setAppName("NewAppName");
		SpringApplication.run(MarketInsightApp.class, args);
	}

	@Value("${app.name}")
	private String appName;

	public void setAppName(String appName) {
		this.appName = appName;
	}


}
