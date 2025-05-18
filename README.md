# ⚽ Football Statistics Web App

A full-stack web application that provides interactive statistics for international men's football matches from 1872 to 2024.  
It includes ETL data processing, a MySQL database, and a Spring Boot web interface.

---

## 📌 Features

- 🏆 View global football statistics
- 🌍 Filter by country, year, and scorer
- 📅 Historical match data (results, goalscorers, shootouts)
- 🔄 ETL pipeline for cleaning and transforming raw data
- 🌐 Web frontend built with HTML, Bootstrap & Thymeleaf

---


---

## ⚙️ Technologies Used

- **Java 17**
- **Spring Boot 3.4.4**
- **MySQL 8.0**
- **Thymeleaf** & **Bootstrap**
- **Python 3.12.3 for ETL scripts
- **pandas, chardet, unidecode** (Python libs)

---

## 🔁 ETL Pipeline

Raw `.csv` files → cleaned `.tsv` → loaded into MySQL  
ETL steps include:

- Encoding fixes & whitespace trimming
- Converting missing values to `NULL`
- Deduplicating rows
- ISO code enrichment for countries

Run order:
1. `convertscsvtotsv.py`
2. `clean_data.py`
3. `update_countries.py`
4. `shootouts_results_non_match.py`

---

## 🧪 How to Run Locally

### 📌 Requirements:
- Python 3.x
- MySQL Server 8.0+
- Java 17
- Maven

### 🚀 Steps:
1. Clone the repo:
   ```bash
   git clone https://github.com/ikalpourtzi/football_db.git
   cd football_db

2. Run Spring Boot:
   mvn spring-boot:run
3.Visit the app at:
📍 http://localhost:8080

🧠 Authors
Georgia Katsarou

Ioanna Kalpourtzi

Thanasis Koukouliatas

📎 Notes
The project was developed as part of an academic assignment.
