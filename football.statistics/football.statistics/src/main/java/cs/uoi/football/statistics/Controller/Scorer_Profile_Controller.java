package cs.uoi.football.statistics.Controller;

import cs.uoi.football.statistics.Service.Scorer_Profile_Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class Scorer_Profile_Controller {

    @Autowired
    private Scorer_Profile_Service scorerProfileService;

    @GetMapping("/scorer-profile")
    public String getScorerProfile(@RequestParam String scorer, Model model) {
        // Retrieve the scorer's statistics for years
        var scorerStats = scorerProfileService.getScorerYears(scorer);

        // Retrieve the scorer's match goals statistics
        var scorerMatchGoals = scorerProfileService.getScorerMatchGoals(scorer);

        // Fetch first and last year from scorerStats
        int firstYear = 0;
        int lastYear = 0;

        if (!scorerStats.isEmpty()) {
            firstYear = ((Number) scorerStats.get(0).get("first_year")).intValue();
            lastYear = ((Number) scorerStats.get(0).get("last_year")).intValue();
        }

        // Retrieve the scorer's team goals statistics per match
        var scorerTeamGoalsPerMatch = scorerProfileService.getScorerTeamGoals(scorer, firstYear, lastYear);

        // Retrieve the scorer's team goals statistics per year
        var scorerTeamGoalsPerYear = scorerProfileService.getScorerTeamGoalsPerYear(scorer, firstYear, lastYear);

        // Add attributes to model
        model.addAttribute("scorer", scorer);
        model.addAttribute("scorerStats", scorerStats);
        model.addAttribute("scorerMatchGoals", scorerMatchGoals);
        model.addAttribute("scorerTeamGoalsPerMatch", scorerTeamGoalsPerMatch);
        model.addAttribute("scorerTeamGoalsPerYear", scorerTeamGoalsPerYear);

        return "scorer-profile";  // Return the Thymeleaf view
    }


}
