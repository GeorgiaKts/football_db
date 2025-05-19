package cs.uoi.football.statistics.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class Year_Profile_Service_Impl implements Year_Profile_Service {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    // Get distinct years from the year_count view
    @Override
    public List<Integer> getDistinctYears() {
        String sql = "SELECT year FROM year_count GROUP BY year ORDER BY year";
        return jdbcTemplate.queryForList(sql, Integer.class);
    }

    // Get total matches, draws, penalties for year
    @Override
    public Map<String, Integer> getYearStatistics(int year) {
        Map<String, Integer> stats = new HashMap<>();

        // Get total matches and total draws count in one query
        String combinedQuery = "SELECT SUM(match_count), SUM(draw_count) FROM year_count WHERE year = ?";
        Map<String, Object> result = jdbcTemplate.queryForMap(combinedQuery, year);

        // Convert BigDecimal to Integer, handling null or zero values
        BigDecimal matchCountBigDecimal = (BigDecimal) result.get("SUM(match_count)");
        BigDecimal drawCountBigDecimal = (BigDecimal) result.get("SUM(draw_count)");

        stats.put("totalMatches", matchCountBigDecimal != null ? matchCountBigDecimal.intValue() : 0);
        stats.put("totalDraws", drawCountBigDecimal != null ? drawCountBigDecimal.intValue() : 0);

        // Get total penalties count
        String penaltyCountQuery = "SELECT SUM(penalty_count) FROM yearly_penalties WHERE year = ?";
        BigDecimal penaltyCountBigDecimal = jdbcTemplate.queryForObject(penaltyCountQuery, BigDecimal.class, year);
        stats.put("totalPenalties", penaltyCountBigDecimal != null ? penaltyCountBigDecimal.intValue() : 0);

        return stats;
    }

    // Get display names from countries
    @Override
    public List<String> getDistinctCountries() {
        String sql = "SELECT display_name FROM countries ORDER BY display_name";
        return jdbcTemplate.queryForList(sql, String.class);
    }

    // Get year and country specific statistics (Barchart)
    @Override
    public Map<String, Integer> getYearCountryStatistics(int year, String country) {
        Map<String, Integer> stats = new HashMap<>();

        // Query to get match, draw, win, loss counts
        String matchQuery = "SELECT match_count, draw_count, win_count, loss_count FROM year_count WHERE year = ? AND country_name = ?";

        List<Map<String, Object>> matchResults = jdbcTemplate.queryForList(matchQuery, year, country);

        // If no result is found, initialize values to 0
        if (matchResults.isEmpty()) {
            stats.put("totalMatches", 0);
            stats.put("totalDraws", 0);
            stats.put("totalWins", 0);
            stats.put("totalLosses", 0);
        } else {
            // If result is found, extract the values
            Map<String, Object> matchResult = matchResults.get(0);
            stats.put("totalMatches", convertToInteger(matchResult.get("match_count")));
            stats.put("totalDraws", convertToInteger(matchResult.get("draw_count")));
            stats.put("totalWins", convertToInteger(matchResult.get("win_count")));
            stats.put("totalLosses", convertToInteger(matchResult.get("loss_count")));
        }

        // Query to get penalty count
        String penaltyQuery = "SELECT penalty_count FROM yearly_penalties WHERE year = ? AND country_name = ?";
        List<Integer> penaltyCountList = jdbcTemplate.queryForList(penaltyQuery, Integer.class, year, country);

        // If no result is found, set penaltyCount to 0
        Integer penaltyCount = penaltyCountList.isEmpty() ? 0 : penaltyCountList.get(0);
        stats.put("totalPenalties", penaltyCount);

        return stats;
    }

    // Helper method to convert Object to Integer
    private Integer convertToInteger(Object value) {
        if (value instanceof BigDecimal) {
            return ((BigDecimal) value).intValue();  // Convert BigDecimal to Integer
        }
        if (value instanceof Integer) {
            return (Integer) value;
        }
        return 0;  // Return 0 if value is null or not a valid number
    }

    // Get results based on year, continent, status, and development_status
    @Override
    public List<Map<String, Object>> getMatchResultsByYearAndCountryDetails(int year, String continent, String status, String developmentStatus) {
        String sql = "SELECT r.date, "
                + "       r.home_team, "
                + "       r.away_team, "
                + "       r.home_score, "
                + "       r.away_score, "
                + "       r.tournament, "
                + "       r.city, "
                + "       r.country, "
                + "       CASE "
                + "           WHEN r.neutral = TRUE THEN 'Yes' "
                + "           WHEN r.neutral = FALSE THEN 'No' "
                + "       END AS neutral, "
                + "       CASE "
                + "           WHEN r.winner IS NULL AND r.home_score = r.away_score THEN 'Draw' "
                + "           ELSE r.winner "
                + "       END AS winner "
                + "FROM results r "
                + "JOIN team_results tr ON r.id_results = tr.id_results "
                + "JOIN countries c ON tr.id_countries = c.id_countries "
                + "WHERE YEAR(r.date) = ? "
                + "  AND c.continent = ? "
                + "  AND c.status = ? "
                + "  AND c.development_status = ?";

        return jdbcTemplate.queryForList(sql, year, continent, status, developmentStatus);
    }
}
