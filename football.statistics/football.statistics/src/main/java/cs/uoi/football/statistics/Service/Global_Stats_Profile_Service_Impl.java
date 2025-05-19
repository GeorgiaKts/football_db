package cs.uoi.football.statistics.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class Global_Stats_Profile_Service_Impl implements Global_Stats_Profile_Service{

    @Autowired
    private JdbcTemplate jdbcTemplate;

    // List of countries by total wins
/*    @Override
    public List<Map<String, Object>> getCountriesByWins() {
        String query = "SELECT country_name, total_wins FROM country_total_stats ORDER BY total_wins DESC";
        return jdbcTemplate.queryForList(query);
    }*/

    // List of top 10 countries of total wins
    @Override
    public List<Map<String, Object>> getTop10CountriesByWins() {
        String query = "SELECT country_name, total_wins FROM country_total_stats ORDER BY total_wins DESC LIMIT 10";
        return jdbcTemplate.queryForList(query);
    }

    // List of countries by total score
/*    @Override
    public List<Map<String, Object>> getCountriesByScore() {
        String query = "SELECT country_name, total_points FROM country_total_stats ORDER BY total_points DESC";
        return jdbcTemplate.queryForList(query);
    }*/

    // List of top 10 countries of total score
    @Override
    public List<Map<String, Object>> getTop10CountriesByScore() {
        String query = "SELECT country_name, total_points FROM country_total_stats ORDER BY total_points DESC LIMIT 10";
        return jdbcTemplate.queryForList(query);
    }

    // List countries by average wins per year
/*    @Override
    public List<Map<String, Object>> getCountriesByAvgWinsPerYear() {
        String query = "SELECT country_name, total_wins, year_count, " +
                "ROUND(total_wins / year_count, 2) AS avg_wins_per_year " +
                "FROM country_total_stats " +
                "ORDER BY avg_wins_per_year DESC ";
        return jdbcTemplate.queryForList(query);
    }*/

    // List of top 10 countries by average wins per year
    @Override
    public List<Map<String, Object>> getTop10CountriesByAvgWinsPerYear() {
        String query = "SELECT country_name, total_wins, year_count, " +
                "ROUND(total_wins / year_count, 2) AS avg_wins_per_year " +
                "FROM country_total_stats " +
                "ORDER BY avg_wins_per_year DESC " +
                "LIMIT 10";
        return jdbcTemplate.queryForList(query);
    }

    // List countries by average score per year
/*    @Override
    public List<Map<String, Object>> getCountriesByAvgScorePerYear() {
        String query = "SELECT country_name, total_points, year_count, " +
                "ROUND(total_points / year_count, 2) AS avg_score_per_year " +
                "FROM country_total_stats " +
                "ORDER BY avg_score_per_year DESC ";
        return jdbcTemplate.queryForList(query);
    }*/

    // List of top 10 countries by average score per year
    @Override
    public List<Map<String, Object>> getTop10CountriesByAvgScorePerYear() {
        String query = "SELECT country_name, total_points, year_count, " +
                "ROUND(total_points / year_count, 2) AS avg_score_per_year " +
                "FROM country_total_stats " +
                "ORDER BY avg_score_per_year DESC " +
                "LIMIT 10";
        return jdbcTemplate.queryForList(query);
    }

    // Scatterplot Score X Population
    @Override
    public List<Map<String, Object>> getCountryScoreVsPopulation() {
        String query = """
        SELECT 
            s.country_name,
            s.total_points,
            c.population
        FROM 
            country_total_stats s
        JOIN 
            countries c ON s.country_name = c.display_name
        WHERE 
            s.total_points IS NOT NULL AND c.population IS NOT NULL
    """;
        return jdbcTemplate.queryForList(query);
    }
}
