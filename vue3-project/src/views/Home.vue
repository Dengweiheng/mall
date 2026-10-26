<template>
  <div class="home-container">
    <el-row :gutter="20">
      <el-col :span="8">
        <el-card shadow="hover" class="data-card">
          <template #header>
            <div class="card-header">
              <span>今日订单</span>
              <el-tag type="success">新增加</el-tag>
            </div>
          </template>
          <div class="card-body">
            <span class="num">{{ stats.todayOrders }}</span>
            <span class="unit">单</span>
          </div>
        </el-card>
      </el-col>
      <el-col :span="8">
        <el-card shadow="hover" class="data-card">
          <template #header>
            <div class="card-header">
              <span>今日销售额</span>
              <el-tag type="primary">实时</el-tag>
            </div>
          </template>
          <div class="card-body">
            <span class="num">￥{{ stats.todaySales }}</span>
          </div>
        </el-card>
      </el-col>
      <el-col :span="8">
        <el-card shadow="hover" class="data-card">
          <template #header>
            <div class="card-header">
              <span>待处理单</span>
              <el-tag type="warning">待办</el-tag>
            </div>
          </template>
          <div class="card-body">
            <span class="num">{{ stats.pendingOrders }}</span>
            <span class="unit">项</span>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <el-row :gutter="20" class="mt-20">
      <el-col :span="16">
        <el-card shadow="hover" header="销售额趋势图 (最近7天)">
          <div class="chart-box">
             <div class="mock-chart">
                <div v-for="item in stats.salesTrend" :key="item.date" class="bar" :style="{height: (item.amount / maxAmount * 100 || 0) + '%'}">
                  <div class="tooltip">¥{{ item.amount }}</div>
                </div>
                <div v-if="!stats.salesTrend || stats.salesTrend.length === 0" class="empty-tip">暂无数据</div>
             </div>
             <div class="x-axis">
                <span v-for="item in stats.salesTrend" :key="item.date">{{ item.date.split('-').slice(1).join('/') }}</span>
             </div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="8">
        <el-card shadow="hover" header="最新动态">
          <el-timeline>
            <el-timeline-item v-for="(msg, idx) in stats.activities" :key="idx" :type="idx === 0 ? 'primary' : ''">
              {{ msg }}
            </el-timeline-item>
            <el-empty v-if="!stats.activities || stats.activities.length === 0" description="暂无动态" />
          </el-timeline>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script setup>
import { ref, onMounted, computed } from 'vue'
import request from '../utils/request'

const stats = ref({
  todayOrders: 0,
  todaySales: 0,
  pendingOrders: 0,
  salesTrend: [],
  activities: []
})

const maxAmount = computed(() => {
  if (!stats.value.salesTrend || stats.value.salesTrend.length === 0) return 100
  return Math.max(...stats.value.salesTrend.map(i => i.amount)) || 100
})

const loadData = async () => {
  try {
    const res = await request.get('/home/statistics')
    stats.value = res
  } catch (err) {
    console.error('加载统计数据失败', err)
  }
}

onMounted(() => {
  loadData()
})
</script>

<style scoped>
.mt-20 { margin-top: 20px; }

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.card-body {
  padding: 10px 0;
}

.num {
  font-size: 32px;
  font-weight: bold;
  color: #303133;
}

.unit {
  font-size: 14px;
  color: #909399;
  margin-left: 5px;
}

.chart-box {
  height: 350px;
  display: flex;
  flex-direction: column;
  padding-top: 40px;
}

.mock-chart {
  flex: 1;
  display: flex;
  align-items: flex-end;
  justify-content: space-around;
  padding: 0 40px;
  border-bottom: 2px solid #ebeef5;
}

.bar {
  width: 40px;
  background: linear-gradient(to top, #409EFF, #67C23A);
  border-radius: 6px 6px 0 0;
  transition: all 0.3s ease;
  position: relative;
  cursor: pointer;
}

.bar:hover {
  filter: brightness(1.1);
  box-shadow: 0 0 15px rgba(64, 158, 255, 0.3);
}

.tooltip {
  position: absolute;
  top: -40px;
  left: 50%;
  transform: translateX(-50%);
  background: #303133;
  color: #fff;
  padding: 5px 10px;
  border-radius: 6px;
  font-size: 13px;
  font-weight: bold;
  white-space: nowrap;
  pointer-events: none;
  z-index: 10;
  box-shadow: 0 4px 12px rgba(0,0,0,0.15);
}

.tooltip::after {
  content: '';
  position: absolute;
  bottom: -4px;
  left: 50%;
  transform: translateX(-50%);
  border-left: 4px solid transparent;
  border-right: 4px solid transparent;
  border-top: 4px solid rgba(48, 49, 51, 0.9);
}

.empty-tip {
  color: #999;
  font-size: 14px;
}

.x-axis {
  display: flex;
  justify-content: space-around;
  padding-top: 10px;
  font-size: 13px;
  color: #999;
}
</style>
