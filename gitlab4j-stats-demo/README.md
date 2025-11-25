# GitLab Statistics Dashboard

A comprehensive statistics dashboard for GitLab projects that provides insights into developer activity, commit trends, and project metrics. Built with Spring Boot backend and Vue3 frontend.

## Features

### Backend (Spring Boot)
- **Data Collection**: Automatically fetches commit data from GitLab API
- **Statistics Calculation**: Processes commit diffs to calculate code line statistics
- **RESTful API**: Provides comprehensive endpoints for statistics data
- **H2 Database**: In-memory database for development and testing
- **Comprehensive Testing**: Unit tests for services and integration tests for controllers

### Frontend (Vue3 + TypeScript)
- **Modern UI**: Built with Vue3, TypeScript, and Element Plus
- **Interactive Dashboard**: Real-time statistics visualization
- **Developer Rankings**: Top developers by lines changed, commits, etc.
- **Commit Trends**: Time-series charts showing commit activity
- **Summary Cards**: Quick overview of key metrics
- **Responsive Design**: Works on desktop and mobile devices

## Project Structure

```
gitlab4j-api/
├── gitlab4j-stats-demo/          # Spring Boot backend
│   ├── src/main/java/com/gitlab4j/stats/
│   │   ├── config/              # Configuration classes (CORS, etc.)
│   │   ├── controller/          # REST API controllers
│   │   ├── dto/                 # Data Transfer Objects
│   │   ├── model/               # JPA entities
│   │   ├── repository/          # JPA repositories
│   │   └── service/             # Business logic services
│   └── src/test/                # Unit and integration tests
└── gitlab4j-stats-frontend/     # Vue3 frontend
    ├── src/
    │   ├── components/          # Vue components
    │   ├── stores/              # Pinia state management
    │   ├── types/               # TypeScript type definitions
    │   ├── utils/               # API utilities
    │   └── views/               # Page components
    └── package.json             # Dependencies and scripts
```

## Quick Start

### Prerequisites
- Java 11 or higher
- Node.js 16 or higher
- GitLab account with API access

### Backend Setup

1. **Configure GitLab API Access**
   ```bash
   cd gitlab4j-stats-demo
   
   # Create application.properties in src/main/resources/
   echo "gitlab.host.url=https://gitlab.com" > src/main/resources/application.properties
   echo "gitlab.private.token=YOUR_GITLAB_PRIVATE_TOKEN" >> src/main/resources/application.properties
   echo "gitlab.project.id=YOUR_PROJECT_ID" >> src/main/resources/application.properties
   ```

2. **Build and Run Backend**
   ```bash
   cd gitlab4j-stats-demo
   ./gradlew bootRun
   ```
   
   The backend will start on http://localhost:8080

### Frontend Setup

1. **Install Dependencies**
   ```bash
   cd gitlab4j-stats-frontend
   npm install
   ```

2. **Start Development Server**
   ```bash
   npm run dev
   ```
   
   The frontend will be available at http://localhost:3000

## API Endpoints

The backend provides the following REST API endpoints:

### Statistics Endpoints
- `GET /api/stats/developers` - Get developer statistics
- `GET /api/stats/developers/{email}` - Get specific developer stats
- `GET /api/stats/daily` - Get daily statistics
- `GET /api/stats/projects` - Get project statistics
- `GET /api/stats/summary` - Get summary statistics
- `GET /api/stats/activity` - Get developer activity data
- `GET /api/stats/trends` - Get commit trends over time

### Data Collection
- `POST /api/stats/collect` - Trigger data collection from GitLab

## Frontend Components

### Dashboard Components
- **SummaryCards**: Displays key metrics in card format
- **DeveloperStats**: Table showing top developers with sorting
- **CommitTrends**: Interactive charts showing commit activity over time

### State Management
- **Stats Store**: Manages application state using Pinia
- **API Utilities**: Centralized HTTP client with TypeScript support

## Configuration

### Backend Configuration
Create `src/main/resources/application.properties`:
```properties
# GitLab Configuration
gitlab.host.url=https://gitlab.com
gitlab.private.token=your-private-token
gitlab.project.id=your-project-id

# Database Configuration (H2)
spring.datasource.url=jdbc:h2:mem:testdb
spring.datasource.driverClassName=org.h2.Driver
spring.jpa.hibernate.ddl-auto=update

# Logging
logging.level.com.gitlab4j.stats=DEBUG
```

### Frontend Configuration
The frontend is configured via `vite.config.ts` with proxy settings for API calls:
```typescript
proxy: {
  '/api': {
    target: 'http://localhost:8080',
    changeOrigin: true,
    rewrite: (path) => path
  }
}
```

## Development

### Running Tests

**Backend Tests:**
```bash
cd gitlab4j-stats-demo
./gradlew test
```

**Frontend Tests:**
```bash
cd gitlab4j-stats-frontend
npm run test
```

### Building for Production

**Backend:**
```bash
cd gitlab4j-stats-demo
./gradlew build
```

**Frontend:**
```bash
cd gitlab4j-stats-frontend
npm run build
```

## Data Flow

1. **Data Collection**: Backend fetches commit data from GitLab API
2. **Processing**: Commit diffs are parsed to extract line statistics
3. **Storage**: Statistics are stored in H2 database
4. **API**: Frontend fetches data via REST API
5. **Visualization**: Data is displayed in interactive charts and tables

## Technologies Used

### Backend
- Spring Boot 3.x
- Spring Data JPA
- GitLab4J API Client
- H2 Database
- Lombok
- JUnit 5 & Mockito

### Frontend
- Vue 3 with Composition API
- TypeScript
- Element Plus UI Library
- ECharts for data visualization
- Pinia for state management
- Axios for HTTP requests
- Vite for build tooling

## Contributing

1. Fork the repository
2. Create a feature branch
3. Make your changes
4. Add tests for new functionality
5. Submit a pull request

## License

This project is licensed under the MIT License - see the LICENSE file for details.