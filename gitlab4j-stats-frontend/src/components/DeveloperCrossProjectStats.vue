<template>
  <div class="developer-cross-project-stats">
    <div class="section-header">
      <h3>开发者跨项目统计</h3>
      <el-button @click="fetchData" :loading="loading" type="primary" size="small">
        刷新数据
      </el-button>
    </div>
    
    <div class="search-section">
      <el-input
        v-model="developerEmail"
        placeholder="输入开发者邮箱"
        style="width: 300px; margin-right: 10px;"
        @keyup.enter="fetchData"
      >
        <template #prepend>
          <el-icon><User /></el-icon>
        </template>
      </el-input>
      <el-button @click="fetchData" type="primary" :disabled="!developerEmail">
        查询
      </el-button>
    </div>

    <div v-if="stats && !loading" class="stats-content">
      <el-card class="overview-card">
        <template #header>
          <div class="card-header">
            <span>{{ stats.developerName }} ({{ stats.developerEmail }})</span>
            <el-tag type="info">跨项目统计</el-tag>
          </div>
        </template>
        
        <el-row :gutter="20">
          <el-col :span="6">
            <div class="stat-item">
              <div class="stat-label">参与项目数</div>
              <div class="stat-value">{{ stats.totalProjects }}</div>
            </div>
          </el-col>
          <el-col :span="6">
            <div class="stat-item">
              <div class="stat-label">总提交数</div>
              <div class="stat-value">{{ stats.totalCommits }}</div>
            </div>
          </el-col>
          <el-col :span="6">
            <div class="stat-item">
              <div class="stat-label">提交频率评分</div>
              <div class="stat-value">
                <el-progress 
                  :percentage="Math.round(stats.commitFrequencyScore)" 
                  :color="getProgressColor(stats.commitFrequencyScore)"
                />
              </div>
            </div>
          </el-col>
          <el-col :span="6">
            <div class="stat-item">
              <div class="stat-label">平均每项目提交数</div>
              <div class="stat-value">{{ stats.averageCommitsPerProject.toFixed(1) }}</div>
            </div>
          </el-col>
        </el-row>
      </el-card>

      <el-card class="project-activity-card">
        <template #header>
          <div class="card-header">
            <span>项目活跃度排名</span>
            <el-tag type="success">最活跃: {{ stats.mostActiveProject }}</el-tag>
            <el-tag type="warning">最不活跃: {{ stats.leastActiveProject }}</el-tag>
          </div>
        </template>
        
        <el-table :data="stats.projectActivities" style="width: 100%" max-height="400">
          <el-table-column prop="projectName" label="项目名称" min-width="200" />
          <el-table-column prop="commitsCount" label="提交数" width="100" sortable />
          <el-table-column prop="linesAdded" label="新增行数" width="120" sortable />
          <el-table-column prop="linesDeleted" label="删除行数" width="120" sortable />
          <el-table-column prop="linesChanged" label="修改行数" width="120" sortable />
          <el-table-column prop="firstCommitDate" label="首次提交" width="120" />
          <el-table-column prop="lastCommitDate" label="最后提交" width="120" />
          <el-table-column prop="activityScore" label="活跃度评分" width="140" sortable>
            <template #default="{ row }">
              <el-progress 
                :percentage="Math.round(row.activityScore)" 
                :color="getProgressColor(row.activityScore)"
              />
            </template>
          </el-table-column>
        </el-table>
      </el-card>

      <el-card class="commit-distribution-card">
        <template #header>
          <div class="card-header">
            <span>项目提交分布</span>
          </div>
        </template>
        
        <div class="distribution-chart">
          <div 
            v-for="(count, project) in stats.projectCommitCounts" 
            :key="project"
            class="distribution-item"
          >
            <div class="project-name">{{ project }}</div>
            <div class="commit-bar">
              <div 
                class="commit-fill" 
                :style="{ width: getCommitBarWidth(count) + '%' }"
              ></div>
            </div>
            <div class="commit-count">{{ count }}</div>
          </div>
        </div>
      </el-card>
    </div>

    <div v-if="error" class="error-message">
      <el-alert :title="error" type="error" show-icon @close="error = null" />
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import { User } from '@element-plus/icons-vue'
import { statisticsApi } from '@/utils/api'
import type { DeveloperCrossProjectStatsDTO } from '@/types'

const developerEmail = ref('')
const stats = ref<DeveloperCrossProjectStatsDTO | null>(null)
const loading = ref(false)
const error = ref<string | null>(null)

const fetchData = async () => {
  if (!developerEmail.value) {
    ElMessage.warning('请输入开发者邮箱')
    return
  }

  loading.value = true
  error.value = null
  
  try {
    const response = await statisticsApi.getDeveloperCrossProjectStats(developerEmail.value)
    stats.value = response.data
    ElMessage.success('数据加载成功')
  } catch (err: any) {
    error.value = err.response?.data?.message || '获取数据失败'
    ElMessage.error(error.value)
    stats.value = null
  } finally {
    loading.value = false
  }
}

const getProgressColor = (score: number) => {
  if (score >= 80) return '#67C23A'
  if (score >= 60) return '#E6A23C'
  if (score >= 40) return '#409EFF'
  return '#F56C6C'
}

const getCommitBarWidth = (count: number) => {
  if (!stats.value) return 0
  const maxCount = Math.max(...Object.values(stats.value.projectCommitCounts))
  return maxCount > 0 ? (count / maxCount) * 100 : 0
}

onMounted(() => {
  // 可以从路由参数或本地存储获取默认邮箱
  const savedEmail = localStorage.getItem('lastDeveloperEmail')
  if (savedEmail) {
    developerEmail.value = savedEmail
    fetchData()
  }
})
</script>

<style scoped>
.developer-cross-project-stats {
  padding: 20px;
  background: #fff;
  border-radius: 8px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.1);
}

.section-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
  padding-bottom: 15px;
  border-bottom: 1px solid #ebeef5;
}

.section-header h3 {
  margin: 0;
  color: #303133;
  font-size: 18px;
  font-weight: 600;
}

.search-section {
  margin-bottom: 20px;
  display: flex;
  align-items: center;
}

.stats-content {
  display: flex;
  flex-direction: column;
  gap: 20px;
}

.overview-card {
  margin-bottom: 20px;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  font-weight: 600;
}

.stat-item {
  text-align: center;
  padding: 15px;
  background: #f8f9fa;
  border-radius: 6px;
}

.stat-label {
  font-size: 14px;
  color: #909399;
  margin-bottom: 8px;
}

.stat-value {
  font-size: 24px;
  font-weight: 600;
  color: #303133;
}

.project-activity-card {
  margin-bottom: 20px;
}

.commit-distribution-card {
  margin-bottom: 20px;
}

.distribution-chart {
  padding: 20px 0;
}

.distribution-item {
  display: flex;
  align-items: center;
  margin-bottom: 15px;
  padding: 10px;
  background: #f8f9fa;
  border-radius: 4px;
}

.project-name {
  width: 200px;
  font-weight: 500;
  color: #303133;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.commit-bar {
  flex: 1;
  height: 20px;
  background: #e4e7ed;
  border-radius: 10px;
  margin: 0 15px;
  position: relative;
  overflow: hidden;
}

.commit-fill {
  height: 100%;
  background: linear-gradient(90deg, #409EFF, #67C23A);
  border-radius: 10px;
  transition: width 0.3s ease;
}

.commit-count {
  width: 60px;
  text-align: right;
  font-weight: 600;
  color: #303133;
}

.error-message {
  margin-top: 20px;
}

@media (max-width: 768px) {
  .el-col {
    margin-bottom: 15px;
  }
  
  .project-name {
    width: 120px;
    font-size: 12px;
  }
  
  .commit-bar {
    margin: 0 8px;
  }
  
  .commit-count {
    width: 40px;
    font-size: 12px;
  }
}
</style>