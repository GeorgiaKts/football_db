import pandas as pd
import os
from unidecode import unidecode

# List of CSV filenames with their respective columns for processing
file_columns = {
    "countries.csv": ["ISO"],
    "former_names.csv": ["current", "former", "start_date"],
    "goalscorers.csv": ["date", "home_team", "away_team", "team", "scorer", "minute", "own_goal", "penalty"],
    "results.csv": ["date", "home_team", "away_team", "home_score", "away_score", "tournament", "city", "country", "neutral"],
    "shootouts.csv": ["date", "home_team", "away_team"]
}

# Process each file
for file, columns in file_columns.items():
    if os.path.exists(file):
        # Read CSV file (keep "NA" as a string, avoid automatic NaN conversion)
        df = pd.read_csv(file, sep="\t", dtype=str, keep_default_na=False, na_values=[""])

        # Convert empty values and NaN to "NULL"
        df = df.fillna("NULL").replace("", "NULL")

        # Trim whitespace from all string columns
        df = df.apply(lambda x: x.strip() if isinstance(x, str) else x)

        # Convert "NA" to "NULL" in all columns except for 'minute' in 'goalscorers.csv'
        if file != "goalscorers.csv":
            df = df.replace("NA", "NULL")

        # Handle the 'minute' column in 'goalscorers.csv' (replace 'NA' with 'NULL' only, leaving valid integers intact)
        if file == "goalscorers.csv" and "minute" in df.columns:
            # Replace 'NA' with 'NULL' for all columns except 'minute'
            df = df.apply(lambda x: x.map(lambda y: "NULL" if y == "NA" else y) if x.dtype == "O" else x)

            # Handle the 'minute' column specifically
            df["minute"] = df["minute"].apply(lambda x: "NULL" if x == "NA" else x)

        # Special case: Keep "NA" in the "ISO" column of countries.csv
        if file == "countries.csv" and "ISO" in df.columns:
            df["ISO"] = df["ISO"].replace("NULL", "NA")  # Restore "NA" only in "ISO"

        # Special case for countries.csv: Replace '#N/A' with NULL in all columns
        if file == "countries.csv":
            df = df.replace("#N/A", "NULL")

        # Handle Date Format Consistency (parse date fields as datetime and format them as 'YYYY-MM-DD')
        date_columns = ["date", "start_date", "end_date"]
        for date_col in date_columns:
            if date_col in df.columns:
                # Convert to datetime, any invalid dates will become NaT (Not a Time)
                df[date_col] = pd.to_datetime(df[date_col], errors='coerce').dt.strftime('%Y-%m-%d')
                # Replace NaT with NULL if any invalid date is found
                df[date_col] = df[date_col].fillna("NULL")

        # Apply transliteration to all string columns to convert to English characters
        for col in df.select_dtypes(include=['object']).columns:
            df[col] = df[col].apply(lambda x: unidecode(x) if isinstance(x, str) else x)

        # Save cleaned file with tab separator
        cleaned_file = f"cleaned_{file}"
        df.to_csv(cleaned_file, sep="\t", index=False)

        print(f"\nProcessed and saved: {cleaned_file}\n")
    else:
        print(f"File not found: {file}")
