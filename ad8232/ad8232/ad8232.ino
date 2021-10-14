/******************************************************************************
Thanks to:
https://github.com/sparkfun/AD8232_Heart_Rate_Monitor
http://www.martyncurrey.com/arduino-with-hc-05-bluetooth-module-at-mode/
******************************************************************************/
#include <SoftwareSerial.h>
SoftwareSerial BTserial(2, 3); // RX | TX
 
void setup() {
  // initialize the serial communication:
  Serial.begin(9600);
  pinMode(10, INPUT); // Setup for leads off detection LO +
  pinMode(11, INPUT); // Setup for leads off detection LO -

  BTserial.begin(9600);  
}

void loop() {
    if ((digitalRead(10) == 1)||(digitalRead(11) == 1)) {
      Serial.println(0, DEC);
      BTserial.println(0, DEC);
    }
    else {
      Serial.println(analogRead(A0), DEC);
      BTserial.println(analogRead(A0), DEC);
    }
  //Wait for a bit to keep serial data from saturating
  delay(1);
}
