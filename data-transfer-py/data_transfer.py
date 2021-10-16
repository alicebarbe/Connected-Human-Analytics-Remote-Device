import serial
import struct
import pandas as pd
from datetime import datetime
import h5py

portName = 'COM5'
structString = '!IIIB'
structSize = struct.calcsize(structString)
print(structSize)
HEADER = 0xFFCCFFCC

def main():
    ser = serial.Serial(portName)
    ser.write(str.encode('\x00')) # write one byte to start the daq
    start_time = datetime.now()
    while True:
        try:
            byte_arr = ser.read(structSize)
            data_recv = struct.unpack(structString,byte_arr)
            if (data_recv[0] != HEADER):
                continue
            print('Header: ' + hex(data_recv[0]) + ' Data: ' + hex(data_recv[1]) + ' Time: ' + str(data_recv[2]) + ' CRC: ' + hex(data_recv[3]))
        except (KeyboardInterrupt):
            break


if (__name__ == '__main__'):
    main()

