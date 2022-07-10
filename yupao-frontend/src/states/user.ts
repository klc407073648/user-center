import {UserType} from "../model/user";

let currentUser:UserType;

const getCurrentUserState = (user:UserType) => {
    currentUser =user;
}


const setCurrentUserState = ():UserType => {
    return currentUser;
}

export {
    getCurrentUserState,
    setCurrentUserState,
}
