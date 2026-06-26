<template>
  <div>
    <h3 style="margin-bottom:20px">内容审核</h3>
    <el-table :data="list" stripe border>
      <el-table-column prop="id" label="ID" width="60"/><el-table-column prop="username" label="用户" width="100"/><el-table-column label="类型" width="100"><template #default="{row}"><el-tag>{{row.targetType==='ATTRACTION'?'景点':row.targetType==='HOTEL'?'酒店':'餐厅'}}</el-tag></template></el-table-column>
      <el-table-column prop="rating" label="评分" width="70"/><el-table-column prop="content" label="内容" show-overflow-tooltip/>
      <el-table-column label="状态" width="100"><template #default="{row}"><el-tag :type="row.status===1?'success':row.status===-1?'danger':'warning'">{{row.status===1?'通过':row.status===-1?'拒绝':'待审'}}</el-tag></template></el-table-column>
      <el-table-column prop="createTime" label="时间" width="170"/>
      <el-table-column label="操作" width="150">
        <template #default="{row}"><el-button size="small" type="success" @click="audit(row,1)" v-if="row.status!==1">通过</el-button><el-button size="small" type="danger" @click="audit(row,-1)" v-if="row.status!==-1">拒绝</el-button></template>
      </el-table-column>
    </el-table>
    <div style="margin-top:20px;text-align:center"><el-pagination v-model:current-page="page" :total="total" :page-size="size" layout="total,prev,pager,next" @change="load" /></div>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import request from '@/api/request'
import { ElMessage } from 'element-plus'
const list=ref([]),page=ref(1),size=10,total=ref(0)
async function load(){try{const r=await request({url:'/admin/reviews',params:{page:page.value,size}});list.value=r.data.records;total.value=r.data.total}catch{}}
load()
async function audit(row,status){await request({url:`/admin/reviews/${row.id}/audit`,method:'put',data:{status}});load();ElMessage.success('已审核')}
</script>
