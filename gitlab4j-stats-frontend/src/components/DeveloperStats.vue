<template>
  <div class="developer-stats">
    <el-card>
      <template #header>
        <div class="card-header">
          <span>Top Developers</span>
          <el-button type="primary" size="small" @click="fetchData">
            <el-icon><Refresh /></el-icon>
            Refresh
          </el-button>
        </div>
      </template>
      
      <el-table
        v-loading="loading"
        :data="developerStats"
        style="width: 100%"
        :default-sort="{ prop: 'totalLinesChanged', order: 'descending' }"
      >
        <el-table-column prop="developerName" label="Developer Name" min-width="150" />
        <el-table-column prop="developerEmail" label="Email" min-width="200" />
        <el-table-column prop="totalCommits" label="Commits" sortable align="center" width="100" />
        <el-table-column prop="totalLinesAdded" label="Lines Added" sortable align="center" width="120" />
        <el-table-column prop="totalLinesDeleted" label="Lines Deleted" sortable align="center" width="120" />
        <el-table-column prop="totalLinesChanged" label="Lines Changed" sortable align="center" width="120" />
        <el-table-column label="First Commit" align="center" width="150">
          <template #default="scope">
            {{ formatDate(scope.row.firstCommitDate) }}
          </template>
        </el-table-column>
        <el-table-column label="Last Commit" align="center" width="150">
          <template #default="scope">
            {{ formatDate(scope.row.lastCommitDate) }}
          </template>
        </el-table-column>
      </el-table>
    </el-card>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import type { DeveloperStats } from '@/types'
import { statisticsApi } from '@/utils/api'
import { Refresh } from '@element-plus/icons-vue'

const developerStats = ref<DeveloperStats[]>([])
const loading = ref(false)

const fetchData = async () => {
  loading.value = true
  try {
    const response = await statisticsApi.getDeveloperStats(20, 'totalLinesChanged')
    developerStats.value = response.data
  } catch (error) {
    console.error('Error fetching developer stats:', error)
  } finally {
    loading.value = false
  }
}

const formatDate = (dateString: string): string => {
  return new Date(dateString).toLocaleDateString()
}

onMounted(() => {
  fetchData()
})
</script>

<style scoped>
.developer-stats {
  margin-bottom: 20px;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}
</style>