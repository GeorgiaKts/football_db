package cs.uoi.football.statistics.Service.Create_And_Load;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class Data_Loader_Service {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public void loadDataAndUpdate() {

        // Check if the 'countries' table is empty and load data if needed
        if (isTableEmpty("countries")) {
            // Load countries data
            loadCountriesData();
        }

        // Check if the 'teams' table is empty and insert data if needed
        if (isTableEmpty("teams")) {
            // Insert into teams
            insertIntoTeams();
            // Update teams
            updateTeams();
            updateTeamsIsoCode();
            updateTeamsWithCountries();
        }

        // Check if the 'former_names' table is empty and load data if needed
        if (isTableEmpty("former_names")) {
            // Load former_names data
            loadFormerNamesData();
            // Update former_names
            updateFormerNames();
        }

        // Check if the 'results' table is empty and load data if needed
        if (isTableEmpty("results")) {
            // Load results data
            loadResultsData();
            // Update results
            updateResults();
        }

        // Check if the 'shootouts' table is empty and load data if needed
        if (isTableEmpty("shootouts")) {
            // Load shootouts data
            loadShootoutsData();
            // Find winner for results table
            findWinner();
            // Update shootouts
            updateShootouts();
        }

        // Check if the 'goalscorers' table is empty and load data if needed
        if (isTableEmpty("goalscorers")) {
            // Load goalscorers data
            loadGoalscorersData();
            // Update goalscorers
            updateGoalscorers();
        }

        // Create team_participation view if not exists
        createTeamParticipationView();

        // Create team_results view if not exists
        createTeamResultsView();

        // Create year_count view if not exists
        createYearCountView();

        // Create yearly_penalties view if not exists
        createYearlyPenaltiesView();

        // Create country_total_stats view if not exists
        createCountryTotalStatsView();

        // Create scorer_years view if not exists
        createScorerYearsView();

        // Create scorer_match view if not exists
        createScorerMatchView();

        // Create goals_match view if not exists
        createGoalsMatchView();

        // Print a message indicating that the loading is complete
        System.out.println("Data loading and updates completed successfully.");
    }

    // Check if a table is empty
    private boolean isTableEmpty(String tableName) {
        // Perform a query to check if the table has any rows
        String checkQuery = "SELECT 1 FROM " + tableName + " LIMIT 1";
        List<Integer> result = jdbcTemplate.queryForList(checkQuery, Integer.class);

        // If no rows are found, the table is empty
        if (result.isEmpty()) {
            // Print the message only when the table is empty
            System.out.println(tableName + " is empty");
            return true; // Table is empty
        }

        // Table is not empty
        return false;
    }


    private void loadCountriesData() {
        String query = "LOAD DATA INFILE 'C:/ProgramData/MySQL/MySQL Server 8.0/Uploads/cleaned_countries.csv' "
                + "INTO TABLE countries "
                + "FIELDS TERMINATED BY '\\t' "
                + "ENCLOSED BY '\"' "
                + "LINES TERMINATED BY '\\r\\n' "
                + "IGNORE 1 LINES (iso, iso3, iso_code, fips, display_name, official_name, capital, continent, \n" +
                " currency_code, currency_name, phone_code, region_code, region_name, \n" +
                " subregion_code, subregion_name, intermediate_region_code, intermediate_region_name, \n" +
                " status, development_status, sids, lldc, ldc, area_sqkm, population);";
        jdbcTemplate.execute(query);
    }

    private void loadFormerNamesData() {
        String query = "LOAD DATA INFILE 'C:/ProgramData/MySQL/MySQL Server 8.0/Uploads/cleaned_former_names.csv' "
                + "INTO TABLE former_names "
                + "FIELDS TERMINATED BY '\\t' "
                + "ENCLOSED BY '\"' "
                + "LINES TERMINATED BY '\\r\\n' "
                + "IGNORE 1 LINES (current, former, start_date, end_date);";
        jdbcTemplate.execute(query);
    }

    private void loadResultsData() {
        String query = "LOAD DATA INFILE 'C:/ProgramData/MySQL/MySQL Server 8.0/Uploads/cleaned_results.csv' "
                + "INTO TABLE results "
                + "FIELDS TERMINATED BY '\\t' "
                + "ENCLOSED BY '\"' "
                + "LINES TERMINATED BY '\\r\\n' "
                + "IGNORE 1 LINES (date, home_team, away_team, home_score, away_score, tournament, city, country, neutral);";
        jdbcTemplate.execute(query);
    }

    private void loadGoalscorersData() {
        String query = "LOAD DATA INFILE 'C:/ProgramData/MySQL/MySQL Server 8.0/Uploads/cleaned_goalscorers.csv' "
                + "INTO TABLE goalscorers "
                + "FIELDS TERMINATED BY '\\t' "
                + "ENCLOSED BY '\"' "
                + "LINES TERMINATED BY '\\r\\n' "
                + "IGNORE 1 LINES (date, home_team, away_team, team, scorer, minute, own_goal, penalty);";
        jdbcTemplate.execute(query);
    }

    private void loadShootoutsData() {
        String query = "LOAD DATA INFILE 'C:/ProgramData/MySQL/MySQL Server 8.0/Uploads/cleaned_shootouts.csv' "
                + "INTO TABLE shootouts "
                + "FIELDS TERMINATED BY '\\t' "
                + "ENCLOSED BY '\"' "
                + "LINES TERMINATED BY '\\r\\n' "
                + "IGNORE 1 LINES (date, home_team, away_team, winner, first_shooter);";
        jdbcTemplate.execute(query);
    }

    // Insert new teams into the teams table
    private void insertIntoTeams() {
        String query = "INSERT INTO teams (team) VALUES "
                + "('Abkhazia'),"
                + "('Afghanistan'),"
                + "('Albania'),"
                + "('Alderney'),"
                + "('Algeria'),"
                + "('Ambazonia'),"
                + "('American Samoa'),"
                + "('Andalusia'),"
                + "('Andorra'),"
                + "('Angola'),"
                + "('Anguilla'),"
                + "('Antigua and Barbuda'),"
                + "('Arameans Suryoye'),"
                + "('Argentina'),"
                + "('Armenia'),"
                + "('Artsakh'),"
                + "('Aruba'),"
                + "('Asturias'),"
                + "('Australia'),"
                + "('Austria'),"
                + "('Aymara'),"
                + "('Azerbaijan'),"
                + "('Bahamas'),"
                + "('Bahrain'),"
                + "('Bangladesh'),"
                + "('Barawa'),"
                + "('Barbados'),"
                + "('Basque Country'),"
                + "('Belarus'),"
                + "('Belgium'),"
                + "('Belize'),"
                + "('Benin'),"
                + "('Bermuda'),"
                + "('Bhutan'),"
                + "('Biafra'),"
                + "('Bolivia'),"
                + "('Bonaire'),"
                + "('Bosnia and Herzegovina'),"
                + "('Botswana'),"
                + "('Brazil'),"
                + "('British Virgin Islands'),"
                + "('Brittany'),"
                + "('Brunei'),"
                + "('Bulgaria'),"
                + "('Burkina Faso'),"
                + "('Burma'),"
                + "('Burundi'),"
                + "('Cambodia'),"
                + "('Cameroon'),"
                + "('Canada'),"
                + "('Canary Islands'),"
                + "('Cape Verde'),"
                + "('Cascadia'),"
                + "('Catalonia'),"
                + "('Cayman Islands'),"
                + "('Central African Republic'),"
                + "('Central Spain'),"
                + "('Chad'),"
                + "('Chagos Islands'),"
                + "('Chameria'),"
                + "('Chechnya'),"
                + "('Chile'),"
                + "('China PR'),"
                + "('Cilento'),"
                + "('Colombia'),"
                + "('Comoros'),"
                + "('Congo'),"
                + "('Cook Islands'),"
                + "('Corsica'),"
                + "('Costa Rica'),"
                + "('County of Nice'),"
                + "('Crimea'),"
                + "('Croatia'),"
                + "('Cuba'),"
                + "('Curaçao'),"
                + "('Cyprus'),"
                + "('Czech Republic'),"
                + "('Czechoslovakia'),"
                + "('DR Congo'),"
                + "('Darfur'),"
                + "('Denmark'),"
                + "('Djibouti'),"
                + "('Dominica'),"
                + "('Dominican Republic'),"
                + "('Donetsk PR'),"
                + "('Délvidék'),"
                + "('Ecuador'),"
                + "('Egypt'),"
                + "('El Salvador'),"
                + "('Elba Island'),"
                + "('Ellan Vannin'),"
                + "('England'),"
                + "('Equatorial Guinea'),"
                + "('Eritrea'),"
                + "('Estonia'),"
                + "('Eswatini'),"
                + "('Ethiopia'),"
                + "('Falkland Islands'),"
                + "('Faroe Islands'),"
                + "('Felvidék'),"
                + "('Fiji'),"
                + "('Finland'),"
                + "('France'),"
                + "('Franconia'),"
                + "('French Guiana'),"
                + "('Frøya'),"
                + "('Gabon'),"
                + "('Galicia'),"
                + "('Gambia'),"
                + "('Georgia'),"
                + "('German DR'),"
                + "('Germany'),"
                + "('Ghana'),"
                + "('Gibraltar'),"
                + "('Gotland'),"
                + "('Gozo'),"
                + "('Greece'),"
                + "('Greenland'),"
                + "('Grenada'),"
                + "('Guadeloupe'),"
                + "('Guam'),"
                + "('Guatemala'),"
                + "('Guernsey'),"
                + "('Guinea'),"
                + "('Guinea-Bissau'),"
                + "('Guyana'),"
                + "('Găgăuzia'),"
                + "('Haiti'),"
                + "('Hitra'),"
                + "('Hmong'),"
                + "('Honduras'),"
                + "('Hong Kong'),"
                + "('Hungary'),"
                + "('Iceland'),"
                + "('India'),"
                + "('Indonesia'),"
                + "('Iran'),"
                + "('Iraq'),"
                + "('Iraqi Kurdistan'),"
                + "('Isle of Man'),"
                + "('Isle of Wight'),"
                + "('Israel'),"
                + "('Italy'),"
                + "('Ivory Coast'),"
                + "('Jamaica'),"
                + "('Japan'),"
                + "('Jersey'),"
                + "('Jordan'),"
                + "('Kabylia'),"
                + "('Kazakhstan'),"
                + "('Kenya'),"
                + "('Kernow'),"
                + "('Kiribati'),"
                + "('Kosovo'),"
                + "('Kuwait'),"
                + "('Kyrgyzstan'),"
                + "('Kárpátalja'),"
                + "('Laos'),"
                + "('Latvia'),"
                + "('Lebanon'),"
                + "('Lesotho'),"
                + "('Liberia'),"
                + "('Libya'),"
                + "('Liechtenstein'),"
                + "('Lithuania'),"
                + "('Luhansk PR'),"
                + "('Luxembourg'),"
                + "('Macau'),"
                + "('Madagascar'),"
                + "('Madrid'),"
                + "('Malawi'),"
                + "('Malaya'),"
                + "('Malaysia'),"
                + "('Maldives'),"
                + "('Mali'),"
                + "('Malta'),"
                + "('Manchukuo'),"
                + "('Mapuche'),"
                + "('Martinique'),"
                + "('Matabeleland'),"
                + "('Maule Sur'),"
                + "('Mauritania'),"
                + "('Mauritius'),"
                + "('Mayotte'),"
                + "('Menorca'),"
                + "('Mexico'),"
                + "('Micronesia'),"
                + "('Moldova'),"
                + "('Monaco'),"
                + "('Mongolia'),"
                + "('Montenegro'),"
                + "('Montserrat'),"
                + "('Morocco'),"
                + "('Mozambique'),"
                + "('Myanmar'),"
                + "('Namibia'),"
                + "('Nepal'),"
                + "('Netherlands'),"
                + "('New Caledonia'),"
                + "('New Zealand'),"
                + "('Nicaragua'),"
                + "('Niger'),"
                + "('Nigeria'),"
                + "('Niue'),"
                + "('North Korea'),"
                + "('North Macedonia'),"
                + "('North Vietnam'),"
                + "('Northern Cyprus'),"
                + "('Northern Ireland'),"
                + "('Northern Mariana Islands'),"
                + "('Norway'),"
                + "('Occitania'),"
                + "('Oman'),"
                + "('Orkney'),"
                + "('Padania'),"
                + "('Pakistan'),"
                + "('Palau'),"
                + "('Palestine'),"
                + "('Panama'),"
                + "('Panjab'),"
                + "('Papua New Guinea'),"
                + "('Paraguay'),"
                + "('Parishes of Jersey'),"
                + "('Peru'),"
                + "('Philippines'),"
                + "('Poland'),"
                + "('Portugal'),"
                + "('Provence'),"
                + "('Puerto Rico'),"
                + "('Qatar'),"
                + "('Raetia'),"
                + "('Republic of Ireland'),"
                + "('Republic of St. Pauli'),"
                + "('Rhodes'),"
                + "('Romani people'),"
                + "('Romania'),"
                + "('Russia'),"
                + "('Rwanda'),"
                + "('Ryūkyū'),"
                + "('Réunion'),"
                + "('Saare County'),"
                + "('Saarland'),"
                + "('Saint Barthélemy'),"
                + "('Saint Helena'),"
                + "('Saint Kitts and Nevis'),"
                + "('Saint Lucia'),"
                + "('Saint Martin'),"
                + "('Saint Pierre and Miquelon'),"
                + "('Saint Vincent and the Grenadines'),"
                + "('Samoa'),"
                + "('San Marino'),"
                + "('Sark'),"
                + "('Saudi Arabia'),"
                + "('Saugeais'),"
                + "('Scotland'),"
                + "('Sealand'),"
                + "('Seborga'),"
                + "('Senegal'),"
                + "('Serbia'),"
                + "('Seychelles'),"
                + "('Shetland'),"
                + "('Sierra Leone'),"
                + "('Silesia'),"
                + "('Singapore'),"
                + "('Sint Maarten'),"
                + "('Slovakia'),"
                + "('Slovenia'),"
                + "('Solomon Islands'),"
                + "('Somalia'),"
                + "('Somaliland'),"
                + "('South Africa'),"
                + "('South Korea'),"
                + "('South Ossetia'),"
                + "('South Sudan'),"
                + "('South Yemen'),"
                + "('Spain'),"
                + "('Sri Lanka'),"
                + "('Sudan'),"
                + "('Suriname'),"
                + "('Surrey'),"
                + "('Sweden'),"
                + "('Switzerland'),"
                + "('Syria'),"
                + "('Székely Land'),"
                + "('Sápmi'),"
                + "('São Tomé and Príncipe'),"
                + "('Tahiti'),"
                + "('Taiwan'),"
                + "('Tajikistan'),"
                + "('Tamil Eelam'),"
                + "('Tanzania'),"
                + "('Thailand'),"
                + "('Tibet'),"
                + "('Ticino'),"
                + "('Timor-Leste'),"
                + "('Togo'),"
                + "('Tonga'),"
                + "('Trinidad and Tobago'),"
                + "('Tunisia'),"
                + "('Turkey'),"
                + "('Turkmenistan'),"
                + "('Turks and Caicos Islands'),"
                + "('Tuvalu'),"
                + "('Two Sicilies'),"
                + "('Uganda'),"
                + "('Ukraine'),"
                + "('United Arab Emirates'),"
                + "('United Koreans in Japan'),"
                + "('United States'),"
                + "('United States Virgin Islands'),"
                + "('Uruguay'),"
                + "('Uzbekistan'),"
                + "('Vanuatu'),"
                + "('Vatican City'),"
                + "('Venezuela'),"
                + "('Vietnam'),"
                + "('Vietnam Republic'),"
                + "('Wales'),"
                + "('Wallis Islands and Futuna'),"
                + "('West Papua'),"
                + "('Western Armenia'),"
                + "('Western Australia'),"
                + "('Western Isles'),"
                + "('Western Sahara'),"
                + "('Yemen'),"
                + "('Yemen DPR'),"
                + "('Ynys Môn'),"
                + "('Yorkshire'),"
                + "('Yoruba Nation'),"
                + "('Yugoslavia'),"
                + "('Zambia'),"
                + "('Zanzibar'),"
                + "('Zimbabwe'),"
                + "('Åland Islands');";
        jdbcTemplate.execute(query);
    }

    private void updateTeams() {
        // Disable safe updates temporarily (to allow updates without keys)
        String disableSafeUpdates = "SET SQL_SAFE_UPDATES = 0;";
        jdbcTemplate.execute(disableSafeUpdates);

        // The query to update the teams
        String query = "UPDATE teams "
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

        // Execute the update query
        jdbcTemplate.execute(query);

        // Enable safe updates again after the operations
        String enableSafeUpdates = "SET SQL_SAFE_UPDATES = 1;";
        jdbcTemplate.execute(enableSafeUpdates);
    }

    private void updateTeamsIsoCode() {
        // Disable safe updates temporarily (to allow updates without keys)
        String disableSafeUpdates = "SET SQL_SAFE_UPDATES = 0;";
        jdbcTemplate.execute(disableSafeUpdates);

        // SQL query to update teams' iso_code based on matching team name with country names
        String query = "UPDATE teams t "
                + "INNER JOIN countries c "
                + "ON t.team = c.display_name OR t.team = c.official_name "
                + "SET t.iso_code = c.iso_code "
                + "WHERE t.iso_code IS NULL;";

        // Execute the update query
        jdbcTemplate.execute(query);

        // Enable safe updates again after the operations
        String enableSafeUpdates = "SET SQL_SAFE_UPDATES = 1;";
        jdbcTemplate.execute(enableSafeUpdates);
    }


    private void updateTeamsWithCountries() {
        // Disable safe updates temporarily (to allow updates without keys)
        String disableSafeUpdates = "SET SQL_SAFE_UPDATES = 0;";
        jdbcTemplate.execute(disableSafeUpdates);

        // SQL query to update teams with countries based on team names or iso_code
        String query = "UPDATE teams t "
                + "LEFT JOIN countries c "
                + "ON (t.team = c.display_name OR t.team = c.official_name) "
                + "SET t.id_countries = c.id_countries "
                + "WHERE t.id_countries IS NULL "
                + "AND (t.team = c.display_name OR t.team = c.official_name);";

        // Execute the first query for matching team names
        jdbcTemplate.execute(query);

        // SQL query to update teams with countries based on iso_code (if no match found previously)
        String secondQuery = "UPDATE teams t "
                + "LEFT JOIN countries c "
                + "ON t.iso_code = c.iso_code "
                + "SET t.id_countries = c.id_countries "
                + "WHERE t.id_countries IS NULL "
                + "AND t.iso_code = c.iso_code;";

        // Execute the second query for matching iso_code
        jdbcTemplate.execute(secondQuery);

        // Enable safe updates again after the operations
        String enableSafeUpdates = "SET SQL_SAFE_UPDATES = 1;";
        jdbcTemplate.execute(enableSafeUpdates);
    }

    private void updateFormerNames() {
        // Disable safe updates temporarily (to allow updates without keys)
        String disableSafeUpdates = "SET SQL_SAFE_UPDATES = 0;";
        jdbcTemplate.execute(disableSafeUpdates);

        String query = "UPDATE former_names fn "
                + "INNER JOIN teams t ON fn.current = t.team "
                + "SET fn.id_teams = t.id_teams;";

        // Enable safe updates again after the operations
        String enableSafeUpdates = "SET SQL_SAFE_UPDATES = 1;";
        jdbcTemplate.execute(query);
    }

    private void updateResults() {
        // Disable safe updates temporarily (to allow updates without keys)
        String disableSafeUpdates = "SET SQL_SAFE_UPDATES = 0;";
        jdbcTemplate.execute(disableSafeUpdates);

        String query1 = "UPDATE results r "
                + "INNER JOIN teams t ON r.home_team = t.team "
                + "SET r.fk_home_teams = t.id_teams;";
        jdbcTemplate.execute(query1);

        // Update fk_away_teams with the corresponding id_teams from the teams table
        String query2 = "UPDATE results r "
                + "INNER JOIN teams t ON r.away_team = t.team "
                + "SET r.fk_away_teams = t.id_teams;";
        jdbcTemplate.execute(query2);

        // Update fk_home_fr with the corresponding id_former_names from the former_names table for home team
        String query3 = "UPDATE results r "
                + "INNER JOIN former_names fn ON r.home_team = fn.current "
                + "AND r.date BETWEEN fn.start_date AND fn.end_date "
                + "SET r.fk_home_fr = fn.id_former_names;";
        jdbcTemplate.execute(query3);

        // Update fk_away_fr with the corresponding id_former_names from the former_names table for away team
        String query4 = "UPDATE results r "
                + "INNER JOIN former_names fn ON r.away_team = fn.current "
                + "AND r.date BETWEEN fn.start_date AND fn.end_date "
                + "SET r.fk_away_fr = fn.id_former_names;";
        jdbcTemplate.execute(query4);

        // Enable safe updates again after the operations
        String enableSafeUpdates = "SET SQL_SAFE_UPDATES = 1;";
        jdbcTemplate.execute(enableSafeUpdates);
    }

    private void findWinner() {
        // Disable safe updates temporarily (to allow updates without keys)
        String disableSafeUpdates = "SET SQL_SAFE_UPDATES = 0;";
        jdbcTemplate.execute(disableSafeUpdates);

        String query = "UPDATE results r "
                + "LEFT JOIN shootouts s ON r.date = s.date "
                + "AND r.home_team = s.home_team "
                + "AND r.away_team = s.away_team "
                + "SET r.winner = CASE "
                + "WHEN r.home_score > r.away_score THEN r.home_team "
                + "WHEN r.home_score < r.away_score THEN r.away_team "
                + "ELSE s.winner END;";

        // Enable safe updates again after the operations
        String enableSafeUpdates = "SET SQL_SAFE_UPDATES = 1;";
        jdbcTemplate.execute(query);
    }

    private void updateShootouts() {
        // Disable safe updates temporarily (to allow updates without keys)
        String disableSafeUpdates = "SET SQL_SAFE_UPDATES = 0;";
        jdbcTemplate.execute(disableSafeUpdates);

        String query = "UPDATE shootouts so "
                + "INNER JOIN results r ON so.date = r.date "
                + "AND so.home_team = r.home_team "
                + "AND so.away_team = r.away_team "
                + "AND (r.home_score - r.away_score) = 0 "
                + "SET so.id_results = r.id_results;";

        // Enable safe updates again after the operations
        String enableSafeUpdates = "SET SQL_SAFE_UPDATES = 1;";
        jdbcTemplate.execute(query);
    }

    private void updateGoalscorers() {
        // Disable safe updates temporarily (to allow updates without keys)
        String disableSafeUpdates = "SET SQL_SAFE_UPDATES = 0;";
        jdbcTemplate.execute(disableSafeUpdates);

        String query = "UPDATE goalscorers gs "
                + "INNER JOIN results r ON gs.date = r.date "
                + "AND gs.home_team = r.home_team "
                + "AND gs.away_team = r.away_team "
                + "SET gs.id_results = r.id_results;";

        // Enable safe updates again after the operations
        String enableSafeUpdates = "SET SQL_SAFE_UPDATES = 1;";
        jdbcTemplate.execute(query);
    }

    public void createTeamParticipationView() {
        // Query to check if the view exists
        String checkViewQuery = "SELECT COUNT(*) FROM INFORMATION_SCHEMA.VIEWS WHERE TABLE_SCHEMA = DATABASE() AND TABLE_NAME = 'team_participation'";
        Integer count = jdbcTemplate.queryForObject(checkViewQuery, Integer.class);

        // If the view does not exist, create it
        if (count == null || count == 0) {
            String createViewQuery = "CREATE VIEW team_participation AS "
                    + "SELECT "
                    + "    c.id_countries, "
                    + "    c.display_name AS country_name, "
                    + "    t.team, "
                    + "    MIN(YEAR(r.date)) AS first_year, "
                    + "    MAX(YEAR(r.date)) AS last_year, "
                    + "    COUNT(DISTINCT YEAR(r.date)) AS years_participated "
                    + "FROM "
                    + "    countries c "
                    + "JOIN teams t ON c.id_countries = t.id_countries "
                    + "JOIN results r ON t.team = r.home_team OR t.team = r.away_team "
                    + "GROUP BY "
                    + "    c.id_countries, c.display_name, t.team";

            jdbcTemplate.execute(createViewQuery);
            System.out.println("View 'team_participation' has been created successfully.");
        } else {
            System.out.println("View 'team_participation' already exists.");
        }
    }


    public void createTeamResultsView() {
        // Query to check if the view exists
        String checkViewQuery = "SELECT COUNT(*) FROM INFORMATION_SCHEMA.VIEWS WHERE TABLE_SCHEMA = DATABASE() AND TABLE_NAME = 'team_results'";
        Integer count = jdbcTemplate.queryForObject(checkViewQuery, Integer.class);

        // If the view does not exist, create it
        if (count == null || count == 0) {
            String createViewQuery = "CREATE VIEW team_results AS "
                    + "SELECT "
                    + "    tp.team, "
                    + "    tp.id_countries, "
                    + "    r.id_results, "
                    + "    CASE "
                    + "        WHEN tp.team = r.home_team THEN 'Home' "
                    + "        WHEN tp.team = r.away_team THEN 'Away' "
                    + "    END AS team_role, "
                    + "    CASE "
                    + "        WHEN r.winner = tp.team THEN 'Winner' "
                    + "        WHEN r.winner IS NULL AND r.home_score = r.away_score THEN 'Draw' "
                    + "        ELSE 'Loser' "
                    + "    END AS result, "
                    + "    YEAR(r.date) AS match_year "
                    + "FROM "
                    + "    team_participation tp "
                    + "JOIN "
                    + "    results r "
                    + "    ON tp.team = r.home_team OR tp.team = r.away_team";

            jdbcTemplate.execute(createViewQuery);
            System.out.println("View 'team_results' has been created successfully.");
        } else {
            System.out.println("View 'team_results' already exists.");
        }
    }

    public void createYearCountView() {
        // Query to check if the view exists
        String checkViewQuery = "SELECT COUNT(*) FROM INFORMATION_SCHEMA.VIEWS WHERE TABLE_SCHEMA = DATABASE() AND TABLE_NAME = 'year_count'";
        Integer count = jdbcTemplate.queryForObject(checkViewQuery, Integer.class);

        // If the view does not exist, create it
        if (count == null || count == 0) {
            String createViewQuery = "CREATE VIEW year_count AS "
                    + "WITH year_count_help AS ( "
                    + "    SELECT "
                    + "        YEAR(r.date) AS year, "
                    + "        tp.country_name, "
                    + "        tp.id_countries, "
                    + "        COUNT(*) AS match_count, "
                    + "        SUM(CASE WHEN r.winner = tp.team THEN 1 ELSE 0 END) AS win_count, "
                    + "        SUM(CASE WHEN r.winner IS NOT NULL AND r.winner <> tp.team THEN 1 ELSE 0 END) AS loss_count, "
                    + "        SUM(CASE WHEN r.winner IS NULL AND r.home_score = r.away_score THEN 1 ELSE 0 END) AS draw_count "
                    + "    FROM "
                    + "        team_participation tp "
                    + "    JOIN "
                    + "        results r ON tp.team = r.home_team OR tp.team = r.away_team "
                    + "    GROUP BY "
                    + "        YEAR(r.date), tp.team, tp.id_countries "
                    + ") "
                    + "SELECT "
                    + "    year, "
                    + "    country_name, "
                    + "    id_countries, "
                    + "    SUM(match_count) AS match_count, "
                    + "    SUM(win_count) AS win_count, "
                    + "    SUM(loss_count) AS loss_count, "
                    + "    SUM(draw_count) AS draw_count, "
                    + "    (SUM(win_count) * 3 + SUM(draw_count)) AS total_points "
                    + "FROM "
                    + "    year_count_help "
                    + "GROUP BY "
                    + "    year, id_countries, country_name "
                    + "ORDER BY "
                    + "    total_points DESC, win_count DESC";

            jdbcTemplate.execute(createViewQuery);
            System.out.println("View 'year_count' has been created successfully.");
        } else {
            System.out.println("View 'year_count' already exists.");
        }
    }


    public void createYearlyPenaltiesView() {
        // Query to check if the view exists
        String checkViewQuery = "SELECT COUNT(*) FROM INFORMATION_SCHEMA.VIEWS WHERE TABLE_SCHEMA = DATABASE() AND TABLE_NAME = 'yearly_penalties'";
        Integer count = jdbcTemplate.queryForObject(checkViewQuery, Integer.class);

        // If the view does not exist, create it
        if (count == null || count == 0) {
            String createViewQuery = "CREATE VIEW yearly_penalties AS "
                    + "WITH yearly_penalties_help AS ( "
                    + "    SELECT "
                    + "        YEAR(g.date) AS year, "
                    + "        tp.country_name, "
                    + "        tp.id_countries, "
                    + "        COUNT(*) AS penalty_count "
                    + "    FROM "
                    + "        team_participation tp "
                    + "    JOIN "
                    + "        goalscorers g ON g.team = tp.team "
                    + "    WHERE "
                    + "        g.penalty = 'TRUE' "
                    + "    GROUP BY "
                    + "        YEAR(g.date), tp.country_name, tp.id_countries "
                    + ") "
                    + "SELECT "
                    + "    year, "
                    + "    country_name, "
                    + "    id_countries, "
                    + "    SUM(penalty_count) AS penalty_count "
                    + "FROM "
                    + "    yearly_penalties_help "
                    + "GROUP BY "
                    + "    year, country_name, id_countries "
                    + "ORDER BY "
                    + "    year, id_countries";

            jdbcTemplate.execute(createViewQuery);
            System.out.println("View 'yearly_penalties' has been created successfully.");
        } else {
            System.out.println("View 'yearly_penalties' already exists.");
        }
    }

    public void createCountryTotalStatsView() {
        // Query to check if the view exists
        String checkViewQuery = "SELECT COUNT(*) FROM INFORMATION_SCHEMA.VIEWS WHERE TABLE_SCHEMA = DATABASE() AND TABLE_NAME = 'country_total_stats'";
        Integer count = jdbcTemplate.queryForObject(checkViewQuery, Integer.class);

        // If the view does not exist, create it
        if (count == null || count == 0) {
            String createViewQuery = """
                CREATE VIEW country_total_stats AS
                WITH country_aggregates AS (
                    SELECT 
                        id_countries, 
                        country_name, 
                        COUNT(DISTINCT year) AS year_count,
                        SUM(win_count) AS total_wins, 
                        SUM(win_count) * 3 + SUM(draw_count) AS total_points
                    FROM year_count
                    GROUP BY id_countries, country_name
                ),
                top_wins AS (
                    SELECT 
                        country_name, 
                        CAST(SUM(total_wins) AS SIGNED) AS total_wins, 
                        NULL AS total_points,
                        NULL AS year_count
                    FROM country_aggregates
                    GROUP BY country_name
                    ORDER BY total_wins DESC
                ),
                top_points AS (
                    SELECT 
                        country_name, 
                        NULL AS total_wins, 
                        CAST(SUM(total_points) AS SIGNED) AS total_points,
                        NULL AS year_count
                    FROM country_aggregates
                    GROUP BY country_name
                    ORDER BY total_points DESC
                ),
                year_counts AS (
                    SELECT 
                        country_name,
                        NULL AS total_wins,
                        NULL AS total_points,
                        MAX(year_count) AS year_count
                    FROM country_aggregates
                    GROUP BY country_name
                )
                SELECT
                    country_name,
                    CAST(MAX(total_wins) AS SIGNED) AS total_wins,
                    CAST(MAX(total_points) AS SIGNED) AS total_points,
                    MAX(year_count) AS year_count
                FROM (
                    SELECT country_name, total_wins, total_points, year_count FROM top_wins
                    UNION
                    SELECT country_name, total_wins, total_points, year_count FROM top_points
                    UNION
                    SELECT country_name, total_wins, total_points, year_count FROM year_counts
                ) combined
                GROUP BY country_name;
                """;


            jdbcTemplate.execute(createViewQuery);
            System.out.println("View 'country_total_stats' has been created successfully.");
        } else {
            System.out.println("View 'country_total_stats' already exists.");
        }
    }

    public void createScorerYearsView() {
        // Query to check if the view exists
        String checkViewQuery = "SELECT COUNT(*) FROM INFORMATION_SCHEMA.VIEWS WHERE TABLE_SCHEMA = DATABASE() AND TABLE_NAME = 'scorer_years'";
        Integer count = jdbcTemplate.queryForObject(checkViewQuery, Integer.class);

        // If the view does not exist, create it
        if (count == null || count == 0) {
            String createViewQuery = "CREATE VIEW scorer_years AS "
                    + "SELECT "
                    + "    g.scorer, "
                    + "    MIN(YEAR(g.date)) AS first_year, "
                    + "    MAX(YEAR(g.date)) AS last_year "
                    + "FROM "
                    + "    goalscorers g "
                    + "WHERE "
                    + "    g.scorer IS NOT NULL "
                    + "GROUP BY "
                    + "    g.scorer";

            jdbcTemplate.execute(createViewQuery);
            System.out.println("View 'scorer_years' has been created successfully.");
        } else {
            System.out.println("View 'scorer_years' already exists.");
        }
    }

    public void createScorerMatchView() {
        // Query to check if the view exists
        String checkViewQuery = "SELECT COUNT(*) FROM INFORMATION_SCHEMA.VIEWS WHERE TABLE_SCHEMA = DATABASE() AND TABLE_NAME = 'scorer_match'";
        Integer count = jdbcTemplate.queryForObject(checkViewQuery, Integer.class);

        // If the view does not exist, create it
        if (count == null || count == 0) {
            String createViewQuery = "CREATE VIEW scorer_match AS "
                    + "SELECT "
                    + "    date, "
                    + "    home_team, "
                    + "    away_team, "
                    + "    scorer, "
                    + "    team, "
                    + "    COUNT(*) AS player_goals "
                    + "FROM "
                    + "    goalscorers "
                    + "WHERE "
                    + "    scorer IS NOT NULL "
                    + "GROUP BY "
                    + "    date, home_team, away_team, scorer, team";

            jdbcTemplate.execute(createViewQuery);
            System.out.println("View 'scorer_match' has been created successfully.");
        } else {
            System.out.println("View 'scorer_match' already exists.");
        }
    }

    public void createGoalsMatchView() {
        // Query to check if the view exists
        String checkViewQuery = "SELECT COUNT(*) FROM INFORMATION_SCHEMA.VIEWS WHERE TABLE_SCHEMA = DATABASE() AND TABLE_NAME = 'goals_match'";
        Integer count = jdbcTemplate.queryForObject(checkViewQuery, Integer.class);

        // If the view does not exist, create it
        if (count == null || count == 0) {
            String createViewQuery = "CREATE VIEW goals_match AS "
                    + "SELECT "
                    + "    date, "
                    + "    home_team, "
                    + "    away_team, "
                    + "    COUNT(*) AS total_goals "
                    + "FROM "
                    + "    goalscorers "
                    + "GROUP BY "
                    + "    date, home_team, away_team";

            jdbcTemplate.execute(createViewQuery);
            System.out.println("View 'goals_match' has been created successfully.");
        } else {
            System.out.println("View 'goals_match' already exists.");
        }
    }
}
