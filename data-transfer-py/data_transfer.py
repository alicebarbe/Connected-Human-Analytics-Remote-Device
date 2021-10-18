import serial
import struct
import pandas as pd
from datetime import datetime
import zlib
import time
#import h5py

portName = 'COM3'
structString = '!IIIB'
structSize = struct.calcsize(structString)
HEADER = 0xFFCCFFCC

def main():
    packetCorrupt = 0
    packetGood = 0
    ser = serial.Serial(portName)
    print('taking a quick nap...')
    time.sleep(3)
    #ser.write(str.encode('\x00')) # write one byte to start the daq
    ser.write(b'0')
    print('starting now!')
    start_time = datetime.now()
    # ser.read(4) # intentionally insert offset
    while True:
        try:
            byte_arr = ser.read(structSize)
            data_recv = struct.unpack(structString,byte_arr)
            if (data_recv[0] != HEADER):
                print('Corrupt Packet!')
                packetCorrupt = packetCorrupt+1
                ser.read_until(expected=HEADER.to_bytes(4,'big')) # discard until the next header
                ser.read(structSize-4) # discard bytes of the next packet
                continue
            packetGood = packetGood+1
            print('Header: ' + hex(data_recv[0]) + ' Data: ' + hex(data_recv[1]) + ' Time: ' + str(data_recv[2]) + ' CRC: ' + hex(data_recv[3]) + ' Good Packet: ' + str(packetGood) + ' Bad Packet: ' + str(packetCorrupt))
        except (KeyboardInterrupt):
            break


if (__name__ == '__main__'):
    main()

