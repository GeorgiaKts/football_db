package cs.uoi.football.statistics.Controller;

import cs.uoi.football.statistics.Service.Country_Profile_Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Controller
public class Country_Profile_Controller {

    @Autowired
    private Country_Profile_Service countriesProfileService;


    // Display total statistics for country
    @GetMapping("/countries-profile")
    public String showCountryProfile(@RequestParam String countryName, Model model) {
        // Get team details for the selected country
        List<Map<String, Object>> teams = countriesProfileService.getTeamsByCountry(countryName);

        // Get total statistics for the country
        Map<String, Integer> totalStats = countriesProfileService.getTotalCountryStatistics(countryName);
        Map<String, Integer> totalHomeStats = countriesProfileService.getTotalCountryStatisticsByRole(countryName, "Home");
        Map<String, Integer> totalAwayStats = countriesProfileService.getTotalCountryStatisticsByRole(countryName, "Away");

        // Add the data to the model
        model.addAttribute("countryName", countryName);
        model.addAttribute("teams", teams);
        model.addAttribute("totalStats", totalStats);
        model.addAttribute("totalHomeStats", totalHomeStats);
        model.addAttribute("totalAwayStats", totalAwayStats);

        return "countries-profile";
    }

    // Display more details if country has more than 1 team
    @GetMapping("/load-more-stats")
    @ResponseBody
    public List<Map<String, Object>> loadExtraStats(@RequestParam String countryName) {
        List<Map<String, Object>> teams = countriesProfileService.getTeamsByCountry(countryName);

        for (Map<String, Object> team : teams) {
            String teamName = (String) team.get("team");

            // Overall stats
            Map<String, Integer> teamStats = countriesProfileService.getTeamStatistics(teamName);
            team.putAll(teamStats);

            // Home stats
            Map<String, Integer> homeStats = countriesProfileService.getTeamStatisticsByRole(teamName, "Home");
            team.put("home_wins", homeStats.get("wins"));
            team.put("home_losses", homeStats.get("losses"));
            team.put("home_draws", homeStats.get("draws"));
            team.put("home_matches", homeStats.get("total_matches"));

            // Away stats
            Map<String, Integer> awayStats = countriesProfileService.getTeamStatisticsByRole(teamName, "Away");
            team.put("away_wins", awayStats.get("wins"));
            team.put("away_losses", awayStats.get("losses"));
            team.put("away_draws", awayStats.get("draws"));
            team.put("away_matches", awayStats.get("total_matches"));
        }

        return teams;
    }

    // Display linecharts for total wins, losses, draws, matches per year
    @GetMapping("/stats-per-year")
    @ResponseBody
    public Map<Integer, Map<String, Integer>> getStatisticsPerYear(@RequestParam String countryName) {
        return countriesProfileService.getStatisticsPerYear(countryName);
    }

    // Display linecharts for total wins, losses, draws, matches per year as home
    @GetMapping("/stats-per-year/home")
    @ResponseBody
    public Map<Integer, Map<String, Integer>> getHomeStatisticsPerYear(@RequestParam String countryName) {
        return countriesProfileService.getStatisticsPerYearByRole(countryName, "Home");
    }

    // Display linecharts for total wins, losses, draws, matches per year as away
    @GetMapping("/stats-per-year/away")
    @ResponseBody
    public Map<Integer, Map<String, Integer>> getAwayStatisticsPerYear(@RequestParam String countryName) {
        return countriesProfileService.getStatisticsPerYearByRole(countryName, "Away");
    }

    // Fetch and display match details for the country within the specified year range
    @GetMapping("/match-details")
    @ResponseBody
    public List<Map<String, Object>> getMatchDetails(
            @RequestParam String countryName,
            @RequestParam int startYear,
            @RequestParam int endYear) {

        List<Map<String, Object>> teams = countriesProfileService.getTeamsByCountry(countryName);

        List<Map<String, Object>> allMatchDetails = new ArrayList<>();

        for (Map<String, Object> team : teams) {
            String teamName = (String) team.get("team");

            List<Map<String, Object>> teamMatchDetails = countriesProfileService.getMatchDetailsForCountry(teamName, startYear, endYear);

            allMatchDetails.addAll(teamMatchDetails);
        }

        return allMatchDetails;
    }

}
