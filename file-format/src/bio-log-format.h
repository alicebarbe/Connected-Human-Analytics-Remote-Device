#ifndef FILE_FORMAT_BIO_LOG_FORMAT_H
#define FILE_FORMAT_BIO_LOG_FORMAT_H

#define KNAME_BYTES (128)
#define KDATA_START_BYTES (4)
#define KSENSOR_DATA_RAW_BYTES (174)

#define BIO_LOG_FORMAT_IDENTIFIER (0xAA082021)
#define BIO_LOG_ROW_FOOTER (0xdeadd00d)
#define NAN_BYTES (0xDEADFFFF)
#define DATA_START_BYTES {0xdeadd00d, 0xbaddd00d, 0xf00dd00d, 0xcafed00d}

#include <stdint.h>


typedef struct BIO_LOG_FileHeader {
    uint32_t formatIdentifier;
    uint32_t libraryVersion;
    uint32_t applicationVersion;
    uint64_t startTime;
    char applicationName[KNAME_BYTES];
    uint32_t BIO_LOG_dataStartByte[KDATA_START_BYTES];
} BIO_LOG_FileHeader;

typedef struct BIO_LOG_vector {
    uint16_t x; // 2 bytes
    uint16_t y; // 2 bytes
    uint16_t z; // 2 bytes
} BIO_LOG_vector;

typedef struct BIO_LOG_IMU_Data {
    BIO_LOG_vector accel; // 6 bytes
    BIO_LOG_vector gyro; // 6 bytes
    BIO_LOG_vector orientation; // 6 bytes
    BIO_LOG_vector magnet; // 6 bytes
    BIO_LOG_vector gravity; // 6 bytes
} BIO_LOG_IMU_Data;

typedef struct BIO_LOG_SensorData {
    uint64_t timestamp; // 8 bytes
    uint32_t ecgReading; // 4 bytes
    uint32_t alcoholReading; // 4 bytes
    uint32_t emgReading; // 4 bytes
    uint32_t gsrReading; // 4 bytes
    BIO_LOG_IMU_Data imu; // 30 bytes
} BIO_LOG_SensorData;

typedef struct BIO_LOG_DataElement {
    BIO_LOG_SensorData sensorData; // 54 bytes
    uint32_t rowFooter; // 4 bytes
} BIO_LOG_DataElement;

typedef struct BIO_LOG_File {
    int32_t fh; // File handle
    int64_t currentElement; // index of the read/write head
} BIO_LOG_File;

BIO_LOG_File BIO_LOG_open(const char* path, int oflag);
int BIO_LOG_readNextElement(BIO_LOG_File* file, BIO_LOG_SensorData* buf);
int BIO_LOG_appendElement(BIO_LOG_File* file, BIO_LOG_SensorData* buf);
int BIO_LOG_close(BIO_LOG_File* file);



#endif //FILE_FORMAT_BIO_LOG_FORMAT_H
