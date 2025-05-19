package cs.uoi.football.statistics.ServiceTest;

import cs.uoi.football.statistics.Service.Global_Stats_Profile_Service_Impl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import org.springframework.jdbc.core.JdbcTemplate;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class Global_Stats_Profile_Service_Impl_Test {

    @Mock
    private JdbcTemplate jdbcTemplate;

    @InjectMocks
    private Global_Stats_Profile_Service_Impl service;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetTop10CountriesByWins() {
        List<Map<String, Object>> mockResult = List.of(
                Map.of("country_name", "CountryA", "total_wins", 100),
                Map.of("country_name", "CountryB", "total_wins", 90)
        );

        when(jdbcTemplate.queryForList(anyString())).thenReturn(mockResult);

        List<Map<String, Object>> result = service.getTop10CountriesByWins();

        assertEquals(2, result.size());
        assertEquals("CountryA", result.get(0).get("country_name"));
        assertEquals(100, result.get(0).get("total_wins"));
    }

    @Test
    public void testGetTop10CountriesByScore() {
        List<Map<String, Object>> mockResult = List.of(
                Map.of("country_name", "CountryX", "total_points", 150),
                Map.of("country_name", "CountryY", "total_points", 130)
        );

        when(jdbcTemplate.queryForList(anyString())).thenReturn(mockResult);

        List<Map<String, Object>> result = service.getTop10CountriesByScore();

        assertEquals(2, result.size());
        assertEquals("CountryX", result.get(0).get("country_name"));
        assertEquals(150, result.get(0).get("total_points"));
    }

    @Test
    public void testGetTop10CountriesByAvgWinsPerYear() {
        List<Map<String, Object>> mockResult = List.of(
                Map.of("country_name", "Country1", "total_wins", 100, "year_count", 10, "avg_wins_per_year", 10.0),
                Map.of("country_name", "Country2", "total_wins", 90, "year_count", 9, "avg_wins_per_year", 10.0)
        );

        when(jdbcTemplate.queryForList(anyString())).thenReturn(mockResult);

        List<Map<String, Object>> result = service.getTop10CountriesByAvgWinsPerYear();

        assertEquals(2, result.size());
        assertEquals("Country1", result.get(0).get("country_name"));
        assertEquals(10.0, result.get(0).get("avg_wins_per_year"));
    }

    @Test
    public void testGetTop10CountriesByAvgScorePerYear() {
        List<Map<String, Object>> mockResult = List.of(
                Map.of("country_name", "CountryA", "total_points", 150, "year_count", 10, "avg_score_per_year", 15.0),
                Map.of("country_name", "CountryB", "total_points", 130, "year_count", 10, "avg_score_per_year", 13.0)
        );

        when(jdbcTemplate.queryForList(anyString())).thenReturn(mockResult);

        List<Map<String, Object>> result = service.getTop10CountriesByAvgScorePerYear();

        assertEquals(2, result.size());
        assertEquals("CountryA", result.get(0).get("country_name"));
        assertEquals(15.0, result.get(0).get("avg_score_per_year"));
    }

    @Test
    public void testGetCountryScoreVsPopulation() {
        List<Map<String, Object>> mockResult = List.of(
                Map.of("country_name", "CountryX", "total_points", 200, "population", 5000000),
                Map.of("country_name", "CountryY", "total_points", 150, "population", 3000000)
        );

        when(jdbcTemplate.queryForList(anyString())).thenReturn(mockResult);

        List<Map<String, Object>> result = service.getCountryScoreVsPopulation();

        assertEquals(2, result.size());
        assertEquals("CountryX", result.get(0).get("country_name"));
        assertEquals(5000000, result.get(0).get("population"));
    }
}
