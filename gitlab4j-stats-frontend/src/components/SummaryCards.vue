<template>
  <div class="summary-cards">
    <el-row :gutter="20">
      <el-col :span="4">
        <el-card class="summary-card">
          <div class="card-content">
            <div class="card-icon developers">
              <el-icon><User /></el-icon>
            </div>
            <div class="card-info">
              <div class="card-value">{{ stats?.totalDevelopers || 0 }}</div>
              <div class="card-label">Developers</div>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="4">
        <el-card class="summary-card">
          <div class="card-content">
            <div class="card-icon commits">
              <el-icon><Document /></el-icon>
            </div>
            <div class="card-info">
              <div class="card-value">{{ stats?.totalCommits || 0 }}</div>
              <div class="card-label">Commits</div>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="4">
        <el-card class="summary-card">
          <div class="card-content">
            <div class="card-icon lines-added">
              <el-icon><Plus /></el-icon>
            </div>
            <div class="card-info">
              <div class="card-value">{{ formatNumber(stats?.totalLinesAdded) || 0 }}</div>
              <div class="card-label">Lines Added</div>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="4">
        <el-card class="summary-card">
          <div class="card-content">
            <div class="card-icon lines-deleted">
              <el-icon><Minus /></el-icon>
            </div>
            <div class="card-info">
              <div class="card-value">{{ formatNumber(stats?.totalLinesDeleted) || 0 }}</div>
              <div class="card-label">Lines Deleted</div>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="4">
        <el-card class="summary-card">
          <div class="card-content">
            <div class="card-icon lines-changed">
              <el-icon><Edit /></el-icon>
            </div>
            <div class="card-info">
              <div class="card-value">{{ formatNumber(stats?.totalLinesChanged) || 0 }}</div>
              <div class="card-label">Lines Changed</div>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="4">
        <el-card class="summary-card">
          <div class="card-content">
            <div class="card-icon projects">
              <el-icon><Folder /></el-icon>
            </div>
            <div class="card-info">
              <div class="card-value">{{ stats?.totalProjects || 0 }}</div>
              <div class="card-label">Projects</div>
            </div>
          </div>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script setup lang="ts">
import { computed } from 'vue'
import type { SummaryStats } from '@/types'
import { User, Document, Plus, Minus, Edit, Folder } from '@element-plus/icons-vue'

interface Props {
  stats: SummaryStats | null
}

const props = defineProps<Props>()

const stats = computed(() => props.stats)

const formatNumber = (num: number | undefined): string => {
  if (!num) return '0'
  return num.toLocaleString()
}
</script>

<style scoped>
.summary-cards {
  margin-bottom: 20px;
}

.summary-card {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  color: white;
  border: none;
}

.card-content {
  display: flex;
  align-items: center;
  justify-content: space-between;
}

.card-icon {
  font-size: 24px;
  padding: 12px;
  border-radius: 50%;
  background: rgba(255, 255, 255, 0.2);
}

.card-icon.developers { background: rgba(102, 126, 234, 0.3); }
.card-icon.commits { background: rgba(118, 75, 162, 0.3); }
.card-icon.lines-added { background: rgba(76, 175, 80, 0.3); }
.card-icon.lines-deleted { background: rgba(244, 67, 54, 0.3); }
.card-icon.lines-changed { background: rgba(255, 152, 0, 0.3); }
.card-icon.projects { background: rgba(156, 39, 176, 0.3); }

.card-info {
  text-align: right;
}

.card-value {
  font-size: 24px;
  font-weight: bold;
  margin-bottom: 4px;
}

.card-label {
  font-size: 14px;
  opacity: 0.9;
}
</style>