package cs.uoi.football.statistics.ControllerTest;

import cs.uoi.football.statistics.Controller.Year_Profile_Controller;
import cs.uoi.football.statistics.Service.Year_Profile_Service;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.ui.Model;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class Year_Profile_Controller_Test {

    @InjectMocks
    private Year_Profile_Controller controller;

    @Mock
    private Year_Profile_Service yearProfileService;

    @Mock
    private Model model;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testShowYearProfile_withAllParams() {
        // Given
        int year = 2022;
        String country = "TestCountry";
        String continent = "Europe";
        String status = "Independent";
        String developmentStatus = "Developed";

        Map<String, Integer> yearStats = Map.of(
                "totalMatches", 100,
                "totalDraws", 20,
                "totalPenalties", 5
        );

        Map<String, Integer> yearCountryStats = Map.of(
                "totalMatches", 10,
                "totalDraws", 2,
                "totalWins", 6,
                "totalLosses", 2,
                "totalPenalties", 1
        );

        List<String> countries = List.of("TestCountry", "OtherCountry");
        List<Map<String, Object>> matchResults = List.of(
                Map.of("match", "Match A", "score", 3)
        );

        when(yearProfileService.getYearStatistics(year)).thenReturn(yearStats);
        when(yearProfileService.getDistinctCountries()).thenReturn(countries);
        when(yearProfileService.getMatchResultsByYearAndCountryDetails(year, continent, status, developmentStatus)).thenReturn(matchResults);
        when(yearProfileService.getYearCountryStatistics(year, country)).thenReturn(yearCountryStats);

        // When
        String viewName = controller.showYearProfile(year, country, continent, status, developmentStatus, model);

        // Then
        assertEquals("year-profile", viewName);

        verify(model).addAttribute("selectedYear", year);
        verify(model).addAttribute("totalMatches", 100);
        verify(model).addAttribute("totalDraws", 20);
        verify(model).addAttribute("totalPenalties", 5);
        verify(model).addAttribute("countries", countries);
        verify(model).addAttribute(eq("matchResults"), eq(matchResults));
        verify(model).addAttribute("yearCountryStats", yearCountryStats);
        verify(model).addAttribute("selectedCountry", country);
        verify(model).addAttribute("totalMatchesForCountry", 10);
        verify(model).addAttribute("totalDrawsForCountry", 2);
        verify(model).addAttribute("totalWinsForCountry", 6);
        verify(model).addAttribute("totalLossesForCountry", 2);
        verify(model).addAttribute("totalPenaltiesForCountry", 1);
    }

    @Test
    void testGetFilteredMatchResults() {
        // Given
        int year = 2022;
        String continent = "Europe";
        String status = "Independent";
        String developmentStatus = "Developed";

        List<Map<String, Object>> expectedResults = List.of(
                Map.of("match", "Match B", "goals", 4)
        );

        when(yearProfileService.getMatchResultsByYearAndCountryDetails(year, continent, status, developmentStatus))
                .thenReturn(expectedResults);

        // When
        List<Map<String, Object>> result = controller.getFilteredMatchResults(year, continent, status, developmentStatus);

        // Then
        assertEquals(expectedResults, result);
    }
}
