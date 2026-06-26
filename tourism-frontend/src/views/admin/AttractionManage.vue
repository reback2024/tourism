<template>
  <div>
    <div style="display:flex;justify-content:space-between;margin-bottom:20px">
      <h3>景点管理</h3>
      <el-button type="primary" @click="openDialog()">新增景点</el-button>
    </div>
    <el-table :data="list" stripe border>
      <el-table-column prop="id" label="ID" width="60" />
      <el-table-column prop="name" label="名称" width="150" />
      <el-table-column prop="city" label="城市" width="80" />
      <el-table-column prop="category" label="分类" width="100" />
      <el-table-column prop="ticketPrice" label="票价" width="80" />
      <el-table-column prop="rating" label="评分" width="70" />
      <el-table-column label="状态" width="80">
        <template #default="{ row }"><el-tag :type="row.status===1?'success':'danger'">{{ row.status===1?'上架':'下架' }}</el-tag></template>
      </el-table-column>
      <el-table-column label="操作" width="220">
        <template #default="{ row }">
          <el-button size="small" @click="openDialog(row)">编辑</el-button>
          <el-button size="small" :type="row.status===1?'warning':'success'" @click="toggleStatus(row)">{{ row.status===1?'下架':'上架' }}</el-button>
          <el-button size="small" type="danger" @click="handleDelete(row.id)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>
    <div style="margin-top:20px;text-align:center">
      <el-pagination v-model:current-page="page" :total="total" :page-size="size" layout="total,prev,pager,next" @change="loadData" />
    </div>

    <el-dialog v-model="dialogVisible" :title="editingId?'编辑景点':'新增景点'" width="700px">
      <el-form :model="form" label-position="top">
        <el-row :gutter="20">
          <el-col :span="12"><el-form-item label="名称"><el-input v-model="form.name" /></el-form-item></el-col>
          <el-col :span="6"><el-form-item label="城市"><el-input v-model="form.city" /></el-form-item></el-col>
          <el-col :span="6"><el-form-item label="分类"><el-select v-model="form.category"><el-option v-for="c in cats" :key="c" :label="c" :value="c" /></el-select></el-form-item></el-col>
        </el-row>
        <el-form-item label="地址"><el-input v-model="form.address" /></el-form-item>
        <el-row :gutter="20">
          <el-col :span="8"><el-form-item label="票价"><el-input-number v-model="form.ticketPrice" :min="0" /></el-form-item></el-col>
          <el-col :span="8"><el-form-item label="开放时间"><el-input v-model="form.openingHours" /></el-form-item></el-col>
          <el-col :span="8"><el-form-item label="图片URL"><el-input v-model="form.imageUrl" /></el-form-item></el-col>
        </el-row>
        <el-form-item label="介绍"><el-input v-model="form.description" type="textarea" :rows="3" /></el-form-item>
      </el-form>
      <template #footer><el-button @click="dialogVisible=false">取消</el-button><el-button type="primary" @click="handleSave">保存</el-button></template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive } from 'vue'
import request from '@/api/request'
import { ElMessage, ElMessageBox } from 'element-plus'

const list = ref([]), page = ref(1), size = 10, total = ref(0), dialogVisible = ref(false), editingId = ref(null)
const cats = ['自然风光','历史古迹','主题公园','博物馆','古镇','海滩','山脉','其他']
const form = reactive({ name:'',city:'',category:'',address:'',ticketPrice:0,openingHours:'',imageUrl:'',description:'',status:1 })

async function loadData() {
  try { const res = await request({ url: '/admin/attractions', params: { page: page.value, size } }); list.value = res.data.records; total.value = res.data.total } catch {}
}
loadData()

function openDialog(row) {
  dialogVisible.value = true
  if (row) { editingId.value = row.id; Object.assign(form, row) }
  else { editingId.value = null; Object.keys(form).forEach(k => form[k] = k==='ticketPrice'?0:'') }
}

async function handleSave() {
  try {
    if (editingId.value) await request({ url: `/admin/attractions/${editingId.value}`, method:'put', data:form })
    else await request({ url: '/admin/attractions', method:'post', data:form })
    ElMessage.success('保存成功'); dialogVisible.value = false; loadData()
  } catch {}
}

async function toggleStatus(row) {
  await request({ url: `/admin/attractions/${row.id}/status`, method:'put' }); loadData()
}

async function handleDelete(id) {
  try { await ElMessageBox.confirm('确定删除？'); await request({ url: `/admin/attractions/${id}`, method:'delete' }); loadData() } catch {}
}
</script>
