<template>
  <div>
    <h3 style="margin-bottom:20px">用户管理</h3>
    <el-table :data="list" stripe border>
      <el-table-column prop="id" label="ID" width="60"/><el-table-column prop="username" label="用户名" width="120"/><el-table-column prop="nickname" label="昵称" width="120"/><el-table-column prop="role" label="角色" width="100"><template #default="{row}"><el-tag :type="row.role==='ADMIN'?'danger':'info'">{{row.role}}</el-tag></template></el-table-column>
      <el-table-column label="状态" width="100"><template #default="{row}"><el-tag :type="row.status===1?'success':'danger'">{{row.status===1?'正常':'禁用'}}</el-tag></template></el-table-column>
      <el-table-column prop="createTime" label="注册时间" width="170"/>
      <el-table-column label="操作">
        <template #default="{row}">
          <el-button size="small" :type="row.status===1?'warning':'success'" @click="toggleStatus(row)">{{row.status===1?'禁用':'启用'}}</el-button>
          <el-button size="small" @click="changeRole(row)">{{row.role==='ADMIN'?'降为普通用户':'升为管理员'}}</el-button>
        </template>
      </el-table-column>
    </el-table>
    <div style="margin-top:20px;text-align:center"><el-pagination v-model:current-page="page" :total="total" :page-size="size" layout="total,prev,pager,next" @change="load" /></div>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import request from '@/api/request'
import { ElMessage, ElMessageBox } from 'element-plus'
const list=ref([]),page=ref(1),size=10,total=ref(0)
async function load(){try{const r=await request({url:'/admin/users',params:{page:page.value,size}});list.value=r.data.records;total.value=r.data.total}catch{}}
load()
async function toggleStatus(row){await request({url:`/admin/users/${row.id}/status`,method:'put'});load();ElMessage.success('操作成功')}
async function changeRole(row){
  try{await ElMessageBox.confirm(`确定将 ${row.username} ${row.role==='ADMIN'?'降为普通用户':'升为管理员'}？`);await request({url:`/admin/users/${row.id}/role`,method:'put',data:{role:row.role==='ADMIN'?'USER':'ADMIN'}});load();ElMessage.success('角色已变更')}catch{}
}
</script>
