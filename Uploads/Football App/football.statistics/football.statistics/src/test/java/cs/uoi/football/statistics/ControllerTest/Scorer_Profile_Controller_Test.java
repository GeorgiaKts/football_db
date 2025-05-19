package cs.uoi.football.statistics.ControllerTest;

import cs.uoi.football.statistics.Controller.Scorer_Profile_Controller;
import cs.uoi.football.statistics.Service.Scorer_Profile_Service;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.ui.Model;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class Scorer_Profile_Controller_Test {

    @InjectMocks
    private Scorer_Profile_Controller controller;

    @Mock
    private Scorer_Profile_Service scorerProfileService;

    @Mock
    private Model model;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetScorerProfile() {
        // Arrange
        String scorerName = "John Doe";

        List<Map<String, Object>> scorerStats = List.of(
                Map.of("first_year", 2010, "last_year", 2020)
        );

        List<Map<String, Object>> mockMatchGoals = List.of(
                Map.of("match", "Match 1", "goals", 2)
        );

        List<Map<String, Object>> mockTeamGoalsPerMatch = List.of(
                Map.of("match", "Match A", "goals", 1)
        );

        List<Map<String, Object>> mockTeamGoalsPerYear = List.of(
                Map.of("year", 2010, "goals", 3),
                Map.of("year", 2011, "goals", 4)
        );

        when(scorerProfileService.getScorerYears(scorerName)).thenReturn(scorerStats);
        when(scorerProfileService.getScorerMatchGoals(scorerName)).thenReturn(mockMatchGoals);
        when(scorerProfileService.getScorerTeamGoals(scorerName, 2010, 2020)).thenReturn(mockTeamGoalsPerMatch);
        when(scorerProfileService.getScorerTeamGoalsPerYear(scorerName, 2010, 2020)).thenReturn(mockTeamGoalsPerYear);

        // Act
        String viewName = controller.getScorerProfile(scorerName, model);

        // Assert
        verify(model).addAttribute("scorer", scorerName);
        verify(model).addAttribute("scorerStats", scorerStats);
        verify(model).addAttribute("scorerMatchGoals", mockMatchGoals);
        verify(model).addAttribute("scorerTeamGoalsPerMatch", mockTeamGoalsPerMatch);
        verify(model).addAttribute("scorerTeamGoalsPerYear", mockTeamGoalsPerYear);
        assertEquals("scorer-profile", viewName);
    }
}
