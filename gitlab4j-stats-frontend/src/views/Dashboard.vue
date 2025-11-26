<template>
  <div class="dashboard">
    <div class="page-header">
      <h1>GitLab 统计仪表板</h1>
      <div class="actions">
        <el-button @click="collectData" :loading="collecting" type="primary">
          收集数据
        </el-button>
        <el-button @click="refreshData" :loading="loading">
          刷新
        </el-button>
      </div>
    </div>

    <SummaryCards :loading="loading" />
    <DeveloperStats />
    <CommitTrends />
    <MultiProjectStats />
    <DeveloperCrossProjectStats />
  </div>
</template>

<script setup lang="ts">
import { ref } from 'vue'
import { ElMessage } from 'element-plus'
import SummaryCards from '@/components/SummaryCards.vue'
import DeveloperStats from '@/components/DeveloperStats.vue'
import CommitTrends from '@/components/CommitTrends.vue'
import MultiProjectStats from '@/components/MultiProjectStats.vue'
import DeveloperCrossProjectStats from '@/components/DeveloperCrossProjectStats.vue'
import { useStatsStore } from '@/stores/stats'

const statsStore = useStatsStore()
const loading = ref(false)
const collecting = ref(false)

const collectData = async () => {
  collecting.value = true
  try {
    await statsStore.collectData()
    ElMessage.success('数据收集完成')
  } catch (error) {
    ElMessage.error('数据收集失败')
  } finally {
    collecting.value = false
  }
}

const refreshData = async () => {
  loading.value = true
  try {
    await statsStore.fetchSummaryStats()
    ElMessage.success('数据刷新成功')
  } catch (error) {
    ElMessage.error('数据刷新失败')
  } finally {
    loading.value = false
  }
}
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