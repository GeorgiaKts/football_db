----------------------------------- Load file's data countries	
USE football_db;
SET GLOBAL local_infile = 1;

LOAD DATA LOCAL INFILE 'C:\Program Files\MySQL\MySQL Server 8.0\Uploads\Football App\cleaned_tsv_countries.csv'
INTO TABLE countries
FIELDS TERMINATED BY '\t'
ENCLOSED BY '"'
LINES TERMINATED BY '\r\n'
IGNORE 1 LINES;


----------------------------------- Load file's data former_names	
LOAD DATA INFILE 'C:\Program Files\MySQL\MySQL Server 8.0\Uploads\Football App\cleaned_tsv_former_names.csv'
INTO TABLE former_names
FIELDS TERMINATED BY '\t'
ENCLOSED BY '"'
LINES TERMINATED BY '\r\n'
IGNORE 1 LINES
(current, former, start_date, end_date);

----------------------------------- Load file's data results
LOAD DATA INFILE 'C:\Program Files\MySQL\MySQL Server 8.0\Uploads\Football App\cleaned_tsv_results.csv'
INTO TABLE results
FIELDS TERMINATED BY '\t'
ENCLOSED BY '"'
LINES TERMINATED BY '\r\n'
IGNORE 1 LINES
(date, home_team, away_team, home_score, away_score, tournament, city, country, neutral);

----------------------------------- Load file's data goalscorers
LOAD DATA INFILE 'C:\Program Files\MySQL\MySQL Server 8.0\Uploads\Football App\cleaned_tsv_goalscorers.csv'
INTO TABLE goalscorers
FIELDS TERMINATED BY '\t'
ENCLOSED BY '"'
LINES TERMINATED BY '\r\n'
IGNORE 1 LINES
(date, home_team, away_team, team, scorer, minute, own_goal, penalty);

----------------------------------- Load file's data shootouts
LOAD DATA INFILE 'C:\Program Files\MySQL\MySQL Server 8.0\Uploads\Football App\cleaned_tsv_shootouts.csv'
INTO TABLE shootouts
FIELDS TERMINATED BY '\t'
ENCLOSED BY '"'
LINES TERMINATED BY '\r\n'
IGNORE 1 LINES
(date, home_team, away_team, winner, first_shooter);

----------------------------------- Update teams
INSERT INTO teams (team)  
VALUES
('Abkhazia'),
('Afghanistan'),
('Albania'),
('Alderney'),
('Algeria'),
('Ambazonia'),
('American Samoa'),
('Andalusia'),
('Andorra'),
('Angola'),
('Anguilla'),
('Antigua and Barbuda'),
('Arameans Suryoye'),
('Argentina'),
('Armenia'),
('Artsakh'),
('Aruba'),
('Asturias'),
('Australia'),
('Austria'),
('Aymara'),
('Azerbaijan'),
('Bahamas'),
('Bahrain'),
('Bangladesh'),
('Barawa'),
('Barbados'),
('Basque Country'),
('Belarus'),
('Belgium'),
('Belize'),
('Benin'),
('Bermuda'),
('Bhutan'),
('Biafra'),
('Bolivia'),
('Bonaire'),
('Bosnia and Herzegovina'),
('Botswana'),
('Brazil'),
('British Virgin Islands'),
('Brittany'),
('Brunei'),
('Bulgaria'),
('Burkina Faso'),
('Burma'),
('Burundi'),
('Cambodia'),
('Cameroon'),
('Canada'),
('Canary Islands'),
('Cape Verde'),
('Cascadia'),
('Catalonia'),
('Cayman Islands'),
('Central African Republic'),
('Central Spain'),
('Chad'),
('Chagos Islands'),
('Chameria'),
('Chechnya'),
('Chile'),
('China PR'),
('Cilento'),
('Colombia'),
('Comoros'),
('Congo'),
('Cook Islands'),
('Corsica'),
('Costa Rica'),
('County of Nice'),
('Crimea'),
('Croatia'),
('Cuba'),
('Curaçao'),
('Cyprus'),
('Czech Republic'),
('Czechoslovakia'),
('DR Congo'),
('Darfur'),
('Denmark'),
('Djibouti'),
('Dominica'),
('Dominican Republic'),
('Donetsk PR'),
('Délvidék'),
('Ecuador'),
('Egypt'),
('El Salvador'),
('Elba Island'),
('Ellan Vannin'),
('England'),
('Equatorial Guinea'),
('Eritrea'),
('Estonia'),
('Eswatini'),
('Ethiopia'),
('Falkland Islands'),
('Faroe Islands'),
('Felvidék'),
('Fiji'),
('Finland'),
('France'),
('Franconia'),
('French Guiana'),
('Frøya'),
('Gabon'),
('Galicia'),
('Gambia'),
('Georgia'),
('German DR'),
('Germany'),
('Ghana'),
('Gibraltar'),
('Gotland'),
('Gozo'),
('Greece'),
('Greenland'),
('Grenada'),
('Guadeloupe'),
('Guam'),
('Guatemala'),
('Guernsey'),
('Guinea'),
('Guinea-Bissau'),
('Guyana'),
('Găgăuzia'),
('Haiti'),
('Hitra'),
('Hmong'),
('Honduras'),
('Hong Kong'),
('Hungary'),
('Iceland'),
('India'),
('Indonesia'),
('Iran'),
('Iraq'),
('Iraqi Kurdistan'),
('Isle of Man'),
('Isle of Wight'),
('Israel'),
('Italy'),
('Ivory Coast'),
('Jamaica'),
('Japan'),
('Jersey'),
('Jordan'),
('Kabylia'),
('Kazakhstan'),
('Kenya'),
('Kernow'),
('Kiribati'),
('Kosovo'),
('Kuwait'),
('Kyrgyzstan'),
('Kárpátalja'),
('Laos'),
('Latvia'),
('Lebanon'),
('Lesotho'),
('Liberia'),
('Libya'),
('Liechtenstein'),
('Lithuania'),
('Luhansk PR'),
('Luxembourg'),
('Macau'),
('Madagascar'),
('Madrid'),
('Malawi'),
('Malaya'),
('Malaysia'),
('Maldives'),
('Mali'),
('Malta'),
('Manchukuo'),
('Mapuche'),
('Martinique'),
('Matabeleland'),
('Maule Sur'),
('Mauritania'),
('Mauritius'),
('Mayotte'),
('Menorca'),
('Mexico'),
('Micronesia'),
('Moldova'),
('Monaco'),
('Mongolia'),
('Montenegro'),
('Montserrat'),
('Morocco'),
('Mozambique'),
('Myanmar'),
('Namibia'),
('Nepal'),
('Netherlands'),
('New Caledonia'),
('New Zealand'),
('Nicaragua'),
('Niger'),
('Nigeria'),
('Niue'),
('North Korea'),
('North Macedonia'),
('North Vietnam'),
('Northern Cyprus'),
('Northern Ireland'),
('Northern Mariana Islands'),
('Norway'),
('Occitania'),
('Oman'),
('Orkney'),
('Padania'),
('Pakistan'),
('Palau'),
('Palestine'),
('Panama'),
('Panjab'),
('Papua New Guinea'),
('Paraguay'),
('Parishes of Jersey'),
('Peru'),
('Philippines'),
('Poland'),
('Portugal'),
('Provence'),
('Puerto Rico'),
('Qatar'),
('Raetia'),
('Republic of Ireland'),
('Republic of St. Pauli'),
('Rhodes'),
('Romani people'),
('Romania'),
('Russia'),
('Rwanda'),
('Ryūkyū'),
('Réunion'),
('Saare County'),
('Saarland'),
('Saint Barthélemy'),
('Saint Helena'),
('Saint Kitts and Nevis'),
('Saint Lucia'),
('Saint Martin'),
('Saint Pierre and Miquelon'),
('Saint Vincent and the Grenadines'),
('Samoa'),
('San Marino'),
('Sark'),
('Saudi Arabia'),
('Saugeais'),
('Scotland'),
('Sealand'),
('Seborga'),
('Senegal'),
('Serbia'),
('Seychelles'),
('Shetland'),
('Sierra Leone'),
('Silesia'),
('Singapore'),
('Sint Maarten'),
('Slovakia'),
('Slovenia'),
('Solomon Islands'),
('Somalia'),
('Somaliland'),
('South Africa'),
('South Korea'),
('South Ossetia'),
('South Sudan'),
('South Yemen'),
('Spain'),
('Sri Lanka'),
('Sudan'),
('Suriname'),
('Surrey'),
('Sweden'),
('Switzerland'),
('Syria'),
('Székely Land'),
('Sápmi'),
('São Tomé and Príncipe'),
('Tahiti'),
('Taiwan'),
('Tajikistan'),
('Tamil Eelam'),
('Tanzania'),
('Thailand'),
('Tibet'),
('Ticino'),
('Timor-Leste'),
('Togo'),
('Tonga'),
('Trinidad and Tobago'),
('Tunisia'),
('Turkey'),
('Turkmenistan'),
('Turks and Caicos Islands'),
('Tuvalu'),
('Two Sicilies'),
('Uganda'),
('Ukraine'),
('United Arab Emirates'),
('United Koreans in Japan'),
('United States'),
('United States Virgin Islands'),
('Uruguay'),
('Uzbekistan'),
('Vanuatu'),
('Vatican City'),
('Venezuela'),
('Vietnam'),
('Vietnam Republic'),
('Wales'),
('Wallis Islands and Futuna'),
('West Papua'),
('Western Armenia'),
('Western Australia'),
('Western Isles'),
('Western Sahara'),
('Yemen'),
('Yemen DPR'),
('Ynys Môn'),
('Yorkshire'),
('Yoruba Nation'),
('Yugoslavia'),
('Zambia'),
('Zanzibar'),
('Zimbabwe'),
('Åland Islands');

SET SQL_SAFE_UPDATES = 0;

UPDATE teams
SET iso_code = CASE 
    WHEN team = 'DR Congo' THEN 180
    WHEN team = 'Curaçao' THEN 531
    WHEN team = 'China PR' THEN 156
    WHEN team = 'Czechoslovakia' THEN 200
    WHEN team = 'Republic of Ireland' THEN 372
    WHEN team = 'Yugoslavia' THEN 891
    WHEN team = 'Northern Ireland' THEN 826
    WHEN team = 'Wales' THEN 826
    WHEN team = 'Scotland' THEN 826
    WHEN team = 'England' THEN 826
    WHEN team = 'Saarland' THEN 276
    WHEN team = 'German DR' THEN 278
    WHEN team = 'Vietnam Republic' THEN 704
    WHEN team = 'Macau' THEN 446
    WHEN team = 'Yemen DPR' THEN 887
    WHEN team = 'Northern Cyprus' THEN 196
    WHEN team = 'Wallis Islands and Futuna' THEN 876
    WHEN team = 'Åland Islands' THEN 248
    WHEN team = 'Ynys Môn' THEN 826
    WHEN team = 'Tahiti' THEN 258
    WHEN team = 'Czech Republic' THEN 203
    WHEN team = 'North Macedonia' THEN 807
    WHEN team = 'São Tomé and Príncipe' THEN 678
    WHEN team = 'Palestine' THEN 275
    WHEN team = 'Vatican City' THEN 336
    WHEN team = 'Réunion' THEN 638
    WHEN team = 'Malaya' THEN 458
    WHEN team = 'Alderney' THEN 831
    WHEN team = 'Catalonia' THEN 724
    WHEN team = 'Basque Country' THEN 724
    WHEN team = 'Brittany' THEN 250
    WHEN team = 'Galicia' THEN 724
    WHEN team = 'Andalusia' THEN 724
    WHEN team = 'Central Spain' THEN 724
    WHEN team = 'Manchukuo' THEN 156
    WHEN team = 'Zanzibar' THEN 834
    WHEN team = 'Burma' THEN 104
    WHEN team = 'North Vietnam' THEN 704
    WHEN team = 'Western Australia' THEN 36
    WHEN team = 'Corsica' THEN 250
    WHEN team = 'Gotland' THEN 752
    WHEN team = 'Saare County' THEN 233
    WHEN team = 'Orkney' THEN 826
    WHEN team = 'Sark' THEN 831
    WHEN team = 'Shetland' THEN 826
    WHEN team = 'Isle of Wight' THEN 826
    WHEN team = 'Canary Islands' THEN 724
    WHEN team = 'Frøya' THEN 578
    WHEN team = 'Hitra' THEN 578
    WHEN team = 'Rhodes' THEN 300
    WHEN team = 'Occitania' THEN 250
    WHEN team = 'Western Isles' THEN 826
    WHEN team = 'Menorca' THEN 756
    WHEN team = 'Ticino' THEN 756
    WHEN team = 'Asturias' THEN 724
    WHEN team = 'Maule Sur' THEN 152
    WHEN team = 'Surrey' THEN 826
    WHEN team = 'Kernow' THEN 826
    WHEN team = 'Chechnya' THEN 643
    WHEN team = 'Găgăuzia' THEN 498
    WHEN team = 'Tibet' THEN 156
    WHEN team = 'Provence' THEN 250
    WHEN team = 'Padania' THEN 380
    WHEN team = 'Iraqi Kurdistan' THEN 368
    WHEN team = 'Gozo' THEN 470
    WHEN team = 'Bonaire' THEN 528
    WHEN team = 'Chagos Islands' THEN 86
    WHEN team = 'Darfur' THEN 729
    WHEN team = 'Saint Barthélemy' THEN 652
    WHEN team = 'Abkhazia' THEN 268
    WHEN team = 'Artsakh' THEN 31
    WHEN team = 'Madrid' THEN 724
    WHEN team = 'Ellan Vannin' THEN 833
    WHEN team = 'Somaliland' THEN 706
    WHEN team = 'Franconia' THEN 276
    WHEN team = 'South Ossetia' THEN 268
    WHEN team = 'County of Nice' THEN 250
    WHEN team = 'Székely Land' THEN 642
    WHEN team = 'Panjab' THEN 356
    WHEN team = 'Felvidék' THEN 703
    WHEN team = 'Luhansk PR' THEN 804
    WHEN team = 'Donetsk PR' THEN 804
    WHEN team = 'Western Armenia' THEN 792
    WHEN team = 'Délvidék' THEN 688
    WHEN team = 'Barawa' THEN 706
    WHEN team = 'Ryūkyū' THEN 392
    WHEN team = 'Kárpátalja' THEN 804
    WHEN team = 'Yorkshire' THEN 826
    WHEN team = 'Matabeleland' THEN 716
    WHEN team = 'Kabylia' THEN 12
    WHEN team = 'Parishes of Jersey' THEN 832
    WHEN team = 'Biafra' THEN 566
    WHEN team = 'Elba Island' THEN 380
    WHEN team = 'West Papua' THEN 360
    WHEN team = 'South Yemen' THEN 887
    WHEN team = 'Ambazonia' THEN 120
    WHEN team = 'Crimea' THEN 804
    WHEN team = 'Cilento' THEN 380
	WHEN team = 'British Guiana' THEN 328
	WHEN team = 'Bohemia' THEN 203
	WHEN team = 'Manchuria' THEN 156
	WHEN team = 'Southern Rhodesia' THEN 716
	WHEN team = 'Northern Rhodesia' THEN 894
	WHEN team = 'Tanganyika' THEN 834
	WHEN team = 'French Somaliland' THEN 262
	WHEN team = 'Belgian Congo' THEN 180
	WHEN team = 'Gold Coast' THEN 288
	WHEN team = 'Ceylon' THEN 144
	WHEN team = 'Portuguese Guinea' THEN 624
	WHEN team = 'New Hebrides' THEN 548
	WHEN team = 'Nyasaland' THEN 454
	WHEN team = 'United Arab Republic' THEN 818
	WHEN team = 'Dahomey' THEN 204
	WHEN team = 'Congo-Kinshasa' THEN 180
	WHEN team = 'Upper Volta' THEN 854
	WHEN team = 'Zaïre' THEN 180
	WHEN team = 'Western Samoa' THEN 882
	WHEN team = 'Yemen AR' THEN 887
    ELSE NULL 
END;

UPDATE teams t
INNER JOIN countries c
    ON t.team = c.display_name OR t.team = c.official_name
SET t.iso_code = c.iso_code
WHERE t.iso_code IS NULL;

    
SET SQL_SAFE_UPDATES = 1;

----------------------------------- Update former_names
SET SQL_SAFE_UPDATES = 0;

UPDATE former_names fn
INNER JOIN teams t
    ON fn.current = t.team
SET fn.id_teams = t.id_teams;

SET SQL_SAFE_UPDATES = 1;

----------------------------------- Update results
SET SQL_SAFE_UPDATES = 0;

-- Update fk_home_team with the corresponding id_teams from the teams table
UPDATE results r
INNER JOIN teams t ON r.home_team = t.team
SET r.fk_home_teams = t.id_teams;

-- Update fk_away_team with the corresponding id_teams from the teams table
UPDATE results r
INNER JOIN teams t ON r.away_team = t.team
SET r.fk_away_teams = t.id_teams;

UPDATE results r
INNER JOIN former_names fn ON r.home_team = fn.current
AND r.date BETWEEN fn.start_date AND fn.end_date
SET r.fk_home_fr = fn.id_former_names;

UPDATE results r
INNER JOIN former_names fn ON r.away_team = fn.current
AND r.date BETWEEN fn.start_date AND fn.end_date
SET r.fk_away_fr = fn.id_former_names;

SET SQL_SAFE_UPDATES = 1;

----------------------------------- Update shootouts
SET SQL_SAFE_UPDATES = 0;

UPDATE shootouts so
INNER JOIN results r
ON so.date = r.date
AND so.home_team = r.home_team
AND so.away_team = r.away_team
AND (r.home_score - r.away_score) = 0
SET so.id_results = r.id_results;

SET SQL_SAFE_UPDATES = 1;

----------------------------------- Update goalscorers
SET SQL_SAFE_UPDATES = 0;

UPDATE goalscorers gs
INNER JOIN results r
ON gs.date = r.date
AND gs.home_team = r.home_team
AND gs.away_team = r.away_team
SET gs.id_results = r.id_results;

SET SQL_SAFE_UPDATES = 1;

----------------------------------- Create column winner in results and find winner
ALTER TABLE results
ADD COLUMN winner VARCHAR(255);
CREATE INDEX idx_winner ON results(winner);

SET SQL_SAFE_UPDATES = 0;

-- Update the winner column
UPDATE results r
LEFT JOIN shootouts s 
    ON r.date = s.date 
    AND r.home_team = s.home_team 
    AND r.away_team = s.away_team
SET r.winner = CASE
    WHEN r.home_score > r.away_score THEN r.home_team
    WHEN r.home_score < r.away_score THEN r.away_team
    ELSE s.winner
END;

SET SQL_SAFE_UPDATES = 1;
