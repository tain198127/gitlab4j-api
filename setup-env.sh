#!/bin/bash

# GitLab Stats Environment Setup Script
# This script sets up environment variables for secure GitLab configuration

echo "Setting up GitLab Stats environment variables..."

# Set your GitLab configuration here
export GITLAB_API_URL="https://gitlab.com/api/v4"
export GITLAB_API_TOKEN="your-gitlab-token-here"
export GITLAB_PROJECT_ID="your-project-id-here"

echo "Environment variables set:"
echo "GITLAB_API_URL: $GITLAB_API_URL"
echo "GITLAB_API_TOKEN: [HIDDEN]"
echo "GITLAB_PROJECT_ID: $GITLAB_PROJECT_ID"

echo ""
echo "To make these variables permanent, add them to your ~/.bashrc or ~/.zshrc file:"
echo "echo 'export GITLAB_API_TOKEN=\"your-token-here\"' >> ~/.zshrc"
echo "echo 'export GITLAB_PROJECT_ID=\"your-project-id\"' >> ~/.zshrc"
echo ""
echo "To run the application with these variables:"
echo "1. Source this script: source setup-env.sh"
echo "2. Start the backend: ./gradlew :gitlab4j-stats-demo:bootRun"
echo "3. Start the frontend: cd gitlab4j-stats-frontend && npm run dev"