/**
 * 用户类型
 */
export type UserType = {
    id:number;
    username: string;
    userAccount : string;
    avatarUrl?: string;
    gender?: number;
    userPassword?: string;
    phone?: string;
    email?: string;
    userStatus?: number;
    createTime?: Date;
    updateTime?: Date;
    isDelete?: number;
    userRole?: number;
    planetCode?: number;
    tags: string[];
};
