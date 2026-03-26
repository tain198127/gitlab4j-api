<template>
  <div class="multi-project-stats">
    <div class="section-header">
      <h3>多项目统计</h3>
      <el-button @click="fetchData" :loading="loading" type="primary" size="small">
        刷新数据
      </el-button>
    </div>

    <div v-if="stats && !loading" class="stats-content">
      <el-row :gutter="20" class="overview-cards">
        <el-col :span="6">
          <el-card class="stat-card">
            <div class="stat-icon">
              <el-icon size="32" color="#409EFF"><Folder /></el-icon>
            </div>
            <div class="stat-info">
              <div class="stat-number">{{ stats.totalProjects }}</div>
              <div class="stat-label">项目总数</div>
            </div>
          </el-card>
        </el-col>
        <el-col :span="6">
          <el-card class="stat-card">
            <div class="stat-icon">
              <el-icon size="32" color="#67C23A"><User /></el-icon>
            </div>
            <div class="stat-info">
              <div class="stat-number">{{ stats.totalDevelopers }}</div>
              <div class="stat-label">开发者总数</div>
            </div>
          </el-card>
        </el-col>
        <el-col :span="6">
          <el-card class="stat-card">
            <div class="stat-icon">
              <el-icon size="32" color="#E6A23C"><Document /></el-icon>
            </div>
            <div class="stat-info">
              <div class="stat-number">{{ stats.totalCommits }}</div>
              <div class="stat-label">总提交数</div>
            </div>
          </el-card>
        </el-col>
        <el-col :span="6">
          <el-card class="stat-card">
            <div class="stat-icon">
              <el-icon size="32" color="#F56C6C"><Edit /></el-icon>
            </div>
            <div class="stat-info">
              <div class="stat-number">{{ formatNumber(stats.totalLinesChanged) }}</div>
              <div class="stat-label">总行数变更</div>
            </div>
          </el-card>
        </el-col>
      </el-row>

      <el-card class="lines-changed-card">
        <template #header>
          <div class="card-header">
            <span>代码行数统计</span>
          </div>
        </template>
        <el-row :gutter="20">
          <el-col :span="8">
            <div class="lines-stat">
              <div class="lines-label">新增行数</div>
              <div class="lines-number add">+{{ formatNumber(stats.totalLinesAdded) }}</div>
            </div>
          </el-col>
          <el-col :span="8">
            <div class="lines-stat">
              <div class="lines-label">删除行数</div>
              <div class="lines-number delete">-{{ formatNumber(stats.totalLinesDeleted) }}</div>
            </div>
          </el-col>
          <el-col :span="8">
            <div class="lines-stat">
              <div class="lines-label">修改行数</div>
              <div class="lines-number change">{{ formatNumber(stats.totalLinesChanged) }}</div>
            </div>
          </el-col>
        </el-row>
      </el-card>

      <el-card class="project-distribution-card">
        <template #header>
          <div class="card-header">
            <span>项目开发者分布</span>
          </div>
        </template>
        <div class="distribution-chart">
          <div 
            v-for="(developerCount, projectName) in stats.projectDeveloperDistribution" 
            :key="projectName"
            class="distribution-item"
          >
            <div class="project-info">
              <div class="project-name">{{ projectName }}</div>
              <div class="developer-count">{{ developerCount }} 位开发者</div>
            </div>
            <div class="distribution-bar">
              <div 
                class="distribution-fill" 
                :style="{ width: getDistributionWidth(developerCount) + '%' }"
              ></div>
            </div>
          </div>
        </div>
      </el-card>

      <el-card class="developer-details-card">
        <template #header>
          <div class="card-header">
            <span>项目开发者详情</span>
            <el-tag type="info">共 {{ stats.projectDeveloperStats.length }} 条记录</el-tag>
          </div>
        </template>
        
        <el-table :data="stats.projectDeveloperStats" style="width: 100%" max-height="500">
          <el-table-column prop="projectName" label="项目名称" min-width="200" />
          <el-table-column prop="developerName" label="开发者姓名" width="150" />
          <el-table-column prop="developerEmail" label="邮箱" min-width="200" />
          <el-table-column prop="commitsCount" label="提交数" width="100" sortable />
          <el-table-column prop="linesAdded" label="新增行数" width="120" sortable />
          <el-table-column prop="linesDeleted" label="删除行数" width="120" sortable />
          <el-table-column prop="linesChanged" label="修改行数" width="120" sortable />
          <el-table-column prop="commitFrequency" label="提交频率" width="120" sortable>
            <template #default="{ row }">
              <el-tag 
                :type="getFrequencyType(row.commitFrequency)"
                size="small"
              >
                {{ row.commitFrequency.toFixed(2) }} /天
              </el-tag>
            </template>
          </el-table-column>
          <el-table-column prop="firstCommitDate" label="首次提交" width="120" />
          <el-table-column prop="lastCommitDate" label="最后提交" width="120" />
        </el-table>
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
import { Folder, User, Document, Edit } from '@element-plus/icons-vue'
import { statisticsApi } from '@/utils/api'
import type { MultiProjectStatsDTO } from '@/types'

const stats = ref<MultiProjectStatsDTO | null>(null)
const loading = ref(false)
const error = ref<string | null>(null)

const fetchData = async () => {
  loading.value = true
  error.value = null
  
  try {
    const response = await statisticsApi.getMultiProjectStats()
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

const formatNumber = (num: number) => {
  if (num >= 1000000) {
    return (num / 1000000).toFixed(1) + 'M'
  } else if (num >= 1000) {
    return (num / 1000).toFixed(1) + 'K'
  }
  return num.toString()
}

const getDistributionWidth = (count: number) => {
  if (!stats.value) return 0
  const maxCount = Math.max(...Object.values(stats.value.projectDeveloperDistribution))
  return maxCount > 0 ? (count / maxCount) * 100 : 0
}

const getFrequencyType = (frequency: number) => {
  if (frequency >= 2) return 'success'
  if (frequency >= 1) return 'warning'
  return 'info'
}

onMounted(() => {
  fetchData()
})
</script>

<style scoped>
.multi-project-stats {
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

.stats-content {
  display: flex;
  flex-direction: column;
  gap: 20px;
}

.overview-cards {
  margin-bottom: 20px;
}

.stat-card {
  text-align: center;
  padding: 20px;
  transition: transform 0.3s ease;
}

.stat-card:hover {
  transform: translateY(-2px);
}

.stat-icon {
  margin-bottom: 10px;
}

.stat-info {
  display: flex;
  flex-direction: column;
  align-items: center;
}

.stat-number {
  font-size: 28px;
  font-weight: 700;
  color: #303133;
  margin-bottom: 5px;
}

.stat-label {
  font-size: 14px;
  color: #909399;
}

.lines-changed-card {
  margin-bottom: 20px;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  font-weight: 600;
}

.lines-stat {
  text-align: center;
  padding: 15px;
  border-radius: 6px;
  background: #f8f9fa;
}

.lines-label {
  font-size: 14px;
  color: #909399;
  margin-bottom: 8px;
}

.lines-number {
  font-size: 24px;
  font-weight: 600;
}

.lines-number.add {
  color: #67C23A;
}

.lines-number.delete {
  color: #F56C6C;
}

.lines-number.change {
  color: #409EFF;
}

.project-distribution-card {
  margin-bottom: 20px;
}

.distribution-chart {
  padding: 20px 0;
}

.distribution-item {
  display: flex;
  align-items: center;
  margin-bottom: 15px;
  padding: 15px;
  background: #f8f9fa;
  border-radius: 6px;
  transition: background-color 0.3s ease;
}

.distribution-item:hover {
  background: #e9ecef;
}

.project-info {
  width: 250px;
}

.project-name {
  font-weight: 500;
  color: #303133;
  margin-bottom: 5px;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.developer-count {
  font-size: 12px;
  color: #909399;
}

.distribution-bar {
  flex: 1;
  height: 8px;
  background: #e4e7ed;
  border-radius: 4px;
  margin: 0 15px;
  position: relative;
  overflow: hidden;
}

.distribution-fill {
  height: 100%;
  background: linear-gradient(90deg, #409EFF, #67C23A);
  border-radius: 4px;
  transition: width 0.3s ease;
}

.developer-details-card {
  margin-bottom: 20px;
}

.error-message {
  margin-top: 20px;
}

@media (max-width: 1200px) {
  .project-info {
    width: 200px;
  }
}

@media (max-width: 768px) {
  .overview-cards .el-col {
    margin-bottom: 15px;
  }
  
  .project-info {
    width: 150px;
  }
  
  .distribution-bar {
    margin: 0 10px;
  }
  
  .stat-number {
    font-size: 24px;
  }
}
</style>