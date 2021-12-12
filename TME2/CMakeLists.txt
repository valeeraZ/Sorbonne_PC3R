cmake_minimum_required(VERSION 3.17)
project(TME2 C)

set(CMAKE_C_STANDARD 99)

set(INC_DIR ft_v1.0/include)
set(LINK_DIR ft_v1.0/lib)

include_directories(${INC_DIR})
link_directories(${LINK_DIR})
link_libraries(fthread)

add_compile_options( -lfthread -lpthread)

add_executable(main.c paquet.h paquet.c tapis.h tapis.c threads.h threads.c)

target_link_libraries(TME2 fthread)