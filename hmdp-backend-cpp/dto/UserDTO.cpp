#include "UserDTO.h"

UserDTO::UserDTO(TbUser* tbUser)
{
    id = tbUser->getValueOfId();
    nickName = tbUser->getValueOfNickName();
    icon = tbUser->getValueOfIcon();

    userDto["id"] = id;
    userDto["nickName"] = nickName;
    userDto["icon"] = icon;
}

std::map<std::string, std::string> &UserDTO::getMapObject()
{
    return userDto;
}
