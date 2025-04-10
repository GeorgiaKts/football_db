package cs.uoi.football.statistics.Controller;

import cs.uoi.football.statistics.Service.Country_Profile_Service;
import cs.uoi.football.statistics.Service.Year_Profile_Service;
import cs.uoi.football.statistics.Service.Scorer_Profile_Service; // Add this import
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class Homepage_Controller {

    @Autowired
    private Country_Profile_Service countriesProfileService;

    @Autowired
    private Year_Profile_Service yearProfileService;

    @Autowired
    private Scorer_Profile_Service scorerProfileService;  // Autowire the scorer service

    @GetMapping("/")
    public String showHomePage(Model model) {
        // Get the list of country names, years, and scorers from the services
        model.addAttribute("countryNames", countriesProfileService.getCountryNames());
        model.addAttribute("years", yearProfileService.getDistinctYears());
        model.addAttribute("scorers", scorerProfileService.getAllScorers());  // Add scorers to the model
        return "homepage";  // Returns the homepage.html
    }
}
