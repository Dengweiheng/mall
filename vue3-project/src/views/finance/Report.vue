<template>
  <div class="finance-report-container">
    <!-- 顶部概览 -->
    <el-row :gutter="20">
      <el-col :span="8">
        <el-card shadow="hover" class="finance-card income">
          <div class="card-title">累计总收入</div>
          <div class="card-value">￥{{ summary.totalIncome || '0.00' }}</div>
          <div class="card-footer">
            <span>较上月 +12.5%</span>
          </div>
        </el-card>
      </el-col>
      <el-col :span="8">
        <el-card shadow="hover" class="finance-card expense">
          <div class="card-title">累计总支出</div>
          <div class="card-value">￥{{ summary.totalExpense || '0.00' }}</div>
          <div class="card-footer">
            <span>各项费用与退款</span>
          </div>
        </el-card>
      </el-col>
      <el-col :span="8">
        <el-card shadow="hover" class="finance-card balance">
          <div class="card-title">系统净周转余额</div>
          <div class="card-value">￥{{ summary.balance || '0.00' }}</div>
          <div class="card-footer">
            <span>实时结算数据</span>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 财务流水列表 -->
    <el-card class="mt-20">
      <template #header>
        <div class="flex-between">
          <span>财务交易流水 (实收/实付)</span>
          <el-date-picker
            v-model="dateRange"
            type="daterange"
            range-separator="至"
            start-placeholder="开始日期"
            end-placeholder="结束日期"
            size="small"
            value-format="YYYY-MM-DD"
            @change="loadData"
          />
        </div>
      </template>

      <el-table :data="tableData" v-loading="loading" stripe>
        <el-table-column prop="createTime" label="交易时间" width="180" />
        <el-table-column prop="type" label="交易类型" width="100">
          <template #default="scope">
            <el-tag :type="scope.row.type === 1 ? 'success' : 'danger'">
              {{ scope.row.type === 1 ? '收入' : '支出' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="amount" label="金额" width="150">
          <template #default="scope">
            <span :class="scope.row.type === 1 ? 'income-text' : 'expense-text'">
              {{ scope.row.type === 1 ? '+' : '-' }}￥{{ scope.row.amount }}
            </span>
          </template>
        </el-table-column>
        <el-table-column prop="remark" label="摘要/详情" show-overflow-tooltip />
        <el-table-column prop="operatorId" label="经办人ID" width="100" />
      </el-table>

      <div class="pagination-box mt-20">
        <el-pagination
          v-model:current-page="pageNum"
          v-model:page-size="pageSize"
          :total="total"
          layout="total, prev, pager, next"
          @current-change="loadList"
        />
      </div>
    </el-card>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import request from '../../utils/request'

const loading = ref(false)
const tableData = ref([])
const total = ref(0)
const pageNum = ref(1)
const pageSize = ref(10)
const dateRange = ref([])
const summary = ref({ totalIncome: 0, totalExpense: 0, balance: 0 })

const loadSummary = async () => {
  try {
    const data = await request.get('/finance/summary')
    summary.value = data
  } catch (err) {
    console.error(err)
  }
}

const loadList = async () => {
  loading.value = true
  try {
    const params = {
      pageNum: pageNum.value,
      pageSize: pageSize.value
    }
    if (dateRange.value && dateRange.value.length === 2) {
      params.startTime = dateRange.value[0] + ' 00:00:00'
      params.endTime = dateRange.value[1] + ' 23:59:59'
    }
    const res = await request.get('/finance/page', { params })
    tableData.value = res.list
    total.value = res.total
  } finally {
    loading.value = false
  }
}

const loadData = () => {
  pageNum.value = 1
  loadList()
  loadSummary()
}

onMounted(() => {
  loadData()
})
</script>

<style scoped>
.finance-report-container { padding: 10px; }
.mt-20 { margin-top: 20px; }

.finance-card {
  height: 160px;
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  color: #fff;
  border: none;
}

.income { background: linear-gradient(135deg, #11998e 0%, #38ef7d 100%); }
.expense { background: linear-gradient(135deg, #eb3349 0%, #f45c43 100%); }
.balance { background: linear-gradient(135deg, #1e3c72 0%, #2a5298 100%); }

.card-title { font-size: 16px; opacity: 0.9; margin-bottom: 10px; }
.card-value { font-size: 32px; font-weight: bold; margin-bottom: 10px; }
.card-footer { font-size: 12px; opacity: 0.8; }

.income-text { color: #67C23A; font-weight: bold; }
.expense-text { color: #F56C6C; font-weight: bold; }

.flex-between { display: flex; justify-content: space-between; align-items: center; }
.pagination-box { display: flex; justify-content: flex-end; }
</style>
