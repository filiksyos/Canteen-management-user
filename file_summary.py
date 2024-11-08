name: Generate Summary

on:
  push:
    branches:
      - '**'

jobs:
  build:
    runs-on: ubuntu-latest

    steps:
    - name: Checkout code
      uses: actions/checkout@v3

    - name: Set up Python
      uses: actions/setup-python@v4
      with:
        python-version: '3.x'

    - name: Install dependencies
      run: python -m pip install --upgrade pip

    - name: Run file summary script
      run: python file_summary.py

    - name: Check for changes
      id: check_changes
      run: |
        if [[ -n "$(git status --porcelain)" ]]; then
          echo "changes_detected=true" >> $GITHUB_ENV
        else
          echo "changes_detected=false" >> $GITHUB_ENV
        fi

    - name: Commit and push changes
      if: env.changes_detected == 'true'
      run: |
        git config --global user.name "github-actions[bot]"
        git config --global user.email "github-actions[bot]@users.noreply.github.com"
        git add user_summary.txt
        git commit -m "Update user_summary.txt"
        git push
      env:
        GITHUB_TOKEN: ${{ secrets.GITHUB_TOKEN }}
