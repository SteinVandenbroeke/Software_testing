import argparse
import datetime
import os
from random import randrange, randint
import sys
# Create the parser

run = datetime.datetime.now()

test_folder = f"generated_{run}"
# run using python3 fuzzer.py --max 5
if not os.path.isdir(test_folder):
    os.mkdir(test_folder)

parser = argparse.ArgumentParser(description="Fuzzer tool")

# Add arguments
parser.add_argument("--max", help="Max amount if iterations")
# Parse arguments
args = parser.parse_args()

print(f"Iteration count {args.max}")

max_iterations = int(args.max)

def generate_random_input_sequence():
    length_sequence = randint(0, 1000)
    sequence = ''.join(chr(randint(1, 255)) for _ in range(length_sequence))
    return sequence

def generate_random_map():
    length_sequence = randint(0, 1000)
    sequence = ''.join(chr(randint(1, 255)) for _ in range(length_sequence))
    return sequence


report = [None] * max_iterations

for i in range(max_iterations):
    os.mkdir(f"{test_folder}/test_{i}")
    with open(f"{test_folder}/test_{i}/input_map.txt", "a") as f:
        f.write(generate_random_map())
    map_path = f"{test_folder}/test_{i}/input_map.txt"
    random_sequence = generate_random_input_sequence()
    runcode = os.system(f'java -jar jpacman-3.0.1 jar {map_path} {random_sequence}')
    report[i] = runcode

print(report)

