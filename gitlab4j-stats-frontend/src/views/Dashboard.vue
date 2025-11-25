<template>
  <div class="dashboard">
    <el-row :gutter="20">
      <el-col :span="24">
        <div class="page-header">
          <h1>GitLab Statistics Dashboard</h1>
          <div class="header-actions">
            <el-button 
              type="primary" 
              @click="collectData"
              :loading="statsStore.loading"
              :icon="Refresh"
            >
              Collect Data
            </el-button>
            <el-button 
              type="success" 
              @click="refreshData"
              :icon="Search"
            >
              Refresh
            </el-button>
          </div>
        </div>
      </el-col>
    </el-row>

    <SummaryCards :stats="statsStore.summaryStats" />
    <DeveloperStats />
    <CommitTrends />
  </div>
</template>

<script setup lang="ts">
import { onMounted } from 'vue'
import { useStatsStore } from '@/stores/stats'
import SummaryCards from '@/components/SummaryCards.vue'
import DeveloperStats from '@/components/DeveloperStats.vue'
import CommitTrends from '@/components/CommitTrends.vue'
import { Refresh, Search } from '@element-plus/icons-vue'

const statsStore = useStatsStore()

const collectData = async () => {
  await statsStore.collectData()
}

const refreshData = () => {
  statsStore.fetchSummaryStats()
}

onMounted(() => {
  statsStore.fetchSummaryStats()
})
</script>

<style scoped>
.dashboard {
  padding: 20px;
  background-color: #f5f7fa;
  min-height: 100vh;
}

.page-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 30px;
  padding: 20px;
  background: white;
  border-radius: 8px;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
}

.page-header h1 {
  margin: 0;
  color: #303133;
  font-size: 28px;
  font-weight: 600;
}

.header-actions {
  display: flex;
  gap: 12px;
}
</style>