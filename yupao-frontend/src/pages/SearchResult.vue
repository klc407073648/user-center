<template>
    <user-card-list :user-list="userList"/>
    <van-empty v-if ="!userList || userList.length < 1" description="搜索结果为空" />
</template>

<script setup >
    import {useRoute} from "vue-router";
    import {onMounted,ref} from 'vue';
    import myAxios from "../plugins/myAxios";
    import {Toast} from 'vant';
    import qs from 'qs';
    import UserCardList from "../components/UserCardList.vue"

    const route= useRoute()
    console.log(route)

    const {tags} =route.query

    // const mockUser = {
    //     id:111,
    //     username: "kkk",
    //     userAccount : "kkk",
    //     profile: "个人简介",
    //     avatarUrl: "https://fastly.jsdelivr.net/npm/@vant/assets/logo.png",
    //     gender: 1,
    //     phone: '123456',
    //     email: '123456@qq.com',
    //     createTime: new Date(),
    //     userRole: 1,
    //     tags: ['c++','work','student'],
    //     planetCode: '1234',
    // }

    const userList =ref();

    onMounted(async() => {
        console.log(tags)
        const userListData = await myAxios.get('/user/search/tags', {
            params: {
                tagNameList: tags
            },
            paramsSerializer: params => {
                return qs.stringify(params, { indices: false })
            }
        })
        .then(function (response) {
            console.log('/user/search/tags succeed',response);
            console.log('response:'+response)
            return response?.data;
        })
        .catch(function (error) {
             console.error('/user/search/tags error',error);
             Toast.fail('请求失败')
        })
        console.log(userListData)

        if(userListData){
            userListData.forEach( user => {
                if(user.tags){
                    user.tags= JSON.parse(user.tags);
                }
            })
            userList.value = userListData
        }
    })

</script>

<style scoped>

</style>
