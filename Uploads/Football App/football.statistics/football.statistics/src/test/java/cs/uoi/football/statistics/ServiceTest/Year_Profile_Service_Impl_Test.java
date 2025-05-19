package cs.uoi.football.statistics.ServiceTest;

import cs.uoi.football.statistics.Service.Year_Profile_Service_Impl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import org.springframework.jdbc.core.JdbcTemplate;

import java.math.BigDecimal;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

public class Year_Profile_Service_Impl_Test {

    @Mock
    private JdbcTemplate jdbcTemplate;

    @InjectMocks
    private Year_Profile_Service_Impl service;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetDistinctYears() {
        List<Integer> mockYears = List.of(2018, 2019, 2020);

        when(jdbcTemplate.queryForList(anyString(), eq(Integer.class))).thenReturn(mockYears);

        List<Integer> result = service.getDistinctYears();

        assertEquals(3, result.size());
        assertEquals(2018, result.get(0));
    }

    @Test
    public void testGetYearStatistics() {
        int year = 2020;
        Map<String, Object> queryForMapResult = new HashMap<>();
        queryForMapResult.put("SUM(match_count)", new BigDecimal(50));
        queryForMapResult.put("SUM(draw_count)", new BigDecimal(10));

        when(jdbcTemplate.queryForMap(anyString(), eq(year))).thenReturn(queryForMapResult);
        when(jdbcTemplate.queryForObject(anyString(), eq(BigDecimal.class), eq(year))).thenReturn(new BigDecimal(5));

        Map<String, Integer> stats = service.getYearStatistics(year);

        assertEquals(50, stats.get("totalMatches"));
        assertEquals(10, stats.get("totalDraws"));
        assertEquals(5, stats.get("totalPenalties"));
    }

    @Test
    public void testGetDistinctCountries() {
        List<String> countries = List.of("Country A", "Country B", "Country C");

        when(jdbcTemplate.queryForList(anyString(), eq(String.class))).thenReturn(countries);

        List<String> result = service.getDistinctCountries();

        assertEquals(3, result.size());
        assertEquals("Country A", result.get(0));
    }

    @Test
    public void testGetYearCountryStatistics_WithData() {
        int year = 2020;
        String country = "Country A";

        Map<String, Object> row = new HashMap<>();
        row.put("match_count", new BigDecimal(20));
        row.put("draw_count", new BigDecimal(5));
        row.put("win_count", new BigDecimal(10));
        row.put("loss_count", new BigDecimal(5));

        List<Map<String, Object>> matchResults = List.of(row);
        List<Integer> penaltyList = List.of(3);

        when(jdbcTemplate.queryForList(contains("year_count"), eq(year), eq(country))).thenReturn(matchResults);
        when(jdbcTemplate.queryForList(contains("yearly_penalties"), eq(Integer.class), eq(year), eq(country))).thenReturn(penaltyList);

        Map<String, Integer> stats = service.getYearCountryStatistics(year, country);

        assertEquals(20, stats.get("totalMatches"));
        assertEquals(5, stats.get("totalDraws"));
        assertEquals(10, stats.get("totalWins"));
        assertEquals(5, stats.get("totalLosses"));
        assertEquals(3, stats.get("totalPenalties"));
    }

    @Test
    public void testGetYearCountryStatistics_NoData() {
        int year = 2020;
        String country = "Country A";

        List<Map<String, Object>> emptyMatchResults = Collections.emptyList();
        List<Integer> emptyPenaltyList = Collections.emptyList();

        when(jdbcTemplate.queryForList(contains("year_count"), eq(year), eq(country))).thenReturn(emptyMatchResults);
        when(jdbcTemplate.queryForList(contains("yearly_penalties"), eq(Integer.class), eq(year), eq(country))).thenReturn(emptyPenaltyList);

        Map<String, Integer> stats = service.getYearCountryStatistics(year, country);

        assertEquals(0, stats.get("totalMatches"));
        assertEquals(0, stats.get("totalDraws"));
        assertEquals(0, stats.get("totalWins"));
        assertEquals(0, stats.get("totalLosses"));
        assertEquals(0, stats.get("totalPenalties"));
    }

    @Test
    public void testGetMatchResultsByYearAndCountryDetails() {
        int year = 2020;
        String continent = "Europe";
        String status = "Active";
        String developmentStatus = "Developed";

        List<Map<String, Object>> mockResults = List.of(
                Map.of(
                        "date", "2020-06-15",
                        "home_team", "Team A",
                        "away_team", "Team B",
                        "home_score", 2,
                        "away_score", 1,
                        "tournament", "Tournament 1",
                        "city", "City A",
                        "country", "Country A",
                        "neutral", "No",
                        "winner", "Team A"
                )
        );

        when(jdbcTemplate.queryForList(anyString(), eq(year), eq(continent), eq(status), eq(developmentStatus))).thenReturn(mockResults);

        List<Map<String, Object>> results = service.getMatchResultsByYearAndCountryDetails(year, continent, status, developmentStatus);

        assertEquals(1, results.size());
        assertEquals("Team A", results.get(0).get("home_team"));
        assertEquals("No", results.get(0).get("neutral"));
        assertEquals("Team A", results.get(0).get("winner"));
    }
}
