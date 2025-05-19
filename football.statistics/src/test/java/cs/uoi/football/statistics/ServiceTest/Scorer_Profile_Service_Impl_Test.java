package cs.uoi.football.statistics.ServiceTest;

import cs.uoi.football.statistics.Service.Scorer_Profile_Service_Impl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import org.springframework.jdbc.core.JdbcTemplate;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

public class Scorer_Profile_Service_Impl_Test {

    @Mock
    private JdbcTemplate jdbcTemplate;

    @InjectMocks
    private Scorer_Profile_Service_Impl service;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetAllScorers() {
        List<String> mockScorers = List.of("Player A", "Player B", "Player C");

        when(jdbcTemplate.queryForList(anyString(), eq(String.class))).thenReturn(mockScorers);

        List<String> result = service.getAllScorers();

        assertEquals(3, result.size());
        assertEquals("Player A", result.get(0));
    }

    @Test
    public void testGetScorerYears() {
        String scorer = "Player A";
        List<Map<String, Object>> mockData = List.of(
                Map.of("scorer", scorer, "first_year", 2010, "last_year", 2020)
        );

        when(jdbcTemplate.queryForList(anyString(), eq(scorer))).thenReturn(mockData);

        List<Map<String, Object>> result = service.getScorerYears(scorer);

        assertEquals(1, result.size());
        assertEquals(scorer, result.get(0).get("scorer"));
        assertEquals(2010, result.get(0).get("first_year"));
        assertEquals(2020, result.get(0).get("last_year"));
    }

    @Test
    public void testGetScorerMatchGoals() {
        String scorer = "Player A";
        List<Map<String, Object>> mockData = List.of(
                Map.of("date", "2020-05-10", "home_team", "Team A", "away_team", "Team B", "player_goals", 2, "total_goals", 3)
        );

        when(jdbcTemplate.queryForList(anyString(), eq(scorer))).thenReturn(mockData);

        List<Map<String, Object>> result = service.getScorerMatchGoals(scorer);

        assertEquals(1, result.size());
        assertEquals("2020-05-10", result.get(0).get("date"));
        assertEquals(2, result.get(0).get("player_goals"));
        assertEquals(3, result.get(0).get("total_goals"));
    }

    @Test
    public void testGetScorerTeamGoals() {
        String scorer = "Player A";
        int firstYear = 2010;
        int lastYear = 2020;

        List<Map<String, Object>> mockData = List.of(
                Map.of("date", "2015-03-15", "home_team", "Team A", "away_team", "Team B", "team", "Team A", "total_team_goals", 3)
        );

        when(jdbcTemplate.queryForList(anyString(), eq(scorer), eq(firstYear), eq(lastYear))).thenReturn(mockData);

        List<Map<String, Object>> result = service.getScorerTeamGoals(scorer, firstYear, lastYear);

        assertEquals(1, result.size());
        assertEquals("Team A", result.get(0).get("team"));
        assertEquals(3, result.get(0).get("total_team_goals"));
    }

    @Test
    public void testGetScorerTeamGoalsPerYear() {
        String scorer = "Player A";
        int firstYear = 2010;
        int lastYear = 2020;

        List<Map<String, Object>> mockData = List.of(
                Map.of("year", 2015, "team", "Team A", "total_team_goals", 25)
        );

        when(jdbcTemplate.queryForList(anyString(), eq(scorer), eq(firstYear), eq(lastYear))).thenReturn(mockData);

        List<Map<String, Object>> result = service.getScorerTeamGoalsPerYear(scorer, firstYear, lastYear);

        assertEquals(1, result.size());
        assertEquals(2015, result.get(0).get("year"));
        assertEquals("Team A", result.get(0).get("team"));
        assertEquals(25, result.get(0).get("total_team_goals"));
    }
}
