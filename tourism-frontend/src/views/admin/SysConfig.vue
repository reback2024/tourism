<template>
  <div>
    <h3 style="margin-bottom:20px">系统配置</h3>
    <el-table :data="list" stripe border>
      <el-table-column prop="configKey" label="配置键" width="180"/><el-table-column prop="configValue" label="配置值"/><el-table-column prop="description" label="说明" width="200"/>
      <el-table-column label="操作" width="100">
        <template #default="{row}"><el-button size="small" @click="edit(row)">编辑</el-button></template>
      </el-table-column>
    </el-table>
    <el-dialog v-model="show" title="编辑配置" width="500px">
      <el-form :model="f" label-position="top">
        <el-form-item label="配置键"><el-input :model-value="f.configKey" disabled /></el-form-item>
        <el-form-item label="配置值"><el-input v-model="f.configValue" type="textarea" :rows="3"/></el-form-item>
      </el-form>
      <template #footer><el-button @click="show=false">取消</el-button><el-button type="primary" @click="save">保存</el-button></template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive } from 'vue'
import request from '@/api/request'
import { ElMessage } from 'element-plus'
const list=ref([]),show=ref(false),f=reactive({id:0,configKey:'',configValue:''})
async function load(){try{const r=await request({url:'/admin/configs'});list.value=r.data}catch{}}
load()
function edit(row){f.id=row.id;f.configKey=row.configKey;f.configValue=row.configValue;show.value=true}
async function save(){await request({url:`/admin/configs/${f.id}`,method:'put',data:f});show.value=false;load();ElMessage.success('已更新')}
</script>
