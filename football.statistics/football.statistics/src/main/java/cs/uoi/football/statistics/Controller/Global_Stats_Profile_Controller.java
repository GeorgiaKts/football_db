package cs.uoi.football.statistics.Controller;

import cs.uoi.football.statistics.Service.Global_Stats_Profile_Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;
import java.util.Map;

@Controller
public class Global_Stats_Profile_Controller {

    @Autowired
    Global_Stats_Profile_Service globalStatsProfileService;

    @GetMapping("/global-stats-profile")
    public String showGlobalStats(Model model) {

        // Fetch data for scatterplot
        List<Map<String, Object>> scoreVsPopulation = globalStatsProfileService.getCountryScoreVsPopulation();
        model.addAttribute("scoreVsPopulation", scoreVsPopulation);


        // Fetch top 10 countries by average wins and score by year
        List<Map<String, Object>> top10CountriesWinByYear = globalStatsProfileService.getTop10CountriesByAvgWinsPerYear();
        List<Map<String, Object>> top10CountriesScoreByYear = globalStatsProfileService.getTop10CountriesByAvgScorePerYear();

        model.addAttribute("top10CountriesWinByYear", top10CountriesWinByYear);
        model.addAttribute("top10CountriesScoreByYear", top10CountriesScoreByYear);


        // Fetch countries by average wins and score by year
/*        List<Map<String, Object>> countriesWinByYear = globalStatsProfileService.getCountriesByAvgWinsPerYear();
        List<Map<String, Object>> countriesScoreByYear = globalStatsProfileService.getCountriesByAvgScorePerYear();

        model.addAttribute("countriesWinByYear", countriesWinByYear);
        model.addAttribute("countriesScoreByYear", countriesScoreByYear);

        //Fetch countries by wins and score
        List<Map<String, Object>> rankCountriesWin = globalStatsProfileService.getCountriesByWins();
        List<Map<String, Object>> rankCountriesScore = globalStatsProfileService.getCountriesByScore();

        model.addAttribute("rankCountriesWin", rankCountriesWin);
        model.addAttribute("rankCountriesScore", rankCountriesScore);*/

        // Fetch top 10 countries by wins and score
        List<Map<String, Object>> topCountriesWin = globalStatsProfileService.getTop10CountriesByWins();
        List<Map<String, Object>> topCountriesScore = globalStatsProfileService.getTop10CountriesByScore();

        model.addAttribute("topCountriesWin", topCountriesWin);
        model.addAttribute("topCountriesScore", topCountriesScore);

        return "global-stats-profile";
    }
}
