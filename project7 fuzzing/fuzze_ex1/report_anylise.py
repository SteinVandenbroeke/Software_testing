import json

test_folder = "generated_2025-07-08 16:31:49.454489"
dict_all_errors = {}
with open(f"{test_folder}/report.json", "r") as f:
    a = json.load(f)

    for file_id in a["1_"]["test_ids"]:
        with open(f"{test_folder}/test_{file_id}/output.txt", "r") as newfile:
            dict_all_errors[newfile.readlines()[2]] = file_id
print(dict_all_errors)

for key, value in dict_all_errors.items():
    print(key, value)



