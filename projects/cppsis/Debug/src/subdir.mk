################################################################################
# Automatically-generated file. Do not edit!
################################################################################

# Add inputs and outputs from these tool invocations to the build variables 
CPP_SRCS += \
../src/LineScanner.cpp \
../src/ScannedColumn.cpp \
../src/cppsis.cpp 

OBJS += \
./src/LineScanner.o \
./src/ScannedColumn.o \
./src/cppsis.o 

CPP_DEPS += \
./src/LineScanner.d \
./src/ScannedColumn.d \
./src/cppsis.d 


# Each subdirectory must supply rules for building sources it contributes
src/%.o: ../src/%.cpp
	@echo 'Building file: $<'
	@echo 'Invoking: Cross G++ Compiler'
	g++ -I/usr/local/include/eigen -I/usr/local/include/opencv -I/usr/local/include/opencv2 -O0 -g3 -Wall -c -fmessage-length=0 -MMD -MP -MF"$(@:%.o=%.d)" -MT"$(@:%.o=%.d)" -o "$@" "$<"
	@echo 'Finished building: $<'
	@echo ' '


