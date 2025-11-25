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
  date: string
  commitsCount: number
  linesAdded: number
  linesDeleted: number
  linesChanged: number
}