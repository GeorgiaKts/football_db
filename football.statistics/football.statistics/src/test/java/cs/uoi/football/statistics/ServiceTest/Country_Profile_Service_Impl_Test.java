package cs.uoi.football.statistics.ServiceTest;

import cs.uoi.football.statistics.Service.Country_Profile_Service_Impl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.jdbc.core.JdbcTemplate;

import java.math.BigDecimal;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class Country_Profile_Service_Impl_Test {

    @Mock
    private JdbcTemplate jdbcTemplate;

    // Use spy to mock internal method calls of the service
    @Spy
    @InjectMocks
    private Country_Profile_Service_Impl service;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetCountryNames() {
        List<String> mockCountries = Arrays.asList("Argentina", "Brazil", "Germany");
        when(jdbcTemplate.queryForList(anyString(), eq(String.class))).thenReturn(mockCountries);

        List<String> result = service.getCountryNames();

        assertEquals(3, result.size());
        assertTrue(result.contains("Brazil"));
    }

    @Test
    public void testGetTeamStatistics() {
        Map<String, Object> dbResult = new HashMap<>();
        dbResult.put("wins", BigDecimal.valueOf(10));
        dbResult.put("losses", BigDecimal.valueOf(5));
        dbResult.put("draws", BigDecimal.valueOf(2));
        dbResult.put("total_matches", BigDecimal.valueOf(17));

        when(jdbcTemplate.queryForMap(anyString(), eq("France"))).thenReturn(dbResult);

        Map<String, Integer> stats = service.getTeamStatistics("France");

        assertEquals(10, stats.get("wins"));
        assertEquals(5, stats.get("losses"));
        assertEquals(2, stats.get("draws"));
        assertEquals(17, stats.get("total_matches"));
    }

    @Test
    public void testGetTeamsByCountry() {
        List<Map<String, Object>> mockList = new ArrayList<>();
        Map<String, Object> team = new HashMap<>();
        team.put("team", "Italy");
        team.put("first_year", 1930);
        team.put("last_year", 2022);
        team.put("years_participated", 18);
        mockList.add(team);

        when(jdbcTemplate.queryForList(anyString(), eq("Italy"))).thenReturn(mockList);

        List<Map<String, Object>> result = service.getTeamsByCountry("Italy");

        assertEquals(1, result.size());
        assertEquals("Italy", result.get(0).get("team"));
    }

    @Test
    public void testGetTeamStatisticsByRole_WithResult() {
        Map<String, Object> row = Map.of(
                "wins", BigDecimal.valueOf(3),
                "losses", BigDecimal.valueOf(2),
                "draws", BigDecimal.valueOf(1),
                "total_matches", BigDecimal.valueOf(6)
        );
        when(jdbcTemplate.queryForList(anyString(), eq("France"), eq("Home"))).thenReturn(List.of(row));

        Map<String, Integer> stats = service.getTeamStatisticsByRole("France", "Home");

        assertEquals(3, stats.get("wins"));
        assertEquals(2, stats.get("losses"));
        assertEquals(1, stats.get("draws"));
        assertEquals(6, stats.get("total_matches"));
    }

    @Test
    public void testGetTeamStatisticsByRole_NoResult() {
        when(jdbcTemplate.queryForList(anyString(), eq("Japan"), eq("Away"))).thenReturn(Collections.emptyList());

        Map<String, Integer> stats = service.getTeamStatisticsByRole("Japan", "Away");

        assertEquals(0, stats.get("wins"));
        assertEquals(0, stats.get("losses"));
        assertEquals(0, stats.get("draws"));
        assertEquals(0, stats.get("total_matches"));
    }

    @Test
    public void testGetMatchDetailsForCountry() {
        List<Map<String, Object>> mockData = List.of(Map.of(
                "home_team", "France", "away_team", "Germany", "home_score", 2, "away_score", 1
        ));
        when(jdbcTemplate.queryForList(anyString(), eq("France"), eq(2000), eq(2010))).thenReturn(mockData);

        List<Map<String, Object>> result = service.getMatchDetailsForCountry("France", 2000, 2010);

        assertEquals(1, result.size());
        assertEquals("France", result.get(0).get("home_team"));
    }

    @Test
    public void testGetTotalCountryStatistics() {
        // Prepare mock team list for the country
        List<Map<String, Object>> mockTeams = List.of(
                Map.of("team", "TeamA"),
                Map.of("team", "TeamB")
        );

        // Stub getTeamsByCountry to return mockTeams
        doReturn(mockTeams).when(service).getTeamsByCountry("CountryX");

        // Stub getTeamStatistics for each team
        Map<String, Integer> statsTeamA = Map.of(
                "wins", 5, "losses", 2, "draws", 3, "total_matches", 10
        );
        Map<String, Integer> statsTeamB = Map.of(
                "wins", 4, "losses", 1, "draws", 5, "total_matches", 10
        );

        doReturn(statsTeamA).when(service).getTeamStatistics("TeamA");
        doReturn(statsTeamB).when(service).getTeamStatistics("TeamB");

        Map<String, Integer> totalStats = service.getTotalCountryStatistics("CountryX");

        assertEquals(9, totalStats.get("total_wins"));
        assertEquals(3, totalStats.get("total_losses"));
        assertEquals(8, totalStats.get("total_draws"));
        assertEquals(20, totalStats.get("total_matches"));
    }

    @Test
    public void testGetTotalCountryStatisticsByRole() {
        // Prepare mock team list for the country
        List<Map<String, Object>> mockTeams = List.of(
                Map.of("team", "TeamX"),
                Map.of("team", "TeamY")
        );

        doReturn(mockTeams).when(service).getTeamsByCountry("CountryY");

        Map<String, Integer> statsTeamX = Map.of(
                "wins", 6, "losses", 3, "draws", 1, "total_matches", 10
        );
        Map<String, Integer> statsTeamY = Map.of(
                "wins", 2, "losses", 4, "draws", 4, "total_matches", 10
        );

        doReturn(statsTeamX).when(service).getTeamStatisticsByRole("TeamX", "Home");
        doReturn(statsTeamY).when(service).getTeamStatisticsByRole("TeamY", "Home");

        Map<String, Integer> totalStatsByRole = service.getTotalCountryStatisticsByRole("CountryY", "Home");

        assertEquals(8, totalStatsByRole.get("total_wins"));
        assertEquals(7, totalStatsByRole.get("total_losses"));
        assertEquals(5, totalStatsByRole.get("total_draws"));
        assertEquals(20, totalStatsByRole.get("total_matches"));
    }

    @Test
    public void testGetStatisticsPerYear() {
        List<Map<String, Object>> mockResults = List.of(
                Map.of(
                        "match_year", 2020,
                        "total_wins", 5,
                        "total_losses", 3,
                        "total_draws", 2,
                        "total_matches", 10
                ),
                Map.of(
                        "match_year", 2021,
                        "total_wins", 7,
                        "total_losses", 1,
                        "total_draws", 2,
                        "total_matches", 10
                )
        );

        when(jdbcTemplate.queryForList(anyString(), eq("CountryZ"))).thenReturn(mockResults);

        Map<Integer, Map<String, Integer>> statsPerYear = service.getStatisticsPerYear("CountryZ");

        assertEquals(2, statsPerYear.size());
        assertTrue(statsPerYear.containsKey(2020));
        assertEquals(5, statsPerYear.get(2020).get("wins"));
        assertEquals(1, statsPerYear.get(2021).get("losses"));
    }

    @Test
    public void testGetStatisticsPerYearByRole() {
        List<Map<String, Object>> mockResults = List.of(
                Map.of(
                        "match_year", 2019,
                        "total_wins", 4,
                        "total_losses", 4,
                        "total_draws", 2,
                        "total_matches", 10
                )
        );

        when(jdbcTemplate.queryForList(anyString(), eq("CountryW"), eq("Away"))).thenReturn(mockResults);

        Map<Integer, Map<String, Integer>> statsPerYearByRole = service.getStatisticsPerYearByRole("CountryW", "Away");

        assertEquals(1, statsPerYearByRole.size());
        assertEquals(4, statsPerYearByRole.get(2019).get("wins"));
        assertEquals(4, statsPerYearByRole.get(2019).get("losses"));
        assertEquals(2, statsPerYearByRole.get(2019).get("draws"));
        assertEquals(10, statsPerYearByRole.get(2019).get("total_matches"));
    }
}
