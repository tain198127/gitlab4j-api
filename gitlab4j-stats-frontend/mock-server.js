import express from 'express';
import cors from 'cors';
const app = express();
const PORT = 3001;

app.use(cors());
app.use(express.json());

// Mock data
const mockSummaryStats = {
  totalDevelopers: 15,
  totalCommits: 342,
  totalLinesAdded: 12543,
  totalLinesDeleted: 8765,
  totalLinesChanged: 21308,
  totalProjects: 3
};

const mockDeveloperStats = [
  {
    developerName: 'John Doe',
    developerEmail: 'john.doe@example.com',
    totalCommits: 45,
    totalLinesAdded: 2341,
    totalLinesDeleted: 1234,
    totalLinesChanged: 3575,
    firstCommitDate: '2024-01-15T10:30:00',
    lastCommitDate: '2024-12-20T15:45:00'
  },
  {
    developerName: 'Jane Smith',
    developerEmail: 'jane.smith@example.com',
    totalCommits: 38,
    totalLinesAdded: 1876,
    totalLinesDeleted: 987,
    totalLinesChanged: 2863,
    firstCommitDate: '2024-02-01T09:15:00',
    lastCommitDate: '2024-12-19T14:20:00'
  },
  {
    developerName: 'Bob Johnson',
    developerEmail: 'bob.johnson@example.com',
    totalCommits: 52,
    totalLinesAdded: 2890,
    totalLinesDeleted: 1567,
    totalLinesChanged: 4457,
    firstCommitDate: '2024-01-10T08:45:00',
    lastCommitDate: '2024-12-21T16:30:00'
  }
];

const mockCommitTrends = [
  {
    date: '2024-01-01',
    commitCount: 5,
    linesAdded: 234,
    linesDeleted: 123,
    linesChanged: 357
  },
  {
    date: '2024-01-02',
    commitCount: 8,
    linesAdded: 456,
    linesDeleted: 234,
    linesChanged: 690
  },
  {
    date: '2024-01-03',
    commitCount: 3,
    linesAdded: 123,
    linesDeleted: 67,
    linesChanged: 190
  },
  {
    date: '2024-01-04',
    commitCount: 12,
    linesAdded: 789,
    linesDeleted: 345,
    linesChanged: 1134
  },
  {
    date: '2024-01-05',
    commitCount: 7,
    linesAdded: 345,
    linesDeleted: 189,
    linesChanged: 534
  }
];

// API endpoints
app.get('/api/statistics/summary', (req, res) => {
  res.json(mockSummaryStats);
});

app.get('/api/statistics/developers', (req, res) => {
  res.json(mockDeveloperStats);
});

app.get('/api/statistics/commit-trends', (req, res) => {
  res.json(mockCommitTrends);
});

app.post('/api/statistics/collect', (req, res) => {
  res.json({ message: 'Data collection started', status: 'success' });
});

app.get('/api/statistics/projects', (req, res) => {
  res.json([
    {
      projectId: 1,
      projectName: 'Main Project',
      totalCommits: 342,
      totalLinesChanged: 21308,
      totalLinesAdded: 12543,
      totalLinesDeleted: 8765,
      totalDevelopers: 15,
      firstCommitDate: '2024-01-01T08:00:00',
      lastCommitDate: '2024-12-21T18:00:00'
    }
  ]);
});

app.get('/api/statistics/daily', (req, res) => {
  res.json(mockCommitTrends);
});

app.get('/api/statistics/activity', (req, res) => {
  res.json([
    {
      date: '2024-01-01',
      commitCount: 5,
      linesAdded: 234,
      linesDeleted: 123,
      linesChanged: 357
    }
  ]);
});

const server = app.listen(PORT, () => {
  console.log(`Mock server running on http://localhost:${PORT}`);
  console.log('Available endpoints:');
  console.log('  GET /api/statistics/summary');
  console.log('  GET /api/statistics/developers');
  console.log('  GET /api/statistics/commit-trends');
  console.log('  POST /api/statistics/collect');
  console.log('  GET /api/statistics/projects');
  console.log('  GET /api/statistics/daily');
  console.log('  GET /api/statistics/activity');
});

// Keep the process alive
process.on('SIGINT', () => {
  console.log('Shutting down mock server...');
  server.close(() => {
    process.exit(0);
  });
});

// Prevent the process from exiting
process.on('uncaughtException', (err) => {
  console.error('Uncaught exception:', err);
});

process.on('unhandledRejection', (reason, promise) => {
  console.error('Unhandled rejection at:', promise, 'reason:', reason);
});