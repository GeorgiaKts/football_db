package cs.uoi.football.statistics.Service;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public interface Year_Profile_Service {

    // Get distinct years from the year_count view
    public List<Integer> getDistinctYears();

    // Get total matches, draws, penalties for year
    public Map<String, Integer> getYearStatistics(int year);

    // Get distinct countries from the year_count view
    public List<String> getDistinctCountries();

    // Get year and country specific statistics (Barchart)
    public Map<String, Integer> getYearCountryStatistics(int year, String country);

    // Get results based on year, continent, status, and development_status
    public List<Map<String, Object>> getMatchResultsByYearAndCountryDetails(int year, String continent, String status, String developmentStatus);

}
