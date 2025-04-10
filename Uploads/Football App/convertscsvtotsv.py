import os
import csv
import chardet

# Φάκελος που περιέχει τα CSV αρχεία
folder = 'C:\\Users\\jojo_\\Desktop\\test'

# Εύρεση όλων των .csv αρχείων στον φάκελο
csv_files = [f for f in os.listdir(folder) if f.endswith('.csv')]

def detect_encoding(filepath):
    with open(filepath, 'rb') as f:
        result = chardet.detect(f.read())
    return result['encoding']

for filename in csv_files:
    input_path = os.path.join(folder, filename)
    output_path = os.path.join(folder, filename)  # αποθηκεύουμε με το ίδιο όνομα

    encoding = detect_encoding(input_path)

    with open(input_path, 'r', encoding=encoding, newline='') as infile:
        reader = csv.reader(infile)
        rows = list(reader)

    with open(output_path, 'w', encoding='utf-8', newline='') as outfile:
        writer = csv.writer(outfile, delimiter='\t')  # tab ως διαχωριστικό
        writer.writerows(rows)

    print(f"✅ Μετατράπηκε (και κρατήθηκε ίδιο): {filename}")

print("\n🎉 Όλα τα αρχεία μετατράπηκαν σε TSV με tabs και διατήρησαν τα αρχικά τους ονόματα!")