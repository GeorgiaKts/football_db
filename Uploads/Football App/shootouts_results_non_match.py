import pandas as pd

# Load the CSV files
shootouts_df = pd.read_csv('cleaned_shootouts.csv', sep='\t', dtype=str)  # Ensure everything is read as string (to preserve 'NULL')
results_df = pd.read_csv('cleaned_results.csv', sep='\t', dtype=str)  # Ensure everything is read as string (to preserve 'NULL')

# Extract the relevant columns
shootouts_sequence = shootouts_df[['date', 'home_team', 'away_team']]
results_sequence = results_df[['date', 'home_team', 'away_team']]

# Convert both sequences to a list of tuples for comparison
shootouts_list = [tuple(x) for x in shootouts_sequence.values]
results_list = [tuple(x) for x in results_sequence.values]

# Identify the rows in cleaned_shootouts.csv that are not in cleaned_results.csv
deleted_rows = []
valid_rows = []

for idx, entry in enumerate(shootouts_list):
    if entry in results_list:
        valid_rows.append(idx)
    else:
        deleted_rows.append(shootouts_df.iloc[idx])

# Print the deleted rows
print("Deleted rows:")
for row in deleted_rows:
    # Replace NaN with 'NULL' and print the row as a single line
    print(" ".join(row.fillna('NULL').astype(str).values))

# Filter out the deleted rows
shootouts_df_cleaned = shootouts_df.iloc[valid_rows]

# Save the cleaned dataframe back to the original file
shootouts_df_cleaned.to_csv('cleaned_shootouts.csv', sep='\t', index=False, na_rep='NULL')  # 'na_rep' ensures NULL values stay

# Print how many rows were deleted
print(f"\nDeleted {len(deleted_rows)} rows from cleaned_shootouts.csv.")
