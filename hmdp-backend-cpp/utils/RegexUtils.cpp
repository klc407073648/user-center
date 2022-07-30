#include "Util.h"

bool UserServiceImpl::checkSpecialCharacter(const std::string &str)
{
    //特殊字符校验
    std::regex vaildPattern("[~!/@#$%^&*()\\-_=+\\|\\[{}\\];:\'\",<.>/?]+");
    std::smatch match;

    if (regex_search(str, match, vaildPattern))
    {
        return true;
    }

    return false;
}
