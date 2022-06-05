<template>
    <van-cell title="昵称" is-link to="/user/edit/" :value="user.username"/>
    <van-cell title="账号" is-link :value="user.userAccount" />
    <van-cell title="头像" is-link to="/user/edit/" >
        <img style="height: 48px" :src="user.avatarUrl"/>
    </van-cell>
    <van-cell title="性别" is-link to="/user/edit/" :value="user.gender" @click="toEdit('gender', '性别',user.gender)"/>
    <van-cell title="电话" is-link to="/user/edit/" :value="user.phone"  @click="toEdit('phone', '电话',user.phone)"/>
    <van-cell title="邮箱" is-link to="/user/edit/" :value="user.email" />
    <van-cell title="星球编号" :value="user.planetCode" />
    <van-cell title="注册时间"  :value="user.createTime.toISOString()" />
</template>

<script setup lang="ts">
 import {useRouter} from "vue-router";

 const user = {
        id:1,
        username: "kkk",
        userAccount : "kkk",
        avatarUrl: "https://fastly.jsdelivr.net/npm/@vant/assets/logo.png",
        gender: '男',
        phone: '123456',
        email: '123456@qq.com',
        createTime: new Date(),
        isDelete: '0',
        userRole: '1',
        planetCode: '1234',
 };

 const router =useRouter()
 const toEdit = ( editKey:string ,editName:string,currentValue:string) => {
     router.push({
         path: '/user/edit',
         query: {
             editKey,
             editName,
             currentValue,
         }
     })
 }

</script>

<style scoped>

</style>
