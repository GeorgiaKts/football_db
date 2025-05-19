package cs.uoi.football.statistics.ServiceTest.Create_And_Load_Test;

import cs.uoi.football.statistics.Service.Create_And_Load.Data_Loader_Service;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.jdbc.core.JdbcTemplate;

import java.lang.reflect.Method;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class Data_Loader_Service_Test {

    @InjectMocks
    private Data_Loader_Service dataLoaderService;

    @Mock
    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testIsTableEmptyReturnsTrueWhenNoRows() throws Exception {
        // Mock queryForList to return empty list
        when(jdbcTemplate.queryForList(anyString(), eq(Integer.class)))
                .thenReturn(Collections.emptyList());

        // Call private method isTableEmpty via reflection since it's private
        var method = Data_Loader_Service.class.getDeclaredMethod("isTableEmpty", String.class);
        method.setAccessible(true);

        boolean result = (boolean) method.invoke(dataLoaderService, "some_table");

        assertTrue(result);
        verify(jdbcTemplate).queryForList("SELECT 1 FROM some_table LIMIT 1", Integer.class);
    }

    @Test
    void testIsTableEmptyReturnsFalseWhenRowsExist() throws Exception {
        when(jdbcTemplate.queryForList(anyString(), eq(Integer.class)))
                .thenReturn(List.of(1));

        var method = Data_Loader_Service.class.getDeclaredMethod("isTableEmpty", String.class);
        method.setAccessible(true);

        boolean result = (boolean) method.invoke(dataLoaderService, "some_table");

        assertFalse(result);
        verify(jdbcTemplate).queryForList("SELECT 1 FROM some_table LIMIT 1", Integer.class);
    }

    // Helper to test that load methods execute correct SQL
    private void testLoadDataMethod(String methodName, String expectedQuery) throws Exception {
        var method = Data_Loader_Service.class.getDeclaredMethod(methodName);
        method.setAccessible(true);

        method.invoke(dataLoaderService);

        ArgumentCaptor<String> queryCaptor = ArgumentCaptor.forClass(String.class);
        verify(jdbcTemplate).execute(queryCaptor.capture());

        assertEquals(expectedQuery, queryCaptor.getValue());
    }

    @Test
    void testLoadCountriesData() throws Exception {
        String expectedQuery = "LOAD DATA INFILE 'C:/ProgramData/MySQL/MySQL Server 8.0/Uploads/cleaned_countries.csv' "
                + "INTO TABLE countries "
                + "FIELDS TERMINATED BY '\\t' "
                + "ENCLOSED BY '\"' "
                + "LINES TERMINATED BY '\\r\\n' "
                + "IGNORE 1 LINES (iso, iso3, iso_code, fips, display_name, official_name, capital, continent, \n" +
                " currency_code, currency_name, phone_code, region_code, region_name, \n" +
                " subregion_code, subregion_name, intermediate_region_code, intermediate_region_name, \n" +
                " status, development_status, sids, lldc, ldc, area_sqkm, population);";

        testLoadDataMethod("loadCountriesData", expectedQuery);
    }

    @Test
    void testLoadFormerNamesData() throws Exception {
        String expectedQuery = "LOAD DATA INFILE 'C:/ProgramData/MySQL/MySQL Server 8.0/Uploads/cleaned_former_names.csv' "
                + "INTO TABLE former_names "
                + "FIELDS TERMINATED BY '\\t' "
                + "ENCLOSED BY '\"' "
                + "LINES TERMINATED BY '\\r\\n' "
                + "IGNORE 1 LINES (current, former, start_date, end_date);";

        testLoadDataMethod("loadFormerNamesData", expectedQuery);
    }

    @Test
    void testLoadResultsData() throws Exception {
        String expectedQuery = "LOAD DATA INFILE 'C:/ProgramData/MySQL/MySQL Server 8.0/Uploads/cleaned_results.csv' "
                + "INTO TABLE results "
                + "FIELDS TERMINATED BY '\\t' "
                + "ENCLOSED BY '\"' "
                + "LINES TERMINATED BY '\\r\\n' "
                + "IGNORE 1 LINES (date, home_team, away_team, home_score, away_score, tournament, city, country, neutral);";

        testLoadDataMethod("loadResultsData", expectedQuery);
    }

    @Test
    void testLoadGoalscorersData() throws Exception {
        String expectedQuery = "LOAD DATA INFILE 'C:/ProgramData/MySQL/MySQL Server 8.0/Uploads/cleaned_goalscorers.csv' "
                + "INTO TABLE goalscorers "
                + "FIELDS TERMINATED BY '\\t' "
                + "ENCLOSED BY '\"' "
                + "LINES TERMINATED BY '\\r\\n' "
                + "IGNORE 1 LINES (date, home_team, away_team, team, scorer, minute, own_goal, penalty);";

        testLoadDataMethod("loadGoalscorersData", expectedQuery);
    }

    @Test
    void testLoadShootoutsData() throws Exception {
        String expectedQuery = "LOAD DATA INFILE 'C:/ProgramData/MySQL/MySQL Server 8.0/Uploads/cleaned_shootouts.csv' "
                + "INTO TABLE shootouts "
                + "FIELDS TERMINATED BY '\\t' "
                + "ENCLOSED BY '\"' "
                + "LINES TERMINATED BY '\\r\\n' "
                + "IGNORE 1 LINES (date, home_team, away_team, winner, first_shooter);";

        testLoadDataMethod("loadShootoutsData", expectedQuery);
    }

    @Test
    void testInsertIntoTeamsExecutesCorrectQuery() throws Exception {
        // Use reflection to get the private method insertIntoTeams
        var method = Data_Loader_Service.class.getDeclaredMethod("insertIntoTeams");
        method.setAccessible(true);

        // Invoke the method on your service instance
        method.invoke(dataLoaderService);

        // Capture the SQL query passed to jdbcTemplate.execute()
        ArgumentCaptor<String> queryCaptor = ArgumentCaptor.forClass(String.class);
        verify(jdbcTemplate).execute(queryCaptor.capture());

        String executedQuery = queryCaptor.getValue();

        // Assert that the query starts correctly
        assertTrue(executedQuery.startsWith("INSERT INTO teams (team) VALUES"));

        // Assert some expected team names exist in the query string to verify completeness
        assertTrue(executedQuery.contains("('Zimbabwe')"));
        assertTrue(executedQuery.contains("('Åland Islands')"));
    }

    @Test
    void testUpdateTeamsExecutesCorrectQueries() throws Exception {
        // Use reflection to access private method
        var method = Data_Loader_Service.class.getDeclaredMethod("updateTeams");
        method.setAccessible(true);

        // Invoke the method
        method.invoke(dataLoaderService);

        // Capture all the SQL queries passed to jdbcTemplate.execute()
        ArgumentCaptor<String> queryCaptor = ArgumentCaptor.forClass(String.class);
        verify(jdbcTemplate, times(3)).execute(queryCaptor.capture());

        List<String> executedQueries = queryCaptor.getAllValues();

        // Verify the first query disables safe updates
        assertEquals("SET SQL_SAFE_UPDATES = 0;", executedQueries.get(0));

        // Verify the last query enables safe updates
        assertEquals("SET SQL_SAFE_UPDATES = 1;", executedQueries.get(2));

        // Verify the big UPDATE query is in the middle
        String expectedUpdateQuery = "UPDATE teams "
                + "SET iso_code = CASE "
                + "WHEN team = 'DR Congo' THEN 180 "
                + "WHEN team = 'Curaçao' THEN 531 "
                + "WHEN team = 'China PR' THEN 156 "
                + "WHEN team = 'Czechoslovakia' THEN 200 "
                + "WHEN team = 'Republic of Ireland' THEN 372 "
                + "WHEN team = 'Yugoslavia' THEN 891 "
                + "WHEN team = 'Northern Ireland' THEN 826 "
                + "WHEN team = 'Wales' THEN 826 "
                + "WHEN team = 'Scotland' THEN 826 "
                + "WHEN team = 'England' THEN 826 "
                + "WHEN team = 'Saarland' THEN 276 "
                + "WHEN team = 'German DR' THEN 278 "
                + "WHEN team = 'Vietnam Republic' THEN 704 "
                + "WHEN team = 'Macau' THEN 446 "
                + "WHEN team = 'Yemen DPR' THEN 887 "
                + "WHEN team = 'Northern Cyprus' THEN 196 "
                + "WHEN team = 'Wallis Islands and Futuna' THEN 876 "
                + "WHEN team = 'Åland Islands' THEN 248 "
                + "WHEN team = 'Ynys Môn' THEN 826 "
                + "WHEN team = 'Tahiti' THEN 258 "
                + "WHEN team = 'Czech Republic' THEN 203 "
                + "WHEN team = 'North Macedonia' THEN 807 "
                + "WHEN team = 'São Tomé and Príncipe' THEN 678 "
                + "WHEN team = 'Palestine' THEN 275 "
                + "WHEN team = 'Vatican City' THEN 336 "
                + "WHEN team = 'Réunion' THEN 638 "
                + "WHEN team = 'Malaya' THEN 458 "
                + "WHEN team = 'Alderney' THEN 831 "
                + "WHEN team = 'Catalonia' THEN 724 "
                + "WHEN team = 'Basque Country' THEN 724 "
                + "WHEN team = 'Brittany' THEN 250 "
                + "WHEN team = 'Galicia' THEN 724 "
                + "WHEN team = 'Andalusia' THEN 724 "
                + "WHEN team = 'Central Spain' THEN 724 "
                + "WHEN team = 'Manchukuo' THEN 156 "
                + "WHEN team = 'Zanzibar' THEN 834 "
                + "WHEN team = 'Burma' THEN 104 "
                + "WHEN team = 'North Vietnam' THEN 704 "
                + "WHEN team = 'Western Australia' THEN 36 "
                + "WHEN team = 'Corsica' THEN 250 "
                + "WHEN team = 'Gotland' THEN 752 "
                + "WHEN team = 'Saare County' THEN 233 "
                + "WHEN team = 'Orkney' THEN 826 "
                + "WHEN team = 'Sark' THEN 831 "
                + "WHEN team = 'Shetland' THEN 826 "
                + "WHEN team = 'Isle of Wight' THEN 826 "
                + "WHEN team = 'Canary Islands' THEN 724 "
                + "WHEN team = 'Frøya' THEN 578 "
                + "WHEN team = 'Hitra' THEN 578 "
                + "WHEN team = 'Rhodes' THEN 300 "
                + "WHEN team = 'Occitania' THEN 250 "
                + "WHEN team = 'Western Isles' THEN 826 "
                + "WHEN team = 'Menorca' THEN 756 "
                + "WHEN team = 'Ticino' THEN 756 "
                + "WHEN team = 'Asturias' THEN 724 "
                + "WHEN team = 'Maule Sur' THEN 152 "
                + "WHEN team = 'Surrey' THEN 826 "
                + "WHEN team = 'Kernow' THEN 826 "
                + "WHEN team = 'Chechnya' THEN 643 "
                + "WHEN team = 'Găgăuzia' THEN 498 "
                + "WHEN team = 'Tibet' THEN 156 "
                + "WHEN team = 'Provence' THEN 250 "
                + "WHEN team = 'Padania' THEN 380 "
                + "WHEN team = 'Iraqi Kurdistan' THEN 368 "
                + "WHEN team = 'Gozo' THEN 470 "
                + "WHEN team = 'Bonaire' THEN 528 "
                + "WHEN team = 'Chagos Islands' THEN 86 "
                + "WHEN team = 'Darfur' THEN 729 "
                + "WHEN team = 'Saint Barthélemy' THEN 652 "
                + "WHEN team = 'Abkhazia' THEN 268 "
                + "WHEN team = 'Artsakh' THEN 31 "
                + "WHEN team = 'Madrid' THEN 724 "
                + "WHEN team = 'Ellan Vannin' THEN 833 "
                + "WHEN team = 'Somaliland' THEN 706 "
                + "WHEN team = 'Franconia' THEN 276 "
                + "WHEN team = 'South Ossetia' THEN 268 "
                + "WHEN team = 'County of Nice' THEN 250 "
                + "WHEN team = 'Székely Land' THEN 642 "
                + "WHEN team = 'Panjab' THEN 356 "
                + "WHEN team = 'Felvidék' THEN 703 "
                + "WHEN team = 'Luhansk PR' THEN 804 "
                + "WHEN team = 'Donetsk PR' THEN 804 "
                + "WHEN team = 'Western Armenia' THEN 792 "
                + "WHEN team = 'Délvidék' THEN 688 "
                + "WHEN team = 'Barawa' THEN 706 "
                + "WHEN team = 'Ryūkyū' THEN 392 "
                + "WHEN team = 'Kárpátalja' THEN 804 "
                + "WHEN team = 'Yorkshire' THEN 826 "
                + "WHEN team = 'Matabeleland' THEN 716 "
                + "WHEN team = 'Kabylia' THEN 12 "
                + "WHEN team = 'Parishes of Jersey' THEN 832 "
                + "WHEN team = 'Biafra' THEN 566 "
                + "WHEN team = 'Elba Island' THEN 380 "
                + "WHEN team = 'West Papua' THEN 360 "
                + "WHEN team = 'South Yemen' THEN 887 "
                + "WHEN team = 'Ambazonia' THEN 120 "
                + "WHEN team = 'Crimea' THEN 804 "
                + "WHEN team = 'Cilento' THEN 380 "
                + "WHEN team = 'British Guiana' THEN 328 "
                + "WHEN team = 'Bohemia' THEN 203 "
                + "WHEN team = 'Manchuria' THEN 156 "
                + "WHEN team = 'Southern Rhodesia' THEN 716 "
                + "WHEN team = 'Northern Rhodesia' THEN 894 "
                + "WHEN team = 'Tanganyika' THEN 834 "
                + "WHEN team = 'French Somaliland' THEN 262 "
                + "WHEN team = 'Belgian Congo' THEN 180 "
                + "WHEN team = 'Gold Coast' THEN 288 "
                + "WHEN team = 'Ceylon' THEN 144 "
                + "WHEN team = 'Portuguese Guinea' THEN 624 "
                + "WHEN team = 'New Hebrides' THEN 548 "
                + "WHEN team = 'Nyasaland' THEN 454 "
                + "WHEN team = 'United Arab Republic' THEN 818 "
                + "WHEN team = 'Dahomey' THEN 204 "
                + "WHEN team = 'Congo-Kinshasa' THEN 180 "
                + "WHEN team = 'Upper Volta' THEN 854 "
                + "WHEN team = 'Zaïre' THEN 180 "
                + "WHEN team = 'Western Samoa' THEN 882 "
                + "WHEN team = 'Yemen AR' THEN 887 "
                + "ELSE NULL END;";

        assertEquals(expectedUpdateQuery, executedQueries.get(1));
    }

    @Test
    void testUpdateTeamsIsoCodeExecutesCorrectQueries() throws Exception {
        // Access the private method
        var method = Data_Loader_Service.class.getDeclaredMethod("updateTeamsIsoCode");
        method.setAccessible(true);

        // Invoke the method
        method.invoke(dataLoaderService);

        // Capture all executed queries
        ArgumentCaptor<String> queryCaptor = ArgumentCaptor.forClass(String.class);
        verify(jdbcTemplate, times(3)).execute(queryCaptor.capture());

        List<String> executedQueries = queryCaptor.getAllValues();

        // Verify disabling safe updates
        assertEquals("SET SQL_SAFE_UPDATES = 0;", executedQueries.get(0));

        // Verify the main update query
        String expectedQuery = "UPDATE teams t "
                + "INNER JOIN countries c "
                + "ON t.team = c.display_name OR t.team = c.official_name "
                + "SET t.iso_code = c.iso_code "
                + "WHERE t.iso_code IS NULL;";
        assertEquals(expectedQuery, executedQueries.get(1));

        // Verify enabling safe updates
        assertEquals("SET SQL_SAFE_UPDATES = 1;", executedQueries.get(2));
    }

    @Test
    void testUpdateTeamsWithCountriesExecutesCorrectQueries() throws Exception {
        // Access the private method
        var method = Data_Loader_Service.class.getDeclaredMethod("updateTeamsWithCountries");
        method.setAccessible(true);

        // Invoke the method
        method.invoke(dataLoaderService);

        // Capture all executed queries
        ArgumentCaptor<String> queryCaptor = ArgumentCaptor.forClass(String.class);
        verify(jdbcTemplate, times(4)).execute(queryCaptor.capture());

        List<String> executedQueries = queryCaptor.getAllValues();

        // Verify disabling safe updates
        assertEquals("SET SQL_SAFE_UPDATES = 0;", executedQueries.get(0));

        // Verify first update query with LEFT JOIN on team names
        String firstUpdate = "UPDATE teams t "
                + "LEFT JOIN countries c "
                + "ON (t.team = c.display_name OR t.team = c.official_name) "
                + "SET t.id_countries = c.id_countries "
                + "WHERE t.id_countries IS NULL "
                + "AND (t.team = c.display_name OR t.team = c.official_name);";
        assertEquals(firstUpdate, executedQueries.get(1));

        // Verify second update query with LEFT JOIN on iso_code
        String secondUpdate = "UPDATE teams t "
                + "LEFT JOIN countries c "
                + "ON t.iso_code = c.iso_code "
                + "SET t.id_countries = c.id_countries "
                + "WHERE t.id_countries IS NULL "
                + "AND t.iso_code = c.iso_code;";
        assertEquals(secondUpdate, executedQueries.get(2));

        // Verify enabling safe updates
        assertEquals("SET SQL_SAFE_UPDATES = 1;", executedQueries.get(3));
    }

    @Test
    void testUpdateFormerNamesExecutesCorrectQueries() throws Exception {
        var method = Data_Loader_Service.class.getDeclaredMethod("updateFormerNames");
        method.setAccessible(true);

        method.invoke(dataLoaderService);

        ArgumentCaptor<String> queryCaptor = ArgumentCaptor.forClass(String.class);
        verify(jdbcTemplate, times(2)).execute(queryCaptor.capture());  // changed from 3 to 2

        List<String> executedQueries = queryCaptor.getAllValues();

        assertEquals("SET SQL_SAFE_UPDATES = 0;", executedQueries.get(0));

        String expectedUpdate = "UPDATE former_names fn "
                + "INNER JOIN teams t ON fn.current = t.team "
                + "SET fn.id_teams = t.id_teams;";
        assertEquals(expectedUpdate, executedQueries.get(1));
    }

    @Test
    void testUpdateResultsExecutesCorrectQueries() throws Exception {
        var method = Data_Loader_Service.class.getDeclaredMethod("updateResults");
        method.setAccessible(true);

        method.invoke(dataLoaderService);

        ArgumentCaptor<String> queryCaptor = ArgumentCaptor.forClass(String.class);
        verify(jdbcTemplate, times(6)).execute(queryCaptor.capture());

        List<String> executedQueries = queryCaptor.getAllValues();

        assertEquals("SET SQL_SAFE_UPDATES = 0;", executedQueries.get(0));

        String query1 = "UPDATE results r "
                + "INNER JOIN teams t ON r.home_team = t.team "
                + "SET r.fk_home_teams = t.id_teams;";
        assertEquals(query1, executedQueries.get(1));

        String query2 = "UPDATE results r "
                + "INNER JOIN teams t ON r.away_team = t.team "
                + "SET r.fk_away_teams = t.id_teams;";
        assertEquals(query2, executedQueries.get(2));

        String query3 = "UPDATE results r "
                + "INNER JOIN former_names fn ON r.home_team = fn.current "
                + "AND r.date BETWEEN fn.start_date AND fn.end_date "
                + "SET r.fk_home_fr = fn.id_former_names;";
        assertEquals(query3, executedQueries.get(3));

        String query4 = "UPDATE results r "
                + "INNER JOIN former_names fn ON r.away_team = fn.current "
                + "AND r.date BETWEEN fn.start_date AND fn.end_date "
                + "SET r.fk_away_fr = fn.id_former_names;";
        assertEquals(query4, executedQueries.get(4));

        assertEquals("SET SQL_SAFE_UPDATES = 1;", executedQueries.get(5));
    }

    @Test
    void testFindWinnerExecutesCorrectQueries() throws Exception {
        var method = Data_Loader_Service.class.getDeclaredMethod("findWinner");
        method.setAccessible(true);

        method.invoke(dataLoaderService);

        ArgumentCaptor<String> queryCaptor = ArgumentCaptor.forClass(String.class);
        verify(jdbcTemplate, times(2)).execute(queryCaptor.capture());

        List<String> executedQueries = queryCaptor.getAllValues();

        assertEquals("SET SQL_SAFE_UPDATES = 0;", executedQueries.get(0));

        String expectedUpdate = "UPDATE results r "
                + "LEFT JOIN shootouts s ON r.date = s.date "
                + "AND r.home_team = s.home_team "
                + "AND r.away_team = s.away_team "
                + "SET r.winner = CASE "
                + "WHEN r.home_score > r.away_score THEN r.home_team "
                + "WHEN r.home_score < r.away_score THEN r.away_team "
                + "ELSE s.winner END;";
        assertEquals(expectedUpdate, executedQueries.get(1));
    }

    @Test
    void testUpdateShootoutsExecutesCorrectQueries() throws Exception {
        var method = Data_Loader_Service.class.getDeclaredMethod("updateShootouts");
        method.setAccessible(true);

        method.invoke(dataLoaderService);

        ArgumentCaptor<String> queryCaptor = ArgumentCaptor.forClass(String.class);
        verify(jdbcTemplate, times(2)).execute(queryCaptor.capture());

        List<String> executedQueries = queryCaptor.getAllValues();

        assertEquals("SET SQL_SAFE_UPDATES = 0;", executedQueries.get(0));

        String expectedUpdate = "UPDATE shootouts so "
                + "INNER JOIN results r ON so.date = r.date "
                + "AND so.home_team = r.home_team "
                + "AND so.away_team = r.away_team "
                + "AND (r.home_score - r.away_score) = 0 "
                + "SET so.id_results = r.id_results;";
        assertEquals(expectedUpdate, executedQueries.get(1));
    }

    @Test
    void testUpdateGoalscorersExecutesCorrectQueries() throws Exception {
        var method = Data_Loader_Service.class.getDeclaredMethod("updateGoalscorers");
        method.setAccessible(true);

        method.invoke(dataLoaderService);

        ArgumentCaptor<String> queryCaptor = ArgumentCaptor.forClass(String.class);
        verify(jdbcTemplate, times(2)).execute(queryCaptor.capture());

        List<String> executedQueries = queryCaptor.getAllValues();

        assertEquals("SET SQL_SAFE_UPDATES = 0;", executedQueries.get(0));

        String expectedUpdate = "UPDATE goalscorers gs "
                + "INNER JOIN results r ON gs.date = r.date "
                + "AND gs.home_team = r.home_team "
                + "AND gs.away_team = r.away_team "
                + "SET gs.id_results = r.id_results;";
        assertEquals(expectedUpdate, executedQueries.get(1));
    }

    @Test
    void testCreateTeamParticipationView_WhenViewDoesNotExist_CreatesView() {
        when(jdbcTemplate.queryForObject(anyString(), eq(Integer.class))).thenReturn(0);

        dataLoaderService.createTeamParticipationView();

        verify(jdbcTemplate).queryForObject(
                eq("SELECT COUNT(*) FROM INFORMATION_SCHEMA.VIEWS WHERE TABLE_SCHEMA = DATABASE() AND TABLE_NAME = 'team_participation'"),
                eq(Integer.class)
        );

        ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
        verify(jdbcTemplate).execute(captor.capture());

        String createViewQuery = captor.getValue();
        assertTrue(createViewQuery.startsWith("CREATE VIEW team_participation AS"));
    }

    @Test
    void testCreateTeamParticipationView_WhenViewExists_DoesNotCreateView() {
        when(jdbcTemplate.queryForObject(anyString(), eq(Integer.class))).thenReturn(1);

        dataLoaderService.createTeamParticipationView();

        verify(jdbcTemplate).queryForObject(
                eq("SELECT COUNT(*) FROM INFORMATION_SCHEMA.VIEWS WHERE TABLE_SCHEMA = DATABASE() AND TABLE_NAME = 'team_participation'"),
                eq(Integer.class)
        );

        verify(jdbcTemplate, never()).execute(anyString());
    }

    @Test
    void testCreateTeamResultsView_WhenViewDoesNotExist_CreatesView() {
        when(jdbcTemplate.queryForObject(anyString(), eq(Integer.class))).thenReturn(0);

        dataLoaderService.createTeamResultsView();

        verify(jdbcTemplate).queryForObject(
                eq("SELECT COUNT(*) FROM INFORMATION_SCHEMA.VIEWS WHERE TABLE_SCHEMA = DATABASE() AND TABLE_NAME = 'team_results'"),
                eq(Integer.class)
        );

        ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
        verify(jdbcTemplate).execute(captor.capture());

        String createViewQuery = captor.getValue();
        assertTrue(createViewQuery.startsWith("CREATE VIEW team_results AS"));
    }

    @Test
    void testCreateTeamResultsView_WhenViewExists_DoesNotCreateView() {
        when(jdbcTemplate.queryForObject(anyString(), eq(Integer.class))).thenReturn(1);

        dataLoaderService.createTeamResultsView();

        verify(jdbcTemplate).queryForObject(
                eq("SELECT COUNT(*) FROM INFORMATION_SCHEMA.VIEWS WHERE TABLE_SCHEMA = DATABASE() AND TABLE_NAME = 'team_results'"),
                eq(Integer.class)
        );

        verify(jdbcTemplate, never()).execute(anyString());
    }

    @Test
    void testCreateYearCountView_WhenViewDoesNotExist_CreatesView() {
        when(jdbcTemplate.queryForObject(anyString(), eq(Integer.class))).thenReturn(0);

        dataLoaderService.createYearCountView();

        verify(jdbcTemplate).queryForObject(
                eq("SELECT COUNT(*) FROM INFORMATION_SCHEMA.VIEWS WHERE TABLE_SCHEMA = DATABASE() AND TABLE_NAME = 'year_count'"),
                eq(Integer.class)
        );

        ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
        verify(jdbcTemplate).execute(captor.capture());

        String createViewQuery = captor.getValue();
        assertTrue(createViewQuery.startsWith("CREATE VIEW year_count AS"));
    }

    @Test
    void testCreateYearCountView_WhenViewExists_DoesNotCreateView() {
        when(jdbcTemplate.queryForObject(anyString(), eq(Integer.class))).thenReturn(1);

        dataLoaderService.createYearCountView();

        verify(jdbcTemplate).queryForObject(
                eq("SELECT COUNT(*) FROM INFORMATION_SCHEMA.VIEWS WHERE TABLE_SCHEMA = DATABASE() AND TABLE_NAME = 'year_count'"),
                eq(Integer.class)
        );

        verify(jdbcTemplate, never()).execute(anyString());
    }


    @Test
    void testCreateYearlyPenaltiesView_WhenViewDoesNotExist_CreatesView() {
        when(jdbcTemplate.queryForObject(anyString(), eq(Integer.class))).thenReturn(0);

        dataLoaderService.createYearlyPenaltiesView();

        verify(jdbcTemplate).queryForObject(
                eq("SELECT COUNT(*) FROM INFORMATION_SCHEMA.VIEWS WHERE TABLE_SCHEMA = DATABASE() AND TABLE_NAME = 'yearly_penalties'"),
                eq(Integer.class)
        );

        ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
        verify(jdbcTemplate).execute(captor.capture());

        String createViewQuery = captor.getValue();
        assertTrue(createViewQuery.startsWith("CREATE VIEW yearly_penalties AS"));
    }

    @Test
    void testCreateYearlyPenaltiesView_WhenViewExists_DoesNotCreateView() {
        when(jdbcTemplate.queryForObject(anyString(), eq(Integer.class))).thenReturn(1);

        dataLoaderService.createYearlyPenaltiesView();

        verify(jdbcTemplate).queryForObject(
                eq("SELECT COUNT(*) FROM INFORMATION_SCHEMA.VIEWS WHERE TABLE_SCHEMA = DATABASE() AND TABLE_NAME = 'yearly_penalties'"),
                eq(Integer.class)
        );

        verify(jdbcTemplate, never()).execute(anyString());
    }

    @Test
    void testCreateCountryTotalStatsView_WhenViewDoesNotExist_CreatesView() {
        when(jdbcTemplate.queryForObject(anyString(), eq(Integer.class))).thenReturn(0);

        dataLoaderService.createCountryTotalStatsView();

        verify(jdbcTemplate).queryForObject(
                eq("SELECT COUNT(*) FROM INFORMATION_SCHEMA.VIEWS WHERE TABLE_SCHEMA = DATABASE() AND TABLE_NAME = 'country_total_stats'"),
                eq(Integer.class)
        );

        ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
        verify(jdbcTemplate).execute(captor.capture());

        String createViewQuery = captor.getValue();
        assertTrue(createViewQuery.startsWith("CREATE VIEW country_total_stats AS"));
    }

    @Test
    void testCreateCountryTotalStatsView_WhenViewExists_DoesNotCreateView() {
        when(jdbcTemplate.queryForObject(anyString(), eq(Integer.class))).thenReturn(1);

        dataLoaderService.createCountryTotalStatsView();

        verify(jdbcTemplate).queryForObject(
                eq("SELECT COUNT(*) FROM INFORMATION_SCHEMA.VIEWS WHERE TABLE_SCHEMA = DATABASE() AND TABLE_NAME = 'country_total_stats'"),
                eq(Integer.class)
        );

        verify(jdbcTemplate, never()).execute(anyString());
    }

    @Test
    void testCreateScorerYearsView_WhenViewDoesNotExist_CreatesView() {
        when(jdbcTemplate.queryForObject(anyString(), eq(Integer.class))).thenReturn(0);

        dataLoaderService.createScorerYearsView();

        verify(jdbcTemplate).queryForObject(
                eq("SELECT COUNT(*) FROM INFORMATION_SCHEMA.VIEWS WHERE TABLE_SCHEMA = DATABASE() AND TABLE_NAME = 'scorer_years'"),
                eq(Integer.class)
        );

        ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
        verify(jdbcTemplate).execute(captor.capture());

        String createViewQuery = captor.getValue();
        assertTrue(createViewQuery.startsWith("CREATE VIEW scorer_years AS"));
    }

    @Test
    void testCreateScorerYearsView_WhenViewExists_DoesNotCreateView() {
        when(jdbcTemplate.queryForObject(anyString(), eq(Integer.class))).thenReturn(1);

        dataLoaderService.createScorerYearsView();

        verify(jdbcTemplate).queryForObject(
                eq("SELECT COUNT(*) FROM INFORMATION_SCHEMA.VIEWS WHERE TABLE_SCHEMA = DATABASE() AND TABLE_NAME = 'scorer_years'"),
                eq(Integer.class)
        );

        verify(jdbcTemplate, never()).execute(anyString());
    }

    @Test
    void testCreateScorerMatchView_WhenViewDoesNotExist_CreatesView() {
        when(jdbcTemplate.queryForObject(anyString(), eq(Integer.class))).thenReturn(0);

        dataLoaderService.createScorerMatchView();

        verify(jdbcTemplate).queryForObject(
                eq("SELECT COUNT(*) FROM INFORMATION_SCHEMA.VIEWS WHERE TABLE_SCHEMA = DATABASE() AND TABLE_NAME = 'scorer_match'"),
                eq(Integer.class)
        );

        ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
        verify(jdbcTemplate).execute(captor.capture());

        String createViewQuery = captor.getValue();
        assertTrue(createViewQuery.startsWith("CREATE VIEW scorer_match AS"));
    }

    @Test
    void testCreateScorerMatchView_WhenViewExists_DoesNotCreateView() {
        when(jdbcTemplate.queryForObject(anyString(), eq(Integer.class))).thenReturn(1);

        dataLoaderService.createScorerMatchView();

        verify(jdbcTemplate).queryForObject(
                eq("SELECT COUNT(*) FROM INFORMATION_SCHEMA.VIEWS WHERE TABLE_SCHEMA = DATABASE() AND TABLE_NAME = 'scorer_match'"),
                eq(Integer.class)
        );

        verify(jdbcTemplate, never()).execute(anyString());
    }

    @Test
    void testCreateGoalsMatchView_WhenViewDoesNotExist_CreatesView() {
        when(jdbcTemplate.queryForObject(anyString(), eq(Integer.class))).thenReturn(0);

        dataLoaderService.createGoalsMatchView();

        verify(jdbcTemplate).queryForObject(
                eq("SELECT COUNT(*) FROM INFORMATION_SCHEMA.VIEWS WHERE TABLE_SCHEMA = DATABASE() AND TABLE_NAME = 'goals_match'"),
                eq(Integer.class)
        );

        ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
        verify(jdbcTemplate).execute(captor.capture());

        String createViewQuery = captor.getValue();
        assertTrue(createViewQuery.startsWith("CREATE VIEW goals_match AS"));
    }

    @Test
    void testCreateGoalsMatchView_WhenViewExists_DoesNotCreateView() {
        when(jdbcTemplate.queryForObject(anyString(), eq(Integer.class))).thenReturn(1);

        dataLoaderService.createGoalsMatchView();

        verify(jdbcTemplate).queryForObject(
                eq("SELECT COUNT(*) FROM INFORMATION_SCHEMA.VIEWS WHERE TABLE_SCHEMA = DATABASE() AND TABLE_NAME = 'goals_match'"),
                eq(Integer.class)
        );

        verify(jdbcTemplate, never()).execute(anyString());
    }


}
