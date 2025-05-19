package cs.uoi.football.statistics.ControllerTest;

import cs.uoi.football.statistics.Controller.Global_Stats_Profile_Controller;
import cs.uoi.football.statistics.Service.Global_Stats_Profile_Service;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.ui.Model;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class Global_Stats_Profile_Controller_Test {

    @InjectMocks
    private Global_Stats_Profile_Controller controller;

    @Mock
    private Global_Stats_Profile_Service globalStatsProfileService;

    @Mock
    private Model model;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testShowGlobalStats() {
        // Mock data
        List<Map<String, Object>> mockList = Collections.singletonList(
                Map.of("country", "Testland", "value", 100)
        );

        // Stub service methods
        when(globalStatsProfileService.getCountryScoreVsPopulation()).thenReturn(mockList);
        when(globalStatsProfileService.getTop10CountriesByAvgWinsPerYear()).thenReturn(mockList);
        when(globalStatsProfileService.getTop10CountriesByAvgScorePerYear()).thenReturn(mockList);
        when(globalStatsProfileService.getTop10CountriesByWins()).thenReturn(mockList);
        when(globalStatsProfileService.getTop10CountriesByScore()).thenReturn(mockList);

        // Call the controller method
        String viewName = controller.showGlobalStats(model);

        // Verify model attributes were set
        verify(model).addAttribute("scoreVsPopulation", mockList);
        verify(model).addAttribute("top10CountriesWinByYear", mockList);
        verify(model).addAttribute("top10CountriesScoreByYear", mockList);
        verify(model).addAttribute("topCountriesWin", mockList);
        verify(model).addAttribute("topCountriesScore", mockList);

        // Verify view name
        assertEquals("global-stats-profile", viewName);
    }
}
