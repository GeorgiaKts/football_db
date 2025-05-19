import pandas as pd
import os

# Updated list of countries with ISO_Code
iso_numeric_codes = {
    180: "DR Congo",
    531: "Curaçao",
    156: "China PR",
    200: "Czechoslovakia",
    372: "Republic of Ireland",
    891: "Yugoslavia",
    826: "Northern Ireland",
    826: "Wales",
    826: "Scotland",
    826: "England",
    276: "Saarland",
    278: "German DR",
    704: "Vietnam Republic",
    446: "Macau",
    887: "Yemen DPR",
    196: "Northern Cyprus",
    876: "Wallis Islands and Futuna",
    248: "Åland Islands",
    826: "Ynys Môn",
    258: "Tahiti",
    203: "Czech Republic",
    807: "North Macedonia",
    678: "São Tomé and Príncipe",
    275: "Palestine",
    336: "Vatican City",
    638: "Réunion",
    458: "Malaya",
    831: "Alderney",
    724: "Catalonia",
    724: "Basque Country",
    250: "Brittany",
    724: "Galicia",
    724: "Andalusia",
    724: "Central Spain",
    156: "Manchukuo",
    834: "Zanzibar",
    104: "Burma",
    704: "North Vietnam",
    36: "Western Australia",
    250: "Corsica",
    752: "Gotland",
    233: "Saare County",
    826: "Orkney",
    831: "Sark",
    826: "Shetland",
    826: "Isle of Wight",
    724: "Canary Islands",
    578: "Frøya",
    578: "Hitra",
    300: "Rhodes",
    250: "Occitania",
    826: "Western Isles",
    756: "Menorca",
    756: "Ticino",
    724: "Asturias",
    152: "Maule Sur",
    826: "Surrey",
    826: "Kernow",
    643: "Chechnya",
    498: "Găgăuzia",
    156: "Tibet",
    250: "Provence",
    380: "Padania",
    368: "Iraqi Kurdistan",
    470: "Gozo",
    528: "Bonaire",
    86: "Chagos Islands",
    729: "Darfur",
    652: "Saint Barthélemy",
    268: "Abkhazia",
    31: "Artsakh",
    724: "Madrid",
    833: "Ellan Vannin",
    706: "Somaliland",
    276: "Franconia",
    268: "South Ossetia",
    250: "County of Nice",
    642: "Székely Land",
    356: "Panjab",
    703: "Felvidék",
    804: "Luhansk PR",
    804: "Donetsk PR",
    792: "Western Armenia",
    688: "Délvidék",
    706: "Barawa",
    392: "Ryūkyū",
    804: "Kárpátalja",
    826: "Yorkshire",
    716: "Matabeleland",
    12: "Kabylia",
    832: "Parishes of Jersey",
    566: "Biafra",
    380: "Elba Island",
    360: "West Papua",
    887: "South Yemen",
    120: "Ambazonia",
    804: "Crimea",
    380: "Cilento",
    328: "British Guiana",
    203: "Bohemia",
    156: "Manchuria",
    716: "Southern Rhodesia",
    894: "Northern Rhodesia",
    834: "Tanganyika",
    262: "French Somaliland",
    180: "Belgian Congo",
    288: "Gold Coast",
    144: "Ceylon",
    624: "Portuguese Guinea",
    548: "New Hebrides",
    454: "Nyasaland",
    818: "United Arab Republic",
    204: "Dahomey",
    180: "Congo-Kinshasa",
    854: "Upper Volta",
    180: "Zaïre",
    882: "Western Samoa",
    887: "Yemen AR"
}

# Additional regions/groups of people without specific ISO codes
no_iso_code_entities = ["Silesia",
                        "Sápmi",
                        "Romani people",
                        "Arameans Suryoye",
                        "Sealand",
                        "Raetia",
                        "Tamil Eelam",
                        "Saugeais",
                        "Seborga",
                        "United Koreans in Japan",
                        "Cascadia",
                        "Chameria",
                        "Yoruba Nation",
                        "Mapuche",
                        "Aymara",
                        "Hmong",
                        "Two Sicilies",
                        "Republic of St. Pauli"]

# File name for countries CSV
countries_file = "cleaned_countries.csv"

# List to keep track of countries without ISO Numeric Codes
countries_without_code = []

# Process the countries file if it exists
if os.path.exists(countries_file):
    # Read the CSV file (ensure handling of NULL values)
    df = pd.read_csv(countries_file, sep="\t", dtype=str, keep_default_na=False, na_values=[""])

    # Convert empty values and NaN to "NULL"
    df = df.fillna("NULL").replace("", "NULL")

    # Trim whitespace from all string columns
    df = df.apply(lambda x: x.strip() if isinstance(x, str) else x)

    # Preserve "NA" in the "ISO" column and replace "NA" with "NULL" in other columns
    df["ISO"] = df["ISO"].replace("NA", "NA")

    # Replace "NA" with "NULL" in all columns except "ISO"
    for column in df.columns:
        if column != "ISO":
            df[column] = df[column].replace("NA", "NULL")

    # Check if the ISO code exists in the 'ISO_Code' column
    for numeric_code, country_name in iso_numeric_codes.items():
        # Check if the ISO_Code already exists in the dataframe
        if str(numeric_code) not in df["ISO_Code"].values:
            # If not, add the country to the list of countries without code
            countries_without_code.append(country_name)

            # If not, create a new row to append to the dataframe
            new_row = pd.DataFrame([{
                "ISO": "NULL",
                "ISO3": "NULL",
                "ISO_Code": str(numeric_code),
                "FIPS": "NULL",
                "Display_Name": country_name,
                "Official_Name": "NULL",
                "Capital": "NULL",
                "Continent": "NULL",
                "CurrencyCode": "NULL",
                "CurrencyName": "NULL",
                "Phone": "NULL",
                "Region Code": "NULL",
                "Region Name": "NULL",
                "Sub-region Code": "NULL",
                "Sub-region Name": "NULL",
                "Intermediate Region Code": "NULL",
                "Intermediate Region Name": "NULL",
                "Status": "Ceased to exist" if country_name in ["German DR", "Czechoslovakia"] else "NULL",
                "Developed or Developing": "NULL",
                "Small Island Developing States (SIDS)": "NULL",
                "Land Locked Developing Countries (LLDC)": "NULL",
                "Least Developed Countries (LDC)": "NULL",
                "Area_SqKm": "NULL",
                "Population": "NULL"
            }])

            # Add the new row to the dataframe
            df = pd.concat([df, new_row], ignore_index=True)

    # Save the cleaned file back to the original file
    df.to_csv(countries_file, sep="\t", index=False)

    print(f"\nProcessed and saved: {countries_file}")

    # Create a DataFrame for the additional entities
    extra_entities = pd.DataFrame([{
        "ISO": "NULL",
        "ISO3": "NULL",
        "ISO_Code": "NULL",
        "FIPS": "NULL",
        "Display_Name": entity,
        "Official_Name": "NULL",
        "Capital": "NULL",
        "Continent": "NULL",
        "CurrencyCode": "NULL",
        "CurrencyName": "NULL",
        "Phone": "NULL",
        "Region Code": "NULL",
        "Region Name": "NULL",
        "Sub-region Code": "NULL",
        "Sub-region Name": "NULL",
        "Intermediate Region Code": "NULL",
        "Intermediate Region Name": "NULL",
        "Status": "Region/Group",
        "Developed or Developing": "NULL",
        "Small Island Developing States (SIDS)": "NULL",
        "Land Locked Developing Countries (LLDC)": "NULL",
        "Least Developed Countries (LDC)": "NULL",
        "Area_SqKm": "NULL",
        "Population": "NULL"
    } for entity in no_iso_code_entities])

    # Append the new entities to the original DataFrame
    df = pd.concat([df, extra_entities], ignore_index=True)

    # Save the updated dataframe
    df.to_csv(countries_file, sep="\t", index=False)

    # Print countries without an ISO code
    if countries_without_code:
        print("\nCountries without ISO Numeric Codes matches:")
        for country in countries_without_code:
            print(country)
    else:
        print("\nAll countries have an ISO Numeric Code.")

    print("\nEntities that are not countries and have no ISO Numeric Code:")
    for entity in no_iso_code_entities:
        print(entity)

else:
    print(f"File not found: {countries_file}")
