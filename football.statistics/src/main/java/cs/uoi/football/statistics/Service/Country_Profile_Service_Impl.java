package cs.uoi.football.statistics.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
public class Country_Profile_Service_Impl implements Country_Profile_Service {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    // Get display names from countries
    @Override
    public List<String> getCountryNames() {
        String sql = "SELECT display_name FROM countries ORDER BY display_name";

        return jdbcTemplate.queryForList(sql, String.class);
    }

    // Get team details for participation a selected country
    @Override
    public List<Map<String, Object>> getTeamsByCountry(String countryName) {
        String sql = "SELECT team, first_year, last_year, years_participated FROM team_participation WHERE country_name = ?";
        return jdbcTemplate.queryForList(sql, countryName);
    }

    // Get total wins, losses, draws, matches for every team
    @Override
    public Map<String, Integer> getTeamStatistics(String teamName) {
        String sql = """
            SELECT 
                SUM(CASE WHEN result = 'Winner' THEN 1 ELSE 0 END) AS wins,
                SUM(CASE WHEN result = 'Loser' THEN 1 ELSE 0 END) AS losses,
                SUM(CASE WHEN result = 'Draw' THEN 1 ELSE 0 END) AS draws,
                COUNT(*) AS total_matches
            FROM team_results
            WHERE team = ?
            GROUP BY team;
        """;

        // Get the result as a Map<String, Object>
        Map<String, Object> resultMap = jdbcTemplate.queryForMap(sql, teamName);

        // Manually convert BigDecimal to Integer
        Map<String, Integer> stats = new HashMap<>();
        stats.put("wins", convertToInteger(resultMap.get("wins")));
        stats.put("losses", convertToInteger(resultMap.get("losses")));
        stats.put("draws", convertToInteger(resultMap.get("draws")));
        stats.put("total_matches", convertToInteger(resultMap.get("total_matches")));

        return stats;
    }

    // Get total wins, losses, draws, matches for every country
    @Override
    public Map<String, Integer> getTotalCountryStatistics(String countryName) {
        // Get all teams for the country
        List<Map<String, Object>> teams = getTeamsByCountry(countryName);

        // Initialize totals
        int totalWins = 0;
        int totalLosses = 0;
        int totalDraws = 0;
        int totalMatches = 0;

        // Iterate through each team and sum up the statistics
        for (Map<String, Object> team : teams) {
            String teamName = (String) team.get("team");
            Map<String, Integer> teamStats = getTeamStatistics(teamName);
            totalWins += teamStats.get("wins");
            totalLosses += teamStats.get("losses");
            totalDraws += teamStats.get("draws");
            totalMatches += teamStats.get("total_matches");
        }

        // Create a map to return the totals
        Map<String, Integer> totalStats = new HashMap<>();
        totalStats.put("total_wins", totalWins);
        totalStats.put("total_losses", totalLosses);
        totalStats.put("total_draws", totalDraws);
        totalStats.put("total_matches", totalMatches);

        return totalStats;
    }

    // Get team statistics by role (Home/Away)
    @Override
    public Map<String, Integer> getTeamStatisticsByRole(String teamName, String teamRole) {
        String sql = """
        SELECT 
            SUM(CASE WHEN result = 'Winner' THEN 1 ELSE 0 END) AS wins,
            SUM(CASE WHEN result = 'Loser' THEN 1 ELSE 0 END) AS losses,
            SUM(CASE WHEN result = 'Draw' THEN 1 ELSE 0 END) AS draws,
            COUNT(*) AS total_matches
        FROM team_results
        WHERE team = ? AND team_role = ?
        GROUP BY team;
    """;

        List<Map<String, Object>> results = jdbcTemplate.queryForList(sql, teamName, teamRole);

        // If no results, return default values
        if (results.isEmpty()) {
            return Map.of(
                    "wins", 0,
                    "losses", 0,
                    "draws", 0,
                    "total_matches", 0
            );
        }

        Map<String, Object> resultMap = results.get(0);
        return Map.of(
                "wins", convertToInteger(resultMap.get("wins")),
                "losses", convertToInteger(resultMap.get("losses")),
                "draws", convertToInteger(resultMap.get("draws")),
                "total_matches", convertToInteger(resultMap.get("total_matches"))
        );
    }


    // Get total country statistics by role (Home/Away)
    @Override
    public Map<String, Integer> getTotalCountryStatisticsByRole(String countryName, String teamRole) {
        List<Map<String, Object>> teams = getTeamsByCountry(countryName);

        int totalWins = 0;
        int totalLosses = 0;
        int totalDraws = 0;
        int totalMatches = 0;

        for (Map<String, Object> team : teams) {
            String teamName = (String) team.get("team");
            Map<String, Integer> teamStats = getTeamStatisticsByRole(teamName, teamRole);
            totalWins += teamStats.get("wins");
            totalLosses += teamStats.get("losses");
            totalDraws += teamStats.get("draws");
            totalMatches += teamStats.get("total_matches");
        }

        Map<String, Integer> totalStats = new HashMap<>();
        totalStats.put("total_wins", totalWins);
        totalStats.put("total_losses", totalLosses);
        totalStats.put("total_draws", totalDraws);
        totalStats.put("total_matches", totalMatches);

        return totalStats;
    }

    // Helper method to convert BigDecimal or Long to Integer
    private Integer convertToInteger(Object value) {
        if (value instanceof BigDecimal) {
            return ((BigDecimal) value).intValue();  // Convert BigDecimal to Integer
        } else if (value instanceof Long) {
            return ((Long) value).intValue();  // Convert Long to Integer
        } else if (value instanceof Integer) {
            return (Integer) value;  // Already Integer
        }
        return 0;  // Default case for unknown types
    }

    // Get total wins, losses, draws, matches per year for country in total (Linechart)
    @Override
    public Map<Integer, Map<String, Integer>> getStatisticsPerYear(String countryName) {

        String sql = """
    SELECT 
        match_year,
        SUM(CASE WHEN result = 'Winner' THEN 1 ELSE 0 END) AS total_wins,
        SUM(CASE WHEN result = 'Loser' THEN 1 ELSE 0 END) AS total_losses,
        SUM(CASE WHEN result = 'Draw' THEN 1 ELSE 0 END) AS total_draws,
        COUNT(*) AS total_matches
    FROM team_results
    WHERE team IN (
        SELECT team FROM team_participation WHERE country_name = ?
    )
    GROUP BY match_year
    ORDER BY match_year;
    """;

        // Fetch the results from the database
        List<Map<String, Object>> results = jdbcTemplate.queryForList(sql, countryName);

        // Map to hold the results organized by year
        Map<Integer, Map<String, Integer>> statisticsPerYear = new LinkedHashMap<>();

        // Iterate over the results and structure them
        for (Map<String, Object> row : results) {
            Integer year = ((Number) row.get("match_year")).intValue();
            Map<String, Integer> stats = new HashMap<>();
            stats.put("wins", ((Number) row.get("total_wins")).intValue());
            stats.put("losses", ((Number) row.get("total_losses")).intValue());
            stats.put("draws", ((Number) row.get("total_draws")).intValue());
            stats.put("total_matches", ((Number) row.get("total_matches")).intValue());

            statisticsPerYear.put(year, stats);
        }

        return statisticsPerYear;
    }

    // Get total wins, losses, draws, matches per year for country in total as home or away (Linechart)
    @Override
    public Map<Integer, Map<String, Integer>> getStatisticsPerYearByRole(String countryName, String role) {
        String sql = """
        SELECT 
            match_year,
            SUM(CASE WHEN result = 'Winner' THEN 1 ELSE 0 END) AS total_wins,
            SUM(CASE WHEN result = 'Loser' THEN 1 ELSE 0 END) AS total_losses,
            SUM(CASE WHEN result = 'Draw' THEN 1 ELSE 0 END) AS total_draws,
            COUNT(*) AS total_matches
        FROM team_results
        WHERE team IN (
            SELECT team FROM team_participation WHERE country_name = ? AND team_role = ?
        )
        GROUP BY match_year
        ORDER BY match_year;
    """;

        List<Map<String, Object>> results = jdbcTemplate.queryForList(sql, countryName, role);
        Map<Integer, Map<String, Integer>> statisticsPerYear = new LinkedHashMap<>();

        for (Map<String, Object> row : results) {
            Integer year = ((Number) row.get("match_year")).intValue();
            Map<String, Integer> stats = new HashMap<>();
            stats.put("wins", ((Number) row.get("total_wins")).intValue());
            stats.put("losses", ((Number) row.get("total_losses")).intValue());
            stats.put("draws", ((Number) row.get("total_draws")).intValue());
            stats.put("total_matches", ((Number) row.get("total_matches")).intValue());

            statisticsPerYear.put(year, stats);
        }

        return statisticsPerYear;
    }

    // Get details based on year range
    @Override
    public List<Map<String, Object>> getMatchDetailsForCountry(String countryName, int startYear, int endYear) {
        String sql = "SELECT r.date, r.home_team, r.away_team, r.home_score, r.away_score, r.tournament, r.city, r.country, " +
                "CASE WHEN r.neutral = 'TRUE' THEN 'Yes' ELSE 'No' END AS neutral, " +
                "COALESCE(r.winner, 'Draw') AS winner " +
                "FROM results r " +
                "JOIN team_results tr ON r.id_results = tr.id_results " +
                "WHERE tr.team = ? AND YEAR(r.date) BETWEEN ? AND ?";

        return jdbcTemplate.queryForList(sql, countryName, startYear, endYear);
    }
}

