import { defineStore } from 'pinia'
import { ref } from 'vue'
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
      const response = await statisticsApi.getSummaryStats()
      summaryStats.value = response.data
    } catch (err) {
      error.value = 'Failed to fetch summary statistics'
      console.error('Error fetching summary stats:', err)
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