import argparse
import datetime
import os
import subprocess
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
parser.add_argument("--runtime", help="Max runtime for the fuzzer")
parser.add_argument("--programtime", help="Max runtime a individual fuzz test")
# Parse arguments
args = parser.parse_args()

print(f"Iteration count {args.max}")

max_iterations = 10
program_timeout = 10
max_runtime = 10
max_sequence_lenght = 1000
max_chars_in_file = 1000

if args.max is not None:
    max_iterations = int(args.max)

if args.runtime is not None:
    max_runtime = int(args.runtime)

def generate_random_input_sequence():
    length_sequence = randint(0, max_sequence_lenght)
    sequence = ''.join(chr(randint(1, 255)) for _ in range(length_sequence))
    return sequence

def generate_random_map():
    length_sequence = randint(0, max_chars_in_file)
    sequence = ''.join(chr(randint(0, 255)) for _ in range(length_sequence))
    return sequence

report = [None] * max_iterations
print("Normal run", os.system(f'java -jar jpacman-3.0.1.jar {"valid_input/sample.map"} {"SUUWDE"}'))

def run_fuzzer(sequence_generator, map_generator):
    start_time = datetime.datetime.now()
    for i in range(max_iterations):
        os.mkdir(f"{test_folder}/test_{i}")
        with open(f"{test_folder}/test_{i}/input_map.txt", "a") as f:
            f.write(map_generator())
        map_path = f"{test_folder}/test_{i}/input_map.txt"
        random_sequence = sequence_generator()

        with open(f"{test_folder}/test_{i}/input_sequence.txt", "a") as f:
            f.write(random_sequence)

        try:
            result = subprocess.run(
                ['java', '-jar', 'jpacman-3.0.1.jar', map_path, random_sequence],
                timeout=program_timeout
            )
            print("Return code:", result.returncode)
            runcode = result.returncode
        except subprocess.TimeoutExpired:
            print("Process took too long and was killed.")
            runcode = -1

        if datetime.datetime.now() - start_time > datetime.timedelta(seconds=max_runtime):
            print(f"Exit fuzzing loop, becaues max run time {max_runtime} exceeded")
            break

        report[i] = runcode

    print(report)

run_fuzzer(generate_random_input_sequence, generate_random_input_sequence)
