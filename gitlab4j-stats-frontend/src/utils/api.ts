import axios from 'axios'
import type { 
  DeveloperStats, 
  DailyStats, 
  ProjectStats, 
  SummaryStats, 
  DeveloperActivity, 
  CommitTrend 
} from '@/types'

const API_BASE_URL = '/api'

const api = axios.create({
  baseURL: API_BASE_URL,
  timeout: 10000,
  headers: {
    'Content-Type': 'application/json'
  }
})

export const statisticsApi = {
  // Developer stats
  getDeveloperStats(limit: number = 10, sortBy: string = 'linesChanged') {
    return api.get<DeveloperStats[]>('/stats/developers', {
      params: { limit, sortBy }
    })
  },

  getDeveloperStatsByEmail(email: string) {
    return api.get<DeveloperStats>(`/stats/developers/${email}`)
  },

  // Daily stats
  getDailyStats(startDate?: string, endDate?: string, developerEmail?: string) {
    return api.get<DailyStats[]>('/stats/daily', {
      params: { startDate, endDate, developerEmail }
    })
  },

  // Project stats
  getProjectStats() {
    return api.get<ProjectStats[]>('/stats/projects')
  },

  getProjectStatsById(projectId: number) {
    return api.get<ProjectStats>(`/stats/projects/${projectId}`)
  },

  // Summary stats
  getSummaryStats() {
    return api.get<SummaryStats>('/stats/summary')
  },

  // Developer activity
  getDeveloperActivity(startDate?: string, endDate?: string) {
    return api.get<DeveloperActivity[]>('/stats/activity', {
      params: { startDate, endDate }
    })
  },

  // Commit trends
  getCommitTrends(startDate?: string, endDate?: string) {
    return api.get<CommitTrend[]>('/stats/trends', {
      params: { startDate, endDate }
    })
  },

  // Data collection
  collectData() {
    return api.post('/stats/collect')
  },

  collectProjectData(projectId: number) {
    return api.post(`/stats/collect/project/${projectId}`)
  }
}