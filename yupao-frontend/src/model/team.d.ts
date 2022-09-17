/**
 * 队伍类型
 */
import {UserType} from "./user";

export type TeamType = {
    id:number;
    name:string;
    description: string;
    maxNum : number;
    userId:number;
    status: number;
    password?: string;
    expireTime?: Date;
    createTime: Date;
    updateTime: Date;
    createUser?: UserType;
};
