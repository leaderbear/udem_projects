cmake_minimum_required(VERSION 3.11)
project(check_tests C)

set(CMAKE_C_STANDARD 99)

####################################################################################
# <Libcheck libs>                                                                    #
####################################################################################

add_executable(check_tests checks.c check_utils.h ../code/main.c ../code/main.h ../code/blocking_q.c ../code/blocking_q.h ../code/task.c ../code/task.h call_by_string.c call_by_string.h)
TARGET_LINK_LIBRARIES(check_tests pthread check_pic pthread rt m subunit libelf.a)
#add_executable(TP0_test template.c)
