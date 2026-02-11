#!/bin/bash

# Pre-commit checks script
# Runs appropriate linters based on staged files

set -e

# Get the repository root
REPO_ROOT=$(git rev-parse --show-toplevel)
cd "$REPO_ROOT"

# Get list of staged files
STAGED_FILES=$(git diff --cached --name-only --diff-filter=ACM)

# Check if any files are staged
if [ -z "$STAGED_FILES" ]; then
    echo "ℹ️  No staged files to check"
    exit 0
fi

# Flags to track what needs to be checked
CHECK_BACKEND=false
CHECK_FRONTEND=false

# Determine which parts of the codebase were modified
while IFS= read -r file; do
    if [[ "$file" == backend/* ]]; then
        CHECK_BACKEND=true
    elif [[ "$file" == frontend/* ]]; then
        CHECK_FRONTEND=true
    fi
done <<< "$STAGED_FILES"

# Exit early if no backend or frontend files were modified
if [ "$CHECK_BACKEND" = false ] && [ "$CHECK_FRONTEND" = false ]; then
    echo "✅ No backend or frontend files modified, skipping lint checks"
    exit 0
fi

# Track overall success
OVERALL_SUCCESS=true

# Run backend checks if needed
if [ "$CHECK_BACKEND" = true ]; then
    echo "🔍 Backend files modified, running ktlintCheck..."
    cd "$REPO_ROOT/backend"

    if ./gradlew ktlintCheck; then
        echo "✅ Backend ktlintCheck passed!"
    else
        echo ""
        echo "❌ Backend ktlintCheck failed! Please fix the formatting issues."
        echo "💡 You can run 'cd backend && ./gradlew ktlintFormat' to auto-fix most issues."
        OVERALL_SUCCESS=false
    fi

    cd "$REPO_ROOT"
fi

# Run frontend checks if needed
if [ "$CHECK_FRONTEND" = true ]; then
    echo "🔍 Frontend files modified, running lint..."
    cd "$REPO_ROOT/frontend"

    if npm run lint; then
        echo "✅ Frontend lint passed!"
    else
        echo ""
        echo "❌ Frontend lint failed! Please fix the linting issues."
        echo "💡 You can run 'cd frontend && npm run lint:fix' to auto-fix some issues."
        OVERALL_SUCCESS=false
    fi

    cd "$REPO_ROOT"
fi

# Exit with appropriate code
if [ "$OVERALL_SUCCESS" = true ]; then
    echo ""
    echo "✅ All pre-commit checks passed!"
    exit 0
else
    echo ""
    echo "❌ Some pre-commit checks failed. Please fix the issues before committing."
    exit 1
fi
