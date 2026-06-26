<template>
  <div>
    <div style="display:flex;justify-content:space-between;margin-bottom:20px"><h3>餐厅管理</h3><el-button type="primary" @click="open()">新增餐厅</el-button></div>
    <el-table :data="list" stripe border>
      <el-table-column prop="id" label="ID" width="60"/><el-table-column prop="name" label="名称" width="160"/><el-table-column prop="city" label="城市" width="80"/><el-table-column prop="cuisineType" label="菜系" width="80"/>
      <el-table-column prop="pricePerPerson" label="人均" width="80"/><el-table-column prop="rating" label="评分" width="70"/>
      <el-table-column label="状态" width="80"><template #default="{row}"><el-tag :type="row.status===1?'success':'danger'">{{row.status===1?'上架':'下架'}}</el-tag></template></el-table-column>
      <el-table-column label="操作"><template #default="{row}"><el-button size="small" @click="open(row)">编辑</el-button><el-button size="small" :type="row.status===1?'warning':'success'" @click="toggle(row)">{{row.status===1?'下架':'上架'}}</el-button><el-button size="small" type="danger" @click="del(row.id)">删除</el-button></template></el-table-column>
    </el-table>
    <div style="margin-top:20px;text-align:center"><el-pagination v-model:current-page="page" :total="total" :page-size="size" layout="total,prev,pager,next" @change="load" /></div>
    <el-dialog v-model="show" :title="editId?'编辑':'新增'" width="700px">
      <el-form :model="f" label-position="top">
        <el-row :gutter="20"><el-col :span="12"><el-form-item label="名称"><el-input v-model="f.name"/></el-form-item></el-col><el-col :span="6"><el-form-item label="城市"><el-input v-model="f.city"/></el-form-item></el-col><el-col :span="6"><el-form-item label="菜系"><el-select v-model="f.cuisineType"><el-option v-for="c in types" :key="c" :label="c" :value="c"/></el-select></el-form-item></el-col></el-row>
        <el-form-item label="地址"><el-input v-model="f.address"/></el-form-item>
        <el-row :gutter="20"><el-col :span="8"><el-form-item label="人均价格"><el-input-number v-model="f.pricePerPerson"/></el-form-item></el-col><el-col :span="8"><el-form-item label="价格区间"><el-input v-model="f.priceRange"/></el-form-item></el-col><el-col :span="8"><el-form-item label="电话"><el-input v-model="f.phone"/></el-form-item></el-col></el-row>
        <el-form-item label="图片URL"><el-input v-model="f.imageUrl"/></el-form-item>
        <el-form-item label="介绍"><el-input v-model="f.description" type="textarea" :rows="3"/></el-form-item>
      </el-form>
      <template #footer><el-button @click="show=false">取消</el-button><el-button type="primary" @click="save">保存</el-button></template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive } from 'vue'
import request from '@/api/request'
import { ElMessage, ElMessageBox } from 'element-plus'
const list=ref([]),page=ref(1),size=10,total=ref(0),show=ref(false),editId=ref(null)
const types=['本地特色','川菜','粤菜','西餐','日料','火锅','烧烤','小吃','海鲜','素食']
const f=reactive({name:'',city:'',cuisineType:'',pricePerPerson:0,priceRange:'',phone:'',imageUrl:'',description:'',address:''})
async function load(){try{const r=await request({url:'/admin/restaurants',params:{page:page.value,size}});list.value=r.data.records;total.value=r.data.total}catch{}}
load()
function open(row){show.value=true;if(row){editId.value=row.id;Object.assign(f,row)}else{editId.value=null;Object.keys(f).forEach(k=>f[k]=k==='pricePerPerson'?0:'')}}
async function save(){try{if(editId.value)await request({url:`/admin/restaurants/${editId.value}`,method:'put',data:f});else await request({url:'/admin/restaurants',method:'post',data:f});ElMessage.success('OK');show.value=false;load()}catch{}}
async function toggle(row){await request({url:`/admin/restaurants/${row.id}/status`,method:'put'});load()}
async function del(id){try{await ElMessageBox.confirm('确定？');await request({url:`/admin/restaurants/${id}`,method:'delete'});load()}catch{}}
</script>
