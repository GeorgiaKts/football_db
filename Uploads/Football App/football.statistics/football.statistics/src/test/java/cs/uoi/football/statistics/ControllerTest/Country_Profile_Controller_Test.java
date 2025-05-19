package cs.uoi.football.statistics.ControllerTest;

import cs.uoi.football.statistics.Controller.Country_Profile_Controller;
import cs.uoi.football.statistics.Service.Country_Profile_Service;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.ui.Model;
import org.springframework.validation.support.BindingAwareModelMap;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class Country_Profile_Controller_Test {

    @Mock
    private Country_Profile_Service service;

    @InjectMocks
    private Country_Profile_Controller controller;

    private Model model;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        model = new BindingAwareModelMap();
    }

    @Test
    public void testShowCountryProfile() {
        String country = "Greece";

        // Setup mock service responses
        List<Map<String, Object>> teams = new ArrayList<>();
        Map<String, Object> team = new HashMap<>();
        team.put("team", "TeamA");
        teams.add(team);

        Map<String, Integer> totalStats = Map.of("wins", 10, "losses", 5);
        Map<String, Integer> totalHomeStats = Map.of("wins", 6, "losses", 2);
        Map<String, Integer> totalAwayStats = Map.of("wins", 4, "losses", 3);

        when(service.getTeamsByCountry(country)).thenReturn(teams);
        when(service.getTotalCountryStatistics(country)).thenReturn(totalStats);
        when(service.getTotalCountryStatisticsByRole(country, "Home")).thenReturn(totalHomeStats);
        when(service.getTotalCountryStatisticsByRole(country, "Away")).thenReturn(totalAwayStats);

        // Call controller method
        String viewName = controller.showCountryProfile(country, model);

        // Verify
        assertEquals("countries-profile", viewName);
        assertEquals(country, model.getAttribute("countryName"));
        assertEquals(teams, model.getAttribute("teams"));
        assertEquals(totalStats, model.getAttribute("totalStats"));
        assertEquals(totalHomeStats, model.getAttribute("totalHomeStats"));
        assertEquals(totalAwayStats, model.getAttribute("totalAwayStats"));

        verify(service).getTeamsByCountry(country);
        verify(service).getTotalCountryStatistics(country);
        verify(service).getTotalCountryStatisticsByRole(country, "Home");
        verify(service).getTotalCountryStatisticsByRole(country, "Away");
    }

    @Test
    public void testLoadExtraStats() {
        String country = "Greece";

        Map<String, Object> team = new HashMap<>();
        team.put("team", "TeamA");

        List<Map<String, Object>> teams = new ArrayList<>();
        teams.add(team);

        Map<String, Integer> teamStats = Map.of("wins", 10, "losses", 5, "draws", 2, "total_matches", 17);
        Map<String, Integer> homeStats = Map.of("wins", 6, "losses", 2, "draws", 1, "total_matches", 9);
        Map<String, Integer> awayStats = Map.of("wins", 4, "losses", 3, "draws", 1, "total_matches", 8);

        when(service.getTeamsByCountry(country)).thenReturn(teams);
        when(service.getTeamStatistics("TeamA")).thenReturn(teamStats);
        when(service.getTeamStatisticsByRole("TeamA", "Home")).thenReturn(homeStats);
        when(service.getTeamStatisticsByRole("TeamA", "Away")).thenReturn(awayStats);

        List<Map<String, Object>> result = controller.loadExtraStats(country);

        // Check that the returned list contains the team enriched with stats
        Map<String, Object> resultTeam = result.get(0);
        assertEquals("TeamA", resultTeam.get("team"));
        assertEquals(10, resultTeam.get("wins"));
        assertEquals(6, resultTeam.get("home_wins"));
        assertEquals(4, resultTeam.get("away_wins"));

        verify(service).getTeamsByCountry(country);
        verify(service).getTeamStatistics("TeamA");
        verify(service).getTeamStatisticsByRole("TeamA", "Home");
        verify(service).getTeamStatisticsByRole("TeamA", "Away");
    }

    @Test
    public void testGetStatisticsPerYear() {
        String country = "Greece";
        Map<Integer, Map<String, Integer>> statsPerYear = Map.of(
                2020, Map.of("wins", 5, "losses", 3),
                2021, Map.of("wins", 7, "losses", 2)
        );

        when(service.getStatisticsPerYear(country)).thenReturn(statsPerYear);

        Map<Integer, Map<String, Integer>> result = controller.getStatisticsPerYear(country);
        assertEquals(statsPerYear, result);
        verify(service).getStatisticsPerYear(country);
    }

    @Test
    public void testGetHomeStatisticsPerYear() {
        String country = "Greece";
        Map<Integer, Map<String, Integer>> homeStats = Map.of(
                2020, Map.of("wins", 3, "losses", 1),
                2021, Map.of("wins", 4, "losses", 1)
        );

        when(service.getStatisticsPerYearByRole(country, "Home")).thenReturn(homeStats);

        Map<Integer, Map<String, Integer>> result = controller.getHomeStatisticsPerYear(country);
        assertEquals(homeStats, result);
        verify(service).getStatisticsPerYearByRole(country, "Home");
    }

    @Test
    public void testGetAwayStatisticsPerYear() {
        String country = "Greece";
        Map<Integer, Map<String, Integer>> awayStats = Map.of(
                2020, Map.of("wins", 2, "losses", 2),
                2021, Map.of("wins", 3, "losses", 1)
        );

        when(service.getStatisticsPerYearByRole(country, "Away")).thenReturn(awayStats);

        Map<Integer, Map<String, Integer>> result = controller.getAwayStatisticsPerYear(country);
        assertEquals(awayStats, result);
        verify(service).getStatisticsPerYearByRole(country, "Away");
    }

    @Test
    public void testGetMatchDetails() {
        String country = "Greece";
        int startYear = 2019;
        int endYear = 2021;

        Map<String, Object> team = new HashMap<>();
        team.put("team", "TeamA");

        List<Map<String, Object>> teams = List.of(team);

        List<Map<String, Object>> teamMatches = new ArrayList<>();
        Map<String, Object> match1 = new HashMap<>();
        match1.put("matchId", 1);
        teamMatches.add(match1);

        when(service.getTeamsByCountry(country)).thenReturn(teams);
        when(service.getMatchDetailsForCountry("TeamA", startYear, endYear)).thenReturn(teamMatches);

        List<Map<String, Object>> result = controller.getMatchDetails(country, startYear, endYear);

        assertEquals(1, result.size());
        assertEquals(match1, result.get(0));

        verify(service).getTeamsByCountry(country);
        verify(service).getMatchDetailsForCountry("TeamA", startYear, endYear);
    }
}
