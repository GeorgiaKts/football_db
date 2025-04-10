-- Create database
CREATE DATABASE IF NOT EXISTS football_db;
USE football_db;

-- Create tables
CREATE TABLE IF NOT EXISTS countries (
	id_countries INT AUTO_INCREMENT PRIMARY KEY,
    iso VARCHAR(2),             
    iso3 VARCHAR(3),
    iso_code INT UNIQUE,
    fips VARCHAR(100),
    display_name VARCHAR(255) UNIQUE,
    official_name VARCHAR(255) UNIQUE,
    capital VARCHAR(255),
    continent VARCHAR(255),
    currency_code VARCHAR(3),
    currency_name VARCHAR(255),
    phone_code VARCHAR(50),
    region_code FLOAT,
    region_name VARCHAR(255),
    subregion_code FLOAT,
    subregion_name VARCHAR(255),
    intermediate_region_code FLOAT,
    intermediate_region_name VARCHAR(255),
    status VARCHAR(255),
    development_status VARCHAR(255),
    sids VARCHAR(10),
    lldc VARCHAR(10),
    ldc VARCHAR(10),
    area_sqkm FLOAT,
    population INT,
	INDEX idx_country (display_name)
) ENGINE=InnoDB;


CREATE TABLE IF NOT EXISTS teams (
	id_teams INT AUTO_INCREMENT PRIMARY KEY,
	team VARCHAR(255),
	iso_code INT,
	id_countries INT,
	FOREIGN KEY (id_countries) REFERENCES countries(id_countries) ON DELETE SET NULL ON UPDATE CASCADE,
	INDEX idx_team (team)
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS former_names (
	id_former_names INT AUTO_INCREMENT PRIMARY KEY,
    current VARCHAR(255) NOT NULL,
    former VARCHAR(255) NOT NULL,
    start_date DATE NOT NULL,
    end_date DATE NOT NULL,
	id_teams INT,
	UNIQUE (current, former, start_date, end_date),
	FOREIGN KEY (id_teams) REFERENCES teams(id_teams) ON DELETE SET NULL ON UPDATE CASCADE
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS results (
	id_results INT AUTO_INCREMENT PRIMARY KEY,
    date DATE NOT NULL,
    home_team VARCHAR(255) NOT NULL,
    away_team VARCHAR(255) NOT NULL,
    home_score INT NOT NULL,
    away_score INT NOT NULL,
    tournament VARCHAR(255) NOT NULL,
    city VARCHAR(255) NOT NULL,
    country VARCHAR(255) NOT NULL,
    neutral VARCHAR(255) NOT NULL,
	winner VARCHAR(255),
	fk_home_teams INT,
	fk_away_teams INT,
	fk_home_fr INT,
	fk_away_fr INT,
    FOREIGN KEY (fk_home_teams) REFERENCES teams(id_teams) ON DELETE SET NULL ON UPDATE CASCADE,
	FOREIGN KEY (fk_away_teams) REFERENCES teams(id_teams) ON DELETE SET NULL ON UPDATE CASCADE,
	FOREIGN KEY (fk_home_fr) REFERENCES former_names(id_former_names) ON DELETE SET NULL ON UPDATE CASCADE,
	FOREIGN KEY (fk_away_fr) REFERENCES former_names(id_former_names) ON DELETE SET NULL ON UPDATE CASCADE,
	-- Creating indexes for better performance on JOIN operations
	INDEX idx_date_home_away (date, home_team, away_team),
    INDEX idx_home_team (home_team),
    INDEX idx_away_team (away_team)
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS shootouts (
    date DATE NOT NULL,
    home_team VARCHAR(255) NOT NULL,
    away_team VARCHAR(255) NOT NULL,
    winner VARCHAR(255) NOT NULL,
    first_shooter VARCHAR(255),
	id_results INT,
    PRIMARY KEY (date, home_team, away_team),
	FOREIGN KEY (id_results) REFERENCES results(id_results) ON DELETE SET NULL ON UPDATE CASCADE
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS goalscorers (
	id_goalscorers INT AUTO_INCREMENT PRIMARY KEY,
    date DATE NOT NULL,
    home_team VARCHAR(255) NOT NULL,
    away_team VARCHAR(255) NOT NULL,
    team VARCHAR(255) NOT NULL,
    scorer VARCHAR(255),   
    minute INT DEFAULT NULL, 
    own_goal VARCHAR(255),
    penalty VARCHAR(255),
	id_results INT,
	FOREIGN KEY (id_results) REFERENCES results(id_results) ON DELETE SET NULL ON UPDATE CASCADE,
	-- Creating indexes for better performance on JOIN operations
	INDEX idx_date_home_away (date, home_team, away_team),
	INDEX idx_scorer (scorer)
) ENGINE=InnoDB;




