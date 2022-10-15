<template>
    <van-cell center title="心动模式">
        <template #right-icon>
            <van-switch v-model="isMatchMode" size="24" />
        </template>
    </van-cell>
    <user-card-list :user-list="userList" :loading="loading"/>
    <van-empty v-if="!userList || userList.length < 1" description="数据为空"/>
</template>

<script setup lang="ts">
    import {useRoute} from "vue-router";
    import {onMounted, ref, watchEffect} from 'vue';
    import myAxios from "../plugins/myAxios";
    import {Toast} from 'vant';
    import UserCardList from "../components/UserCardList.vue"
    import {UserType} from "../model/user";

    const isMatchMode = ref<boolean>(false);

    const route = useRoute()
    console.log(route)

    const {tags} = route.query

    const userList = ref();
    const loading = ref(true);

    /**
     * 加载数据
     */
    const loadDate = async () =>{
        let userListData;
        loading.value =true;
        //心动模式，根据标签匹配
        if(isMatchMode.value){
            const num = 2;
            userListData = await myAxios.get('/user/match', {
                params: {
                    num
                },
            })
                .then(function (response) {
                    console.log('/user/match succeed', response);
                    console.log('response:' + response)
                    return response?.data;
                })
                .catch(function (error) {
                    console.error('/user/match error', error);
                    Toast.fail('请求失败')
                })
        }
        else {
            //普通模式，直接分页查询
            userListData = await myAxios.get('/user/recommend', {
                params: {
                    pageSize: 8,
                    pageNum:1
                },
            })
                .then(function (response) {
                    console.log('/user/recommend succeed', response);
                    console.log('response:' + response)
                    return response?.data?.records;
                })
                .catch(function (error) {
                    console.error('/user/recommend error', error);
                    Toast.fail('请求失败')
                })
        }
        loading.value =false;

        if (userListData) {
            userListData.forEach((user:UserType) => {
                if (user.tags) {
                    user.tags = JSON.parse(user.tags);
                }
            })
            userList.value = userListData
        }
    }

    watchEffect( () =>{
        console.log("current matchMode: " + isMatchMode.value);
        loadDate();
    })

</script>

<style scoped>

</style>
