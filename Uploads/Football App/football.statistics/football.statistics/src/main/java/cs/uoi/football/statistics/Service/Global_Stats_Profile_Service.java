package cs.uoi.football.statistics.Service;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public interface Global_Stats_Profile_Service {

    // List of countries of total wins
//    public List<Map<String, Object>> getCountriesByWins();

    // List of top 10 countries of total wins
    public List<Map<String, Object>> getTop10CountriesByWins();

    // List of countries by total score
//    public List<Map<String, Object>> getCountriesByScore();

    // List of top 10 countries of total score
    public List<Map<String, Object>> getTop10CountriesByScore();

    // List countries by average wins per year
//    public List<Map<String, Object>> getCountriesByAvgWinsPerYear();

    // List of top 10 countries by average wins per year
    public List<Map<String, Object>> getTop10CountriesByAvgWinsPerYear();

    // List countries by average score per year
//    public List<Map<String, Object>> getCountriesByAvgScorePerYear();

    // List of top 10 countries by average score per year
    public List<Map<String, Object>> getTop10CountriesByAvgScorePerYear();

    // Scatterplot Score X Population
    public List<Map<String, Object>> getCountryScoreVsPopulation();
}
