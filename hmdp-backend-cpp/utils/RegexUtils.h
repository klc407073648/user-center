/**
 * @file Util.h
 * @brief 常用工具类
 * @author klc
 * @date 2020-04-30
 * @copyright Copyright (c) 2020年 klc
 */

#ifndef __STIBEL_UTIL_H_
#define __STIBEL_UTIL_H_

#include <string>
#include <iostream>

/**
* @brief 正则工具类
*/
class RegexUtils {
public:
    static std::string loadFile(const std::string &filename);
    static std::vector<std::string> getFileListFromDir(const std::string& dirPath);
    static bool isFile(const std::string& path);
    static bool isDir(const std::string& path);
    static bool createDir(const std::string& path);
    static bool copyFile(const std::string& srcFile,const std::string& dstFile);
    static bool copyDir(const std::string& srcDir,const std::string& dstDir, const bool& isCover);
};

}//namespace StiBel

#endif //__STIBEL_UTIL_H_
