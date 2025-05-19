package cs.uoi.football.statistics.Service;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public interface Scorer_Profile_Service {

    // Get scorers list
    public List<String> getAllScorers();

    // Get the years for a specific scorer from the scorer_years view
    public List<Map<String, Object>> getScorerYears(String scorer);

    // Get scorer's goals and total goals of a match
    public List<Map<String, Object>> getScorerMatchGoals(String scorer);

    // Get goals per match for scorer's team in range of year the scorer is active
    public List<Map<String, Object>> getScorerTeamGoals(String scorer, int firstYear, int lastYear);

    // Get goals per year for scorer's team in range of year the scorer is active
    public List<Map<String, Object>> getScorerTeamGoalsPerYear(String scorer, int firstYear, int lastYear);


}
