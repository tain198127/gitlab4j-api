import { defineStore } from 'pinia'
import { ref } from 'vue'
import axios from 'axios'
import type { SummaryStats } from '@/types'
import { statisticsApi } from '@/utils/api'

export const useStatsStore = defineStore('stats', () => {
  const summaryStats = ref<SummaryStats | null>(null)
  const loading = ref(false)
  const error = ref<string | null>(null)

  const fetchSummaryStats = async () => {
    loading.value = true
    error.value = null
    try {
      console.log('Fetching summary stats from API...')
      const response = await statisticsApi.getSummaryStats()
      console.log('Summary stats response:', response.data)
      summaryStats.value = response.data
    } catch (err) {
      error.value = 'Failed to fetch summary statistics'
      console.error('Error fetching summary stats:', err)
      if (axios.isAxiosError(err)) {
        console.error('Axios error details:', {
          message: err.message,
          status: err.response?.status,
          statusText: err.response?.statusText,
          data: err.response?.data,
          url: err.config?.url
        })
      }
    } finally {
      loading.value = false
    }
  }

  const collectData = async () => {
    loading.value = true
    error.value = null
    try {
      await statisticsApi.collectData()
      await fetchSummaryStats() // Refresh data after collection
    } catch (err) {
      error.value = 'Failed to collect data'
      console.error('Error collecting data:', err)
    } finally {
      loading.value = false
    }
  }

  return {
    summaryStats,
    loading,
    error,
    fetchSummaryStats,
    collectData
  }
})