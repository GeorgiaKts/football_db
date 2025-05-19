package cs.uoi.football.statistics.Controller;

import cs.uoi.football.statistics.Service.Year_Profile_Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.*;

@Controller
public class Year_Profile_Controller {

    @Autowired
    Year_Profile_Service yearProfileService;

    // Display statistics for year and country
    @GetMapping("/year-profile")
    public String showYearProfile(@RequestParam(value = "year", required = false) Integer selectedYear,
                                  @RequestParam(value = "country", required = false) String selectedCountry,
                                  @RequestParam(value = "continent", required = false) String continent,
                                  @RequestParam(value = "status", required = false) String status,
                                  @RequestParam(value = "developmentStatus", required = false) String developmentStatus,
                                  Model model) {

        // Get the statistics for the selected year
        if (selectedYear != null) {
            Map<String, Integer> yearStats = yearProfileService.getYearStatistics(selectedYear);
            model.addAttribute("selectedYear", selectedYear);
            model.addAttribute("totalMatches", yearStats.get("totalMatches"));
            model.addAttribute("totalDraws", yearStats.get("totalDraws"));
            model.addAttribute("totalPenalties", yearStats.get("totalPenalties"));
        }

        // Fetch the distinct countries for the dropdown
        List<String> countries = yearProfileService.getDistinctCountries();
        model.addAttribute("countries", countries);

        // Fetch the distinct continents for the dropdown
        List<String> continents = Arrays.asList("Africa", "Antarctica", "Asia", "Europe", "North America", "Oceania", "South America");
        model.addAttribute("continents", continents);

        // Fetch the distinct statuses for the dropdown
        List<String> statuses = Arrays.asList("Associated with NZ", "Ceased to exist", "Commonwealth of US", "Crown dependency of GB", "In contention",
                "Independent", "International", "Part of CN", "Part of DK", "Part of FI", "Part of FR", "Part of NL",
                "Region/Group", "Territories of US", "Territory of AU", "Territory of FR", "Territory of GB", "Territory of NO",
                "Territory of NZ", "Territory of US");
        model.addAttribute("statuses", statuses);

        // Fetch the distinct development statuses for the dropdown
        List<String> developmentStatuses = Arrays.asList("Developed", "Developing");
        model.addAttribute("developmentStatuses", developmentStatuses);

        // Fetch the filtered results if filter parameters are provided
        if (selectedYear != null && continent != null && status != null && developmentStatus != null) {
            List<Map<String, Object>> matchResults = yearProfileService.getMatchResultsByYearAndCountryDetails(
                    selectedYear, continent, status, developmentStatus);
            model.addAttribute("matchResults", matchResults);
        }

        // If a specific country is selected, fetch country-specific statistics
        if (selectedYear != null && selectedCountry != null) {
            Map<String, Integer> yearCountryStats = yearProfileService.getYearCountryStatistics(selectedYear, selectedCountry);
            model.addAttribute("yearCountryStats", yearCountryStats);
            model.addAttribute("selectedCountry", selectedCountry);

            // Pass the country stats for the bar chart
            model.addAttribute("totalMatchesForCountry", yearCountryStats.get("totalMatches"));
            model.addAttribute("totalDrawsForCountry", yearCountryStats.get("totalDraws"));
            model.addAttribute("totalWinsForCountry", yearCountryStats.get("totalWins"));
            model.addAttribute("totalLossesForCountry", yearCountryStats.get("totalLosses"));
            model.addAttribute("totalPenaltiesForCountry", yearCountryStats.get("totalPenalties"));
        }

        return "year-profile";
    }

    // Fetch filtered match results based on the selected filters
    @GetMapping("/filtered-match-results")
    @ResponseBody
    public List<Map<String, Object>> getFilteredMatchResults(
            @RequestParam(value = "year") Integer selectedYear,
            @RequestParam(value = "continent", required = false) String continent,
            @RequestParam(value = "status", required = false) String status,
            @RequestParam(value = "developmentStatus", required = false) String developmentStatus) {

        // Fetch the filtered match results
        return yearProfileService.getMatchResultsByYearAndCountryDetails(
                selectedYear, continent, status, developmentStatus);
    }

}
