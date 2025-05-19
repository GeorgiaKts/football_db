package cs.uoi.football.statistics;

import cs.uoi.football.statistics.Service.Create_And_Load.Create_Tables_Service;
import cs.uoi.football.statistics.Service.Create_And_Load.Data_Loader_Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Application implements CommandLineRunner {

	@Autowired
	private Data_Loader_Service dataLoaderService;

	@Autowired
	private Create_Tables_Service createTablesService;

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		// Create the tables
		createTablesService.createTables();

		// Load data
		dataLoaderService.loadDataAndUpdate();
	}
}
