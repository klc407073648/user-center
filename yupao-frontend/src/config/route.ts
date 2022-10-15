import Index from "../pages/Index.vue";
import Team from "../pages/Team.vue";
import TeamAdd from "../pages/TeamAdd.vue";
import TeamUpdate from "../pages/TeamUpdate.vue";
import User from "../pages/User.vue";
import Search from "../pages/Search.vue";
import SearchResult from "../pages/SearchResult.vue";
import UserEdit from "../pages/UserEdit.vue";
import UserLogin from "../pages/UserLogin.vue";

import UserUpdate from "../pages/UserUpdate.vue";
import UserTeamJoin from "../pages/UserTeamJoin.vue";
import UserTeamCreate from "../pages/UserTeamCreate.vue";

const routes = [
    { path: '/', component: Index },
    { path: '/team', title: '找队伍', component: Team },
    { path: '/team/add', title: '创建队伍', component: TeamAdd },
    { path: '/team/update', title: '更新队伍', component: TeamUpdate },
    { path: '/user', title: '个人信息', component: User },
    { path: '/search', title: '找伙伴', component: Search },
    { path: '/user/list',title: '用户列表',  component: SearchResult },
    { path: '/user/edit/',title: '编辑信息',  component: UserEdit },
    { path: '/user/login/',title: '登录', component: UserLogin },
    { path: '/user/update', title: '更新信息', component: UserUpdate },
    { path: '/user/team/join', title: '加入队伍', component: UserTeamJoin },
    { path: '/user/team/create', title: '创建队伍', component: UserTeamCreate },
]

export  default  routes;
