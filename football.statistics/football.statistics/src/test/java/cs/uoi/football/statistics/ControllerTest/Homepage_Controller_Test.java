package cs.uoi.football.statistics.ControllerTest;

import cs.uoi.football.statistics.Controller.Homepage_Controller;
import cs.uoi.football.statistics.Service.Country_Profile_Service;
import cs.uoi.football.statistics.Service.Scorer_Profile_Service;
import cs.uoi.football.statistics.Service.Year_Profile_Service;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.ui.Model;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class Homepage_Controller_Test {

    @InjectMocks
    private Homepage_Controller homepageController;

    @Mock
    private Country_Profile_Service countryProfileService;

    @Mock
    private Year_Profile_Service yearProfileService;

    @Mock
    private Scorer_Profile_Service scorerProfileService;

    @Mock
    private Model model;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testShowHomePage() {
        // Arrange
        List<String> mockCountries = List.of("CountryA", "CountryB");
        List<Integer> mockYears = List.of(2020, 2021);
        List<String> mockScorers = List.of("Player1", "Player2");

        when(countryProfileService.getCountryNames()).thenReturn(mockCountries);
        when(yearProfileService.getDistinctYears()).thenReturn(mockYears);
        when(scorerProfileService.getAllScorers()).thenReturn(mockScorers);

        // Act
        String viewName = homepageController.showHomePage(model);

        // Assert
        verify(model).addAttribute("countryNames", mockCountries);
        verify(model).addAttribute("years", mockYears);
        verify(model).addAttribute("scorers", mockScorers);
        assertEquals("homepage", viewName);
    }
}
