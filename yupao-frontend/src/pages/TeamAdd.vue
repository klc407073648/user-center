<template>
    <van-form @submit="onSubmit">
        <van-cell-group inset>
            <van-field
                    v-model="addTeamData.name"
                    name="name"
                    label="队伍名"
                    placeholder="请输入队伍名"
                    :rules="[{ required: true, message: '请输入队伍名' }]"
            />
            <van-field
                    v-model="addTeamData.description"
                    rows="4"
                    autosize
                    label="描述"
                    type="textarea"
                    placeholder="请输入队伍描述"
            />
            <van-field
                    is-link
                    readonly
                    name="datetimePicker"
                    label="时间选择"
                    :placeholder="addTeamData.expireTime ?? '点击选择过期时间'"
                    @click="showPicker = true"
            />
            <van-popup v-model:show="showPicker" position="bottom">
                <van-datetime-picker
                        v-model="addTeamData.expireTime"
                        @confirm="showPicker = false"
                        type="datetime"
                        title="请选择过期时间"
                        :min-date="minDate"
                />
            </van-popup>
            <van-field name="stepper" label="最大人数">
                <template #input>
                    <van-stepper v-model="addTeamData.maxNum" min="3" max="10"/>
                </template>
            </van-field>
            <van-field name="radio" label="单选框">
                <template #input>
                    <van-radio-group v-model="addTeamData.status" direction="horizontal">
                        <van-radio name="0">公开</van-radio>
                        <van-radio name="1">私有</van-radio>
                        <van-radio name="2">加密</van-radio>
                    </van-radio-group>
                </template>
            </van-field>
            <van-field
                    v-if = "Number(addTeamData.status) === 2"
                    v-model="addTeamData.password"
                    type="password"
                    name="userPassword"
                    label="密码"
                    placeholder="请输入队伍密码"
                    :rules="[{ required: true, message: '请输入队伍密码' }]"
            />
        </van-cell-group>
        <div style="margin: 16px;">
            <van-button round block type="primary" native-type="submit">
                提交
            </van-button>
        </div>
    </van-form>
</template>

<script setup lang="ts">

    import {ref} from "vue";
    import myAxios from "../plugins/myAxios";
    import {useRouter} from "vue-router";
    import {Toast} from "vant";

    const router =useRouter();

    const initFormData = {
        "description": "",
        "expireTime": "",
        "maxNum": 3,
        "name": "",
        "password": "",
        "status": 0,
        "userId": 0
    }

    //用户填写的表单数据
    const addTeamData = ref({...initFormData})
    const showPicker = ref(false)
    const minDate = new Date()

    const onSubmit = async () => {
        const postData = {
            ...addTeamData.value,
            status:Number(addTeamData.value.status)
        }
        //tod 前端参数校验
        const res = await myAxios.post("/team/add",postData);

        if(res?.code === 0 && res.data){
            router.push({
                path: '/team',
                replace:true,
            });
        }else{
            Toast.fail('添加失败');
        }
    }
</script>

<style scoped>

</style>
