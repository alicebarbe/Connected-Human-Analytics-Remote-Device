#include <Arduino.h>
#include <crc.h>

#define DATA_HEADER (0xFFCCFFCC)


#define CYCLE_TIME (100)

typedef struct __attribute__((__packed__)) DataStruct {
  uint32_t header;
  uint32_t timestamp;
  uint32_t analogData;
  uint8_t crc;
} DataStruct;

#define TEST_DATA_LEN (9)
uint32_t test_data[TEST_DATA_LEN] = {   0x69696969, 
                                      0xdeadbeef, 
                                      0xdeaddead, 
                                      0xbad00bad, 
                                      0x0badf00d, 
                                      0xcafebabe, 
                                      0xcafed00d, 
                                      0xdeadbabe, 
                                      0xdeadc0de};

DataStruct dstruct;
uint32_t time_offset;
uint32_t test_idx = 0;
uint32_t last_cycle_time = 0;

uint8_t myCrc(uint8_t* buf, uint32_t len) {
  uint8_t rval = 0;
  for (uint32_t i = 0; i < len; i++) {
    rval += buf[i];
  }
  return rval;
}

void setup() {
  // put your setup code here, to run once:
  Serial.begin(9600);
  while (!Serial.available()){continue;}
  last_cycle_time = time_offset = millis();
  dstruct.header = DATA_HEADER;
}

void loop() {
  // put your main code here, to run repeatedly:
  if (test_idx == TEST_DATA_LEN)
    test_idx = 0;
  if (millis() - last_cycle_time > CYCLE_TIME) {
    dstruct.analogData = test_data[test_idx++];
    dstruct.timestamp = millis()-time_offset;
    dstruct.crc = myCrc(reinterpret_cast<uint8_t*>(&dstruct),sizeof(dstruct)-1);
    Serial.write(reinterpret_cast<byte*>(&dstruct),sizeof(dstruct));
    last_cycle_time = millis();
  }
}