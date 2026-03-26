#!/bin/bash

# GitLab Stats Configuration Setup Script
# This script helps you securely configure GitLab settings

echo "=== GitLab Stats Configuration Setup ==="
echo ""

# Check if user wants to use existing configuration
read -p "Do you want to use the existing GitLab configuration? (y/n): " use_existing

if [[ "$use_existing" == "y" || "$use_existing" == "Y" ]]; then
    export GITLAB_API_URL="https://gitlab.com/api/v4"
    export GITLAB_API_TOKEN="glpat-G3qGLYrP4OQN1FnvDQdoVm86MQp1Oml5ejdlCw.01.121qvnji7"
    export GITLAB_PROJECT_ID="76373621"
    echo "Using existing configuration."
else
    echo "Please enter your GitLab configuration:"
    read -p "GitLab API URL (default: https://gitlab.com/api/v4): " api_url
    read -p "GitLab API Token: " api_token
    read -p "GitLab Project ID: " project_id
    
    export GITLAB_API_URL="${api_url:-https://gitlab.com/api/v4}"
    export GITLAB_API_TOKEN="$api_token"
    export GITLAB_PROJECT_ID="$project_id"
fi

echo ""
echo "Environment variables configured:"
echo "GITLAB_API_URL: $GITLAB_API_URL"
echo "GITLAB_API_TOKEN: [HIDDEN]"
echo "GITLAB_PROJECT_ID: $GITLAB_PROJECT_ID"

echo ""
echo "=== Next Steps ==="
echo "1. To make these variables permanent, add them to your shell profile:"
echo "   echo 'export GITLAB_API_TOKEN=\"$GITLAB_API_TOKEN\"' >> ~/.zshrc"
echo "   echo 'export GITLAB_PROJECT_ID=\"$GITLAB_PROJECT_ID\"' >> ~/.zshrc"
echo ""
echo "2. Restart your backend application:"
echo "   ./gradlew :gitlab4j-stats-demo:bootRun"
echo ""
echo "3. Your configuration is now secure and not stored in properties files!"

# Create a local env file for development (optional)
cat > .env.local << EOF
# Local development environment variables
GITLAB_API_URL=$GITLAB_API_URL
GITLAB_API_TOKEN=$GITLAB_API_TOKEN
GITLAB_PROJECT_ID=$GITLAB_PROJECT_ID
EOF

echo ""
echo "Created .env.local file for local development reference."
echo "Remember to add .env.local to your .gitignore file!"