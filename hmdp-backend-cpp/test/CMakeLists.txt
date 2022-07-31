cmake_minimum_required(VERSION 3.5)
project(usercenter_test CXX)

add_executable(${PROJECT_NAME} test_main.cc)

# ##############################################################################
# If you include the drogon source code locally in your project, use this method
# to add drogon 
# target_link_libraries(${PROJECT_NAME}_test PRIVATE drogon)
#
# and comment out the following lines
target_link_libraries(${PROJECT_NAME} PRIVATE Drogon::Drogon)

ParseAndAddDrogonTests(${PROJECT_NAME})

#头文件目录
INCLUDE_DIRECTORIES( ${HIREDIS_INCLUDE} ${REDIS_PLUS_PLUS_INCLUDE} )

#依赖的LIB
SET(REDIS_TEST_LIB  redis++ hiredis pthread)

#生成 
ADD_EXECUTABLE( test_redis test_redis.cpp)

#连接生成相关库
TARGET_LINK_LIBRARIES( test_redis ${REDIS_TEST_LIB} )
