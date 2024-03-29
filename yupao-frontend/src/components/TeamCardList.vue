<template>
    <van-card
            v-for="team in props.teamList"
            :desc="team.description"
            :thumb="defaultPic"
            :title="`${team.name}`"
    >
        <template #tags>
            <van-tag plain type="danger"  style="margin-right: 8px;margin-top: 8px">
                {{teamStatusEnum[team.status]}}
            </van-tag>
        </template>
        <template #bottom>
            <div>
                {{`最大人数：${team.hasJoinNum}/${team.maxNum}`}}
            </div>
            <div v-if="team.expireTime">
                {{'过期时间：' + team.expireTime}}
            </div>
            <div v-if="team.createTime">
                {{'创建时间：' + team.createTime}}
            </div>
        </template>
        <template #footer>
            <van-button v-if="team.userId !== currentUser?.id && !team.hasJoin" size="small" plain  type="primary"  @click="preJoinTeam(team)">加入队伍</van-button>
            <van-button v-if="team.userId === currentUser?.id" size="small" plain  @click="doUpdateTeam(team.id)">更新队伍</van-button>
            <!-- TODO 仅加入队伍可见 -->
            <van-button v-if="team.userId !== currentUser?.id && team.hasJoin" size="small" plain  @click="doQuitTeam(team.id)">退出队伍</van-button>
            <van-button v-if="team.userId === currentUser?.id" size="small" plain  type="danger" @click="doDeleteTeam(team.id)">解散队伍</van-button>
        </template>
    </van-card>
    <van-dialog v-model:show="showPasswordDialog" title="请输入密码" show-cancel-button @confirm="doJoinTeam" @cancel="doJoinCancel">
        <van-field v-model="password" placeholder="请输入密码" />
    </van-dialog>
</template>

<script setup lang="ts">
    import {TeamType} from "../model/team";
    import {teamStatusEnum} from "../constants/team";
    import defaultPic  from "../assets/default.png";
    import myAxios from "../plugins/myAxios";
    import {Toast} from "vant";
    import {onMounted} from "vue";
    import {getCurrentUser} from "../services/user";
    import {ref} from "vue";
    import {UserType} from "../model/user";
    import {useRouter} from "vue-router";

    const router =useRouter();

    const currentUser:UserType = ref();
    const showPasswordDialog = ref(false);
    const password = '';
    const joinTeamId = ref();


    //显示currentUser：[object Object]，没有具体的内容
    onMounted(async () => {
        currentUser.value = await getCurrentUser()
    })

    interface TeamCardListProps {
        teamList:TeamType[];
    }
    const props = withDefaults(defineProps<TeamCardListProps>(),{
        // @ts-ignore
        teamList: [] as TeamType[],
    });

    const preJoinTeam = (team: TeamType) =>{
        joinTeamId.value = team.id;
        if(team.status ===0){
            doJoinTeam()
        }else{
            showPasswordDialog.value =true;
        }
    }

    const doJoinCancel = () =>{
        joinTeamId.value =0;
        password.value = '';
    }
    /**
     * 加入队伍
     * @param id
     */
    const doJoinTeam = async () =>{
        if(!joinTeamId.value){
            return;
        }
        const res = await myAxios.post('/team/join',{
            teamId:joinTeamId.value,
            password:password.value,
        });

        if(res?.code === 0){
            Toast.success('加入成功')
            doJoinCancel();
        }
        else{
            Toast.fail('加入失败' + (res.description?`, ${res.description}`:''))
        }
    }

    /**
     * 更新队伍
     * @param id
     */
    const doUpdateTeam = async (id:number) =>{
        router.push({
            path: '/team/update',
            query:{
                id,
            }
        });
    }

    /**
     * 退出队伍
     * @param id
     */
    const doQuitTeam = async (id:number) =>{
        const res = await myAxios.post('/team/quit',{
            teamId:id
        });

        if(res?.code === 0){
            Toast.success('操作成功')
        }
        else{
            Toast.fail('操作失败' + (res.description?`, ${res.description}`:''))
        }
    }

    /**
     * 解散队伍
     * @param id
     */
    const doDeleteTeam = async (id:number) =>{
        const res = await myAxios.post('/team/delete',{
            id
        });

        if(res?.code === 0){
            Toast.success('操作成功')
        }
        else{
            Toast.fail('操作失败' + (res.description?`, ${res.description}`:''))
        }
    }

</script>

<style scoped>

</style>
