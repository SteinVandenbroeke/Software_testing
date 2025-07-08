import argparse
import datetime
import json
import os
import random
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
max_runtime = 5 #60 * 5
max_sequence_lenght = 1000
max_chars_in_file = 1000

if args.max is not None:
    max_iterations = int(args.max)

if args.runtime is not None:
    max_runtime = int(args.runtime)

def summerize_report(report):
    count_errors = {}
    for item in report:
        if item is None:
            continue
        if f"{item['exit_code']}_{item['program_output']}" not in count_errors.keys():
            count_errors[f"{item['exit_code']}_{item['program_output']}"] = {"count": 0, "test_ids":  []}
        count_errors[f"{item['exit_code']}_{item['program_output']}"]["count"] += 1
        count_errors[f"{item['exit_code']}_{item['program_output']}"]["test_ids"].append(item["test_id"])
    return count_errors

def generate_random_input_sequence():
    length_sequence = randint(0, max_sequence_lenght)
    sequence = ''.join(chr(randint(1, 255)) for _ in range(length_sequence))
    return sequence

def generate_random_map():
    length_sequence = randint(0, max_chars_in_file)
    sequence = ''.join(chr(randint(0, 255)) for _ in range(length_sequence))
    return sequence


class Counter():
    def __init__(self):
        self.value = 0

    def increment(self):
        self.value += 1

    def get(self):
        return self.value

    def reset(self):
        self.value = 0

one_symbol_counter = Counter()
def generate_one_symbol():
    sequence = ''.join(chr(one_symbol_counter.get()) for _ in range(1))
    one_symbol_counter.increment()
    print("Testing char", sequence)
    return sequence

def genertate_empty_sequence():
    return ""

def ex5_generate_map_with_information():
    amount_of_lines = randint(1, 20)
    amount_of_characters = randint(1, 20)

    sequence = ""
    for i in range(amount_of_lines):
        for j in range(amount_of_characters):
            sequence += ''.join(random.choice(["W", "F", "0", "P", "M"]) for _ in range(amount_of_characters))
            sequence += "\n"
    return sequence

def ex5_generate_sequence_with_information():
    length_sequence = randint(0, 20)
    sequence = ''.join(random.choice(["E", "S", "U", "D", "Q", "W", "L", "R"]) for _ in range(length_sequence))
    return sequence


def fixed_working_map():
    with open('valid_input/sample.map', 'r') as file:
        content = file.read()
    return content


print("Normal run", os.system(f'java -jar jpacman-3.0.1.jar {"valid_input/sample.map"} {"SUUWDE"}'))

def run_fuzzer(sequence_generator, map_generator, max_iterations_run=max_iterations):
    report = [None] * max_iterations_run
    start_time = datetime.datetime.now()
    for i in range(max_iterations_run):
        os.mkdir(f"{test_folder}/test_{i}")
        with open(f"{test_folder}/test_{i}/input_map.txt", "a") as f:
            f.write(map_generator())
        map_path = f"{test_folder}/test_{i}/input_map.txt"
        sequence = sequence_generator()

        with open(f"{test_folder}/test_{i}/input_sequence.txt", "a") as f:
            f.write(sequence)

        try:
            result = subprocess.run(
                ['java', '-jar', 'jpacman-3.0.1.jar', map_path, sequence],
                timeout=program_timeout,
                capture_output=True,
                text=True  # This gives you strings instead of bytes
            )
            print("Return code:", result.returncode)
            runcode = result.returncode
            errorcode = result.stderr
            program_output = result.stdout
        except subprocess.TimeoutExpired:
            print("Process took too long and was killed.")
            runcode = -1
            errorcode = "Process took too long and was killed."
            program_output = "Process took too long and was killed."

        if datetime.datetime.now() - start_time > datetime.timedelta(seconds=max_runtime):
            print(f"Exit fuzzing loop, becaues max run time {max_runtime} exceeded")
            break

        report[i] = {"exit_code": runcode, "errorcode": errorcode, "program_output": program_output, "input_sequence": sequence, "test_id": i}

        with open(f"{test_folder}/test_{i}/output.txt", "a") as f:
            f.write(f"exit_code: {runcode}\n" +
                    f"input_sequence: {sequence}\n" +
                    f"errorcode: {errorcode}"
                    + f"program_output: {program_output}")

    print(report)
    print("-----------------")
    report_summerize = summerize_report(report)
    print(report_summerize)
    with open(f"{test_folder}/report.json", "a") as f:
        json.dump(report_summerize, f)

#run_fuzzer(generate_random_input_sequence, generate_random_map, max_iterations_run=max_iterations)
# run_fuzzer(genertate_empty_sequence, generate_one_symbol, 255)
one_symbol_counter.reset()
one_symbol_counter.increment()

#run_fuzzer(generate_one_symbol, fixed_working_map, 255)
run_fuzzer(ex5_generate_sequence_with_information, ex5_generate_map_with_information, 10000)

#
#
