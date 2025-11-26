export interface DeveloperStats {
  developerName: string
  developerEmail: string
  totalCommits: number
  totalLinesAdded: number
  totalLinesDeleted: number
  totalLinesChanged: number
  firstCommitDate: string
  lastCommitDate: string
}

export interface DailyStats {
  statDate: string
  developerName: string
  developerEmail: string
  commitsCount: number
  linesAdded: number
  linesDeleted: number
  linesChanged: number
}

export interface ProjectStats {
  projectId: number
  projectName: string
  totalCommits: number
  totalLinesAdded: number
  totalLinesDeleted: number
  totalLinesChanged: number
  developerCount: number
}

export interface SummaryStats {
  totalDevelopers: number
  totalCommits: number
  totalLinesAdded: number
  totalLinesDeleted: number
  totalLinesChanged: number
  totalProjects: number
}

export interface DeveloperActivity {
  developerName: string
  developerEmail: string
  commitsCount: number
  linesChanged: number
  avgLinesPerCommit: number
}

export interface CommitTrend {
  date: string;
  commitCount: number;
  linesAdded: number;
  linesDeleted: number;
  linesChanged: number;
}

export interface ProjectDeveloperStatsDTO {
  projectName: string;
  developerName: string;
  developerEmail: string;
  commitsCount: number;
  linesAdded: number;
  linesDeleted: number;
  linesChanged: number;
  commitFrequency: number;
  firstCommitDate: string;
  lastCommitDate: string;
}

export interface MultiProjectStatsDTO {
  totalProjects: number;
  totalDevelopers: number;
  totalCommits: number;
  totalLinesAdded: number;
  totalLinesDeleted: number;
  totalLinesChanged: number;
  projectDeveloperDistribution: Record<string, number>;
  projectDeveloperStats: ProjectDeveloperStatsDTO[];
}

export interface DeveloperProjectActivityDTO {
  projectName: string;
  commitsCount: number;
  linesAdded: number;
  linesDeleted: number;
  linesChanged: number;
  firstCommitDate: string;
  lastCommitDate: string;
  activityScore: number;
}

export interface DeveloperCrossProjectStatsDTO {
  developerName: string;
  developerEmail: string;
  totalProjects: number;
  totalCommits: number;
  totalLinesAdded: number;
  totalLinesDeleted: number;
  totalLinesChanged: number;
  averageCommitsPerProject: number;
  averageLinesPerProject: number;
  projectCommitCounts: Record<string, number>;
  projectActivities: DeveloperProjectActivityDTO[];
  mostActiveProject: string;
  leastActiveProject: string;
  commitFrequencyScore: number;
}