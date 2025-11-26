<template>
  <div class="commit-trends">
    <el-card>
      <template #header>
        <div class="card-header">
          <span>Commit Trends</span>
          <el-date-picker
            v-model="dateRange"
            type="daterange"
            range-separator="To"
            start-placeholder="Start date"
            end-placeholder="End date"
            size="small"
            @change="fetchData"
          />
        </div>
      </template>
      
      <div ref="chartRef" class="chart-container" v-loading="loading"></div>
    </el-card>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted, watch } from 'vue'
import * as echarts from 'echarts'
import type { CommitTrend } from '@/types'
import { statisticsApi } from '@/utils/api'

const chartRef = ref<HTMLElement>()
const dateRange = ref<[Date, Date] | null>(null)
const loading = ref(false)
let chart: echarts.ECharts | null = null

const initChart = () => {
  if (!chartRef.value) return
  
  chart = echarts.init(chartRef.value)
  
  const option: echarts.EChartsOption = {
    title: {
      text: 'Commit Activity Trends',
      left: 'center'
    },
    tooltip: {
      trigger: 'axis',
      axisPointer: {
        type: 'cross'
      }
    },
    legend: {
      data: ['Commits', 'Lines Added', 'Lines Deleted', 'Lines Changed'],
      bottom: 10
    },
    grid: {
      left: '3%',
      right: '4%',
      bottom: '15%',
      containLabel: true
    },
    xAxis: {
      type: 'category',
      data: [],
      axisLabel: {
        rotate: 45
      }
    },
    yAxis: [
      {
        type: 'value',
        name: 'Count',
        position: 'left'
      },
      {
        type: 'value',
        name: 'Lines',
        position: 'right'
      }
    ],
    series: [
      {
        name: 'Commits',
        type: 'line',
        data: [],
        smooth: true,
        itemStyle: { color: '#409EFF' }
      },
      {
        name: 'Lines Added',
        type: 'bar',
        yAxisIndex: 1,
        data: [],
        itemStyle: { color: '#67C23A' }
      },
      {
        name: 'Lines Deleted',
        type: 'bar',
        yAxisIndex: 1,
        data: [],
        itemStyle: { color: '#F56C6C' }
      },
      {
        name: 'Lines Changed',
        type: 'line',
        yAxisIndex: 1,
        data: [],
        smooth: true,
        itemStyle: { color: '#E6A23C' }
      }
    ]
  }
  
  chart.setOption(option)
}

const fetchData = async () => {
  loading.value = true
  try {
    const startDate = dateRange.value?.[0]?.toISOString().split('T')[0]
    const endDate = dateRange.value?.[1]?.toISOString().split('T')[0]
    
    const response = await statisticsApi.getCommitTrends(startDate, endDate)
    const trends = response.data
    
    if (chart) {
      const dates = trends.map(t => t.date)
      const commits = trends.map(t => t.commitCount)
      const linesAdded = trends.map(t => t.linesAdded)
      const linesDeleted = trends.map(t => t.linesDeleted)
      const linesChanged = trends.map(t => t.linesChanged)
      
      chart.setOption({
        xAxis: {
          data: dates
        },
        series: [
          { data: commits },
          { data: linesAdded },
          { data: linesDeleted },
          { data: linesChanged }
        ]
      })
    }
  } catch (error) {
    console.error('Error fetching commit trends:', error)
  } finally {
    loading.value = false
  }
}

const handleResize = () => {
  chart?.resize()
}

onMounted(() => {
  initChart()
  fetchData()
  window.addEventListener('resize', handleResize)
})

watch(chartRef, () => {
  if (chartRef.value && !chart) {
    initChart()
  }
})
</script>

<style scoped>
.commit-trends {
  margin-bottom: 20px;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.chart-container {
  width: 100%;
  height: 400px;
}
</style>