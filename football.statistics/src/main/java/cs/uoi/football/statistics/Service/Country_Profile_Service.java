package cs.uoi.football.statistics.Service;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public interface Country_Profile_Service {

    // Get display names from countries
    public List<String> getCountryNames();

    // Get team details for participation a selected country
    public List<Map<String, Object>> getTeamsByCountry(String countryName);

    // Get total wins, losses, draws, matches for every team
    public Map<String, Integer> getTeamStatistics(String teamName);

    // Get total wins, losses, draws, matches for every country
    public Map<String, Integer> getTotalCountryStatistics(String countryName);

    // Get team statistics by role (Home/Away)
    public Map<String, Integer> getTeamStatisticsByRole(String teamName, String teamRole);

    // Get total country statistics by role (Home/Away)
    public Map<String, Integer> getTotalCountryStatisticsByRole(String countryName, String teamRole);

    // Get total wins, losses, draws, matches per year for country in total (Linechart)
    public Map<Integer, Map<String, Integer>> getStatisticsPerYear(String countryName);

    // Get total wins, losses, draws, matches per year for country in total as home or away (Linechart)
    public Map<Integer, Map<String, Integer>> getStatisticsPerYearByRole(String countryName, String teamRole);

    // Get details based on year range
    public List<Map<String, Object>> getMatchDetailsForCountry(String countryName, int startYear, int endYear);
}
