import os

# Define paths
ROOT_BUILD_FILE = "build.gradle"
APP_DIR = "app"
APP_EXCLUDE_DIR = "app/build"
LIBRARY_DIR = "canteen_managment_library"
LIBRARY_EXCLUDE_DIR = "canteen_managment_library/build"
SUMMARY_FILE = "admin_summary.txt"

def should_include_file(file_path):
    # Exclude files in the app/build and canteen_managment_library/build directories
    return APP_EXCLUDE_DIR not in file_path and LIBRARY_EXCLUDE_DIR not in file_path

def get_files_to_include():
    # Collect files from build.gradle, app directory, and canteen_managment_library directory
    files_to_include = []

    # Add the root build.gradle file
    if os.path.isfile(ROOT_BUILD_FILE):
        files_to_include.append(ROOT_BUILD_FILE)

    # Add all files in the app directory except app/build
    for root, _, files in os.walk(APP_DIR):
        for file in files:
            file_path = os.path.join(root, file)
            if should_include_file(file_path):
                files_to_include.append(file_path)

    # Add all files in the canteen_managment_library directory except canteen_managment_library/build
    for root, _, files in os.walk(LIBRARY_DIR):
        for file in files:
            file_path = os.path.join(root, file)
            if should_include_file(file_path):
                files_to_include.append(file_path)
    
    return files_to_include

def write_summary_file(files_to_include):
    # Write content to admin_summary.txt
    with open(SUMMARY_FILE, 'w') as summary:
        for file_path in files_to_include:
            summary.write(f"File: {file_path}\n")
            summary.write("-" * 40 + "\n")
            try:
                with open(file_path, 'r') as file_content:
                    summary.write(file_content.read())
            except Exception as e:
                summary.write(f"Error reading file: {e}\n")
            summary.write("\n" + "=" * 40 + "\n\n")

def main():
    files_to_include = get_files_to_include()
    write_summary_file(files_to_include)

if __name__ == "__main__":
    main()
