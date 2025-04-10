package cs.uoi.football.statistics.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class Scorer_Profile_Service_Impl implements Scorer_Profile_Service{

    @Autowired
    private JdbcTemplate jdbcTemplate;

    // Get scorers list
    @Override
    public List<String> getAllScorers() {
        String query = "SELECT scorer FROM scorer_years ORDER BY scorer";
        return jdbcTemplate.queryForList(query, String.class);
    }

    // Get the years for a specific scorer from the scorer_years view
    @Override
    public List<Map<String, Object>> getScorerYears(String scorer) {
        String query = "SELECT scorer, first_year, last_year FROM scorer_years WHERE scorer = ?";
        return jdbcTemplate.queryForList(query, scorer);
    }

    // Get scorer's goals and total goals of a match
    @Override
    public List<Map<String, Object>> getScorerMatchGoals(String scorer) {
        String query = "SELECT sm.date, sm.home_team, sm.away_team, sm.player_goals, gm.total_goals " +
                "FROM scorer_match sm " +
                "JOIN goals_match gm " +
                "ON sm.date = gm.date " +
                "AND sm.home_team = gm.home_team " +
                "AND sm.away_team = gm.away_team " +
                "WHERE sm.scorer = ?";

        return jdbcTemplate.queryForList(query, scorer);
    }

    // Get goals per match for scorer's team in range of year the scorer is active
    @Override
    public List<Map<String, Object>> getScorerTeamGoals(String scorer, int firstYear, int lastYear) {
        String query = "SELECT sm.date, sm.home_team, sm.away_team, sm.team, SUM(sm.player_goals) AS total_team_goals " +
                "FROM scorer_match sm " +
                "JOIN scorer_years sy " +
                "ON sm.scorer = sy.scorer " +
                "WHERE sm.team = (SELECT sm2.team FROM scorer_match sm2 WHERE sm2.scorer = ? LIMIT 1) " +
                "AND YEAR(sm.date) BETWEEN ? AND ? " +
                "GROUP BY sm.date, sm.home_team, sm.away_team, sm.team " +
                "ORDER BY sm.date";

        return jdbcTemplate.queryForList(query, scorer, firstYear, lastYear);
    }

    // Get goals per year for scorer's team in range of year the scorer is active
    @Override
    public List<Map<String, Object>> getScorerTeamGoalsPerYear(String scorer, int firstYear, int lastYear) {
        String query = "SELECT YEAR(sm.date) AS year, sm.team, SUM(sm.player_goals) AS total_team_goals " +
                "FROM scorer_match sm " +
                "JOIN scorer_years sy " +
                "ON sm.scorer = sy.scorer " +
                "WHERE sm.team = (SELECT sm2.team FROM scorer_match sm2 WHERE sm2.scorer = ? LIMIT 1) " +
                "AND YEAR(sm.date) BETWEEN ? AND ? " +
                "GROUP BY YEAR(sm.date), sm.team " +
                "ORDER BY YEAR(sm.date)";

        return jdbcTemplate.queryForList(query, scorer, firstYear, lastYear);
    }
}
