----------------------------------- Load file's data countries	
USE football_db;

LOAD DATA INFILE 'C:\Program Files\MySQL\MySQL Server 8.0\Uploads\cleaned_countries.csv'
INTO TABLE countries
FIELDS TERMINATED BY '\t'
ENCLOSED BY '"'
LINES TERMINATED BY '\r\n'
IGNORE 1 LINES;