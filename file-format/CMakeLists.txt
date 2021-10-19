cmake_minimum_required(VERSION 3.16)
project(file_format C)

set(CMAKE_C_STANDARD 11)

add_library(file_format src/bio-log-format.c)
